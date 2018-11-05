package org.rubik.redis.caller;

import static org.rubik.redis.RedisUtil.encode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.rubik.util.callback.Callback;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

public class HashCaller extends Caller {
	
	public HashCaller() {}
	
	public HashCaller(Pool<Jedis> jedisPool) {
		super(jedisPool);
	}
	
	/**
	 * 删除 hash 类型 key 中指定的 fields。不存在的 field 会被忽略。如果key不存在则返回0
	 * 
	 * @param key
	 * @param fields
	 * @return 被移除的 fields 的数量
	 */
	public long del(Object key, Object...fields) { 
		return invoke(new Callback<Jedis, Long>() {
			@Override
			public Long invoke(Jedis jedis) {
				return jedis.hdel(encode(key), encode(fields));
			}
		});
	}
	
	/**
	 * 判断 key 中是否存在字段 field
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public boolean exists(Object key, Object field) { 
		return invoke(new Callback<Jedis, Boolean>() {
			@Override
			public Boolean invoke(Jedis jedis) {
				return jedis.hexists(encode(key), encode(field));
			}
		});
	}

	/**
	 * 返回 key 指定的哈希集中该字段所关联的值
	 * 
	 * @param key
	 * @param field
	 * @return 当字段不存在或者 key 不存在时返回null
	 */
	public byte[] get(Object key, Object field) {
		return invoke(new Callback<Jedis, byte[]>() {
			@Override
			public byte[] invoke(Jedis jedis) {
				return jedis.hget(encode(key), encode(field));
			}
		});
	}
	
	/**
	 * 返回 key 指定的哈希集中所有的字段和值。返回值中，每个字段名的下一个是它的值，所以返回值的长度是哈希集大小的两倍
	 * 
	 * @param key
	 * @return 当 key 指定的哈希集不存在时返回空列表
	 */
	public Map<byte[], byte[]> getAll(Object key) { 
		return invoke(new Callback<Jedis, Map<byte[], byte[]>>() {
			@Override
			public Map<byte[], byte[]> invoke(Jedis jedis) {
				return jedis.hgetAll(encode(key));
			}
		});
	}
	
	/**
	 * 增加 key 指定的哈希集中指定字段的数值。如果 key 不存在，会创建一个新的哈希集并与 key 关联。如果字段不存在，则字段的值在该操作执行前被设置为 0
	 * 
	 * @param key
	 * @param field
	 * @param increment
	 * @return 增值操作执行后的该字段的值
	 */
	public long incrby(Object key, Object field, long increment) {
		return invoke(new Callback<Jedis, Long>() {
			@Override
			public Long invoke(Jedis jedis) {
				return jedis.hincrBy(encode(key), encode(field), increment);
			}
		}); 
	}
	
	public List<byte[]> mget(Object key, Object... fields) {
		return invoke(new Callback<Jedis, List<byte[]>>() {
			@Override
			public List<byte[]> invoke(Jedis jedis) {
				return jedis.hmget(encode(key), encode(fields));
			}
		});
	}
	
	public long set(Object key, Object field, Object value) {
		return invoke(new Callback<Jedis, Long>() {
			@Override
			public Long invoke(Jedis jedis) {
				return jedis.hset(encode(key), encode(field), encode(value));
			}
		});
	}
	
	public void mset(Object key, Map<Object, Object> hash) {
		invoke(new Callback<Jedis, String>() {
			@Override
			public String invoke(Jedis jedis) {
				Map<byte[], byte[]> data = new HashMap<byte[], byte[]>();
				for (Entry<Object, Object> entry : hash.entrySet()) 
					data.put(encode(entry.getKey()), encode(entry.getValue()));
				return jedis.hmset(encode(key), data);
			}
		});
	}
	
	/**
	 * 只在 key 指定的哈希集中不存在指定的字段时，设置字段的值。如果 key 指定的哈希集不存在，会创建一个新的哈希集并与 key 关联。如果字段已存在，该操作无效果
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return true - 如果字段是个新的字段，并成功赋值; false - 如果哈希集中已存在该字段，没有操作被执行
	 */
	public boolean setnx(Object key, Object field, Object value) { 
		return 1 == invoke(new Callback<Jedis, Long>() {
			@Override
			public Long invoke(Jedis jedis) {
				return jedis.hsetnx(encode(key), encode(field), encode(value));
			}
		});
	}
}
