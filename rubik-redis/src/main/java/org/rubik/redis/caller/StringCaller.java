package org.rubik.redis.caller;

import static org.rubik.redis.RedisUtil.encode;

import org.rubik.redis.RedisConsts;
import org.rubik.redis.RedisConsts.EXPX;
import org.rubik.redis.RedisConsts.NXXX;
import org.rubik.util.callback.Callback;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

public class StringCaller extends Caller {
	
	public StringCaller() {}
	
	public StringCaller(Pool<Jedis> jedisPool) {
		super(jedisPool);
	}

	/**
	 * 返回key的value。如果key不存在，返回null。如果key的value不是string，就返回错误，因为GET只处理string类型的values
	 * @param key
	 * @return
	 */
	public byte[] get(Object key) {
		return invoke(new Callback<Jedis, byte[]>() {
			@Override
			public byte[] invoke(Jedis jedis) {
				return jedis.get(encode(key));
			}
		});
	}
	
	/**
	 * 返回key对应的string在offset处的bit值 当offset超出了字符串长度的时候，这个字符串就被假定为由0比特填充的连续空间。当key不存在的时候，它就认为是一个空字符串，
	 * 
	 * @param key
	 * @param offset
	 * @return
	 */
	public boolean getBit(Object key, long offset) { 
		return invoke(new Callback<Jedis, Boolean>() {
			@Override
			public Boolean invoke(Jedis jedis) {
				return jedis.getbit(encode(key), offset);
			}
		});
	}

	/**
	 * 将键key设定为指定的“字符串”值。如果	key	已经保存了一个值，那么这个操作会直接覆盖原来的值，并且忽略原始类型。当set命令执行成功之后，之前设置的过期时间都将失效
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(Object key, Object value) {
		String result = invoke(new Callback<Jedis, String>() {
			@Override
			public String invoke(Jedis jedis) {
				return jedis.set(encode(key), encode(value));
			}
		});
		return null != result && result.equalsIgnoreCase(RedisConsts.OK);
	}

	/**
	 * 和 {@link #set(String, String)} 功能相似但是要求redis版本大于等于2.6.12
	 * 由于SET命令加上选项已经可以完全取代SETNX, SETEX, PSETEX的功能，所以在将来的版本中，redis可能会不推荐使用并且最终抛弃这几个命令。
	 * 
	 * @param key
	 * @param value
	 * @param nxxx 是否在键已经存在时设置
	 * @param expx 设置键的过期时间
	 * @param timeout 具体的过期时间
	 * @return
	 */
	public boolean set(Object key, Object value, NXXX nxxx, EXPX expx, int timeout) {
		String result = invoke(new Callback<Jedis, String>() {
			@Override
			public String invoke(Jedis jedis) {
				return jedis.set(encode(key), encode(value), encode(nxxx.name()), encode(expx.name()), timeout);
			}
		});
		return null != result && result.equalsIgnoreCase(RedisConsts.OK);
	}
	
	/**
	 * 设置或者清空key的value(字符串)在offset处的bit值
	 * 
	 * @param key
	 * @param offset 取值范围是0 ~ 2的32次幂-1即(0 ~ 4294967295l)不在该范围内会报错
	 * @param value
	 * @return 这里千万注意返回的是在offset处原来的值，而不是设置是否成功也不是设置的值
	 */
	public boolean setBit(Object key, long offset, boolean value) {
		return invoke(new Callback<Jedis, Boolean>() {
			@Override
			public Boolean invoke(Jedis jedis) {
				return jedis.setbit(encode(key), offset, value);
			}
		});
	}
}
