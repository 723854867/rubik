package org.rubik.redis.caller;

import static org.rubik.redis.RedisUtil.encode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.rubik.bean.core.Assert;
import org.rubik.redis.ILuaCmd;
import org.rubik.redis.LuaCmd;
import org.rubik.util.callback.Callback;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisNoScriptException;
import redis.clients.util.Pool;

@Slf4j
public class LuaCaller extends Caller {
	
	private Map<String, Script> scripts = new ConcurrentHashMap<String, Script>();	
	
	public LuaCaller() {}
	
	public LuaCaller(Pool<Jedis> jedisPool) {
		super(jedisPool);
	}
	
	public void addScript(String cmd, byte[] content) {
		Script script = new Script(content);
		Script old = scripts.put(cmd, script);
		if (null != old)
			log.info("Script {} conflict, old script was replaced!", cmd);
	}
	
	/**
	 * 如果 key 值存在并且值等于 value 则删除 value 然后返回 true，否则什么也不做返回 false
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean delIfEquals(String key, String value) {
		long flag = execute(LuaCmd.DEL_IF_EQUALS, key, value);
		return flag == 1;
	}
	
	/**
	 * 范围设置bitmap
	 * 
	 * @param key bitmap key
	 * @param start 开始offset包括start
	 * @param stop 结束offset包括stop
	 * @param value 值
	 */
	public void setBitRange(String key, long start, long stop, boolean value) {
		if (start >= stop)
			throw new IllegalArgumentException("start must less than stop");
		execute(LuaCmd.SET_BIT_RANGE, key, start, stop, value ? 1 : 0);
	}
	
	/**
	 * 存储获取验证码
	 * 
	 * @param captchaKey 存放验证码的 key
	 * @param countKey 存放验证码获取次数的 key
	 * @param captcha 验证码
	 * @param lifeTime 验证码有效时长，单位毫秒
	 * @param countMaxinum 验证码最大获取次数
	 * @param countLifetTime 验证码次数生命周期(超过该时间没有获取验证码，则验证码次数 key 会被删除，也就是说验证码次数会被清零)，单位毫秒
	 * @param interval 两次获取验证码之间的时间间隔：在该时间之内再次获取会提示验证码获取太频繁，单位毫秒
	 */
	public long captchaStorage(String captchaKey, String countKey, String captcha, int lifeTime, int countMaxinum, int countLifetTime, int interval) {
		return execute(LuaCmd.CAPTCHA_STORAGE, captchaKey, countKey, captcha, lifeTime, countMaxinum, countLifetTime, interval);
	}
	
	public boolean captchaVerify(Object... params) {
		long flag = execute(params.length / 2, LuaCmd.CAPTCHA_VERIFY, params);
		return 0 == flag;
	}
	
	/**
	 * 网关限速：仅仅限制速率，每隔recoveryTime才可以进行下一次访问
	 * 
	 * @return 0-成功增加计数;1-访问总数超过限制;2-访问太频繁
	 */
	public long gatewayRateLimit(@NonNull String key, @NonNull TimeUnit recoveryTimeUnit, int recoveryTime) {
		if (recoveryTime > 0 && recoveryTimeUnit != TimeUnit.NANOSECONDS && recoveryTimeUnit != TimeUnit.MICROSECONDS)
			return execute(LuaCmd.GATEWAY_RATE_LIMIT, key, recoveryTimeUnit.toMillis(recoveryTime));
		else
			throw new IllegalArgumentException("time unit type error or param error");
	}
	
