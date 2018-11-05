package org.rubik.redis;

import java.util.concurrent.TimeUnit;

import org.rubik.redis.RedisConsts.EXPX;
import org.rubik.redis.RedisConsts.NXXX;
import org.rubik.redis.caller.LuaCaller;
import org.rubik.redis.caller.StringCaller;
import org.rubik.util.common.KeyUtil;

/**
 * redis 分布式锁
 */
public class Locker {

	private LuaCaller luaCaller;
	private StringCaller stringCaller;
	
	/**
	 * 分布式锁：尝试获取指定资源的锁，获取成功返回唯一锁id，失败则返回null。只尝试获取一次
	 * 
	 * @param lock
	 * @return
	 */
	public String tryLock(String lock, int expire) {
		String lockId = KeyUtil.uuid();
		return stringCaller.set(lock, lockId, NXXX.NX, EXPX.PX, expire) ? lockId : null;
	}
	
	/**
	 * 分布式锁：获取指定资源的锁，直到超时，成功则返回锁id，失败或者超时返回null。指定超时时间
	 * 
	 * @param lock
	 * @return
	 */
	public String lock(String lock, int wait, int expire) {
		long begin = System.nanoTime();
		while (true) {
			String lockId = tryLock(lock, expire);
			if (null != lockId)
				return lockId;
			
			long time = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - begin);
			if (time >= wait)
				return null;
			Thread.yield();
		}
	}
	
	/**
	 * 释放锁资源：建议获取锁成功之后将释放锁资源写在 final 块中
	 * 
	 * @param lock
	 * @param lockId
	 * @return
	 */
	public boolean releaseLock(String lock, String lockId) {
		return luaCaller.delIfEquals(lock, lockId);
	}
	
	public void setLuaCaller(LuaCaller luaCaller) {
		this.luaCaller = luaCaller;
	}
	
	public void setStringCaller(StringCaller stringCaller) {
		this.stringCaller = stringCaller;
	}
}
