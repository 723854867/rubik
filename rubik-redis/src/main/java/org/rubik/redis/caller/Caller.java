package org.rubik.redis.caller;

import org.rubik.util.callback.Callback;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

@Slf4j
public abstract class Caller {

	private Pool<Jedis> jedisPool;
	
	public Caller() {}
	
	public Caller(Pool<Jedis> jedisPool) {
		this.jedisPool = jedisPool;
	}
	
	/**
	 * 执行 redis 命令
	 * 
	 * @param invoke
	 * @return
	 */
	protected <T> T invoke(Callback<Jedis, T> invoke) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return invoke.invoke(jedis);
		} catch (Exception e) {
			log.error("Jedis connection get failure!", e);
			throw e;
		} finally {
			if (null != jedis)
				jedis.close();
		}
	}
	
	public void setJedisPool(Pool<Jedis> jedisPool) {
		this.jedisPool = jedisPool;
	}
}