	/**
	 * 网关限速：仅仅限制总数，在timerExpire之内只能访问 countLimit次
	 * <pre>
	 * 注意countLimit的统计采用的是滑动窗口计数。timerExpire为10秒，countLimit为2。加入第一次访问时间为1，第二次访问时间为5，则第三次可以访问的时间点是11，第四次能访问的时间点是15依次类推
	 * </pre>
	 * 
	 * @return 0-成功增加计数;1-访问总数超过限制;2-访问太频繁
	 */
	public long gatewayRateLimit(@NonNull String key, @NonNull TimeUnit timerUnit, long timerExpire, int countLimit) { 
		if (countLimit > 0 && timerExpire > 0 && timerUnit != TimeUnit.NANOSECONDS && timerUnit != TimeUnit.MICROSECONDS) {
			long timeSliceExpiryMills = timerUnit.toMillis(timerExpire);			// 本次计时器的失效时间
			long expiredTimer = System.currentTimeMillis() - timeSliceExpiryMills;			// 当前时间有效的计时器时间起始时间
			return execute(LuaCmd.GATEWAY_RATE_LIMIT, key, System.currentTimeMillis(), timeSliceExpiryMills, expiredTimer, countLimit, 0);
		} else 
			throw new IllegalArgumentException("time unit type error or param error");
	}

	/**
	 * 网关限速：同时限制总访问次数和访问速率
	 * <pre>
	 * 需要注意的是{@link #gatewayRateLimit(String, TimeUnit, int)} 和其他两个网关限速实现机制不一样，它使用的是setpx类型，而其他两个网关限速
	 * 使用的都是 sortedset。因此如果要从有访问总数限制的限速模式切换到仅仅限制速率或者反向切换时需要等前一类的限速结算完全结束才行。而限制访问总次数的两类限速之间可以随意切
	 * 换。
	 * </pre>
	 * 
	 * @return 0-成功增加计数;1-访问总数超过限制;2-访问太频繁
	 */
	public long gatewayRateLimit(@NonNull String key, @NonNull TimeUnit timerUnit, long timerExpire, int countLimit, @NonNull TimeUnit recoveryTimeUnit, int recoveryTime) { 
		Assert.isTrue(recoveryTime > 0 && recoveryTimeUnit != TimeUnit.NANOSECONDS && recoveryTimeUnit != TimeUnit.MICROSECONDS, "time unit type error or param error");
		Assert.isTrue(countLimit > 0 && timerExpire > 0 && timerUnit != TimeUnit.NANOSECONDS && timerUnit != TimeUnit.MICROSECONDS, "time unit type error or param error");
		long timeSliceExpiryMills = timerUnit.toMillis(timerExpire);			// 本次计时器的失效时间
		long expiredTimer = System.currentTimeMillis() - timeSliceExpiryMills;			// 当前时间有效的计时器时间起始时间
		return execute(LuaCmd.GATEWAY_RATE_LIMIT, key, System.currentTimeMillis(), timeSliceExpiryMills, expiredTimer, countLimit, recoveryTimeUnit.toMillis(recoveryTime));
	}
	
	public <T> T execute(ILuaCmd cmd, Object... params) {
		return invoke(cmd.keyCount(), cmd.key().toLowerCase(), params);
	}
	
	public <T> T execute(int keyNum, ILuaCmd cmd, Object... params) {
		return invoke(keyNum, cmd.key().toLowerCase(), params);
	}
	
	/**
	 * 执行  lua 脚本
	 * 
	 * @param keyNum key 数量
	 * @param cmd lua脚本名
	 * @param params 脚本所需参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	<T> T invoke(int keyNum, String cmd, Object... params) {
		Script script = scripts.get(cmd);
		if (null == script)
			throw new JedisNoScriptException("Script " + cmd + " not exist!");
		byte[][] arr = encode(params);
		return invoke(new Callback<Jedis, T>() {
			@Override
			public T invoke(Jedis jedis) {
				if (script.stored) {
					try {
						return (T) jedis.evalsha(encode(script.sha1Key), keyNum, arr);
					} catch (JedisNoScriptException e) {
						log.warn("redis lua 脚本 - {} 缓存未命中,直接执行脚本！", cmd);
					}
				}
				T object = (T) jedis.eval(script.content, keyNum, arr);
				script.stored = true;
				return object;
			}
		});
	}
	
	private class Script {
		private String sha1Key;
		private byte[] content;
		private boolean stored;
		private Script(byte[] content) {
			this.sha1Key = DigestUtils.sha1Hex(content);
			this.content = encode(new String(content));
		}
	}
}
