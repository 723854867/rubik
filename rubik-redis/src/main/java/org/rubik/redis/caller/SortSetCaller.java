package org.rubik.redis.caller;

import static org.rubik.redis.RedisUtil.encode;

import java.util.Set;

import org.rubik.util.callback.Callback;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

public class SortSetCaller extends Caller {
	
	public SortSetCaller() {}
	
	public SortSetCaller(Pool<Jedis> jedisPool) {
		super(jedisPool);
	}

	public Set<byte[]> zrange(Object key, long start, long end) {
		return invoke(new Callback<Jedis, Set<byte[]>>() {
			@Override
			public Set<byte[]> invoke(Jedis jedis) {
				return jedis.zrange(encode(key), start, end);
			}
		});
	}
}
