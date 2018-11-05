package org.rubik.redis.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rubik.bean.core.Identifiable;
import org.rubik.redis.RedisUtil;
import org.rubik.redis.caller.HashCaller;
import org.rubik.util.serializer.Serializer;

/**
 * 一个 reids key 存放多个数据，一般是一个hash存放多个对象，hash的key是ENTITY的key，value是对象的序列化数据
 * 
 * @author lynn
 *
 * @param <KEY>
 * @param <ENTITY>
 */
public class OTMRedisDao<KEY, ENTITY extends Identifiable<KEY>> implements RedisDao<KEY, ENTITY> {

	protected byte[] key;
	protected Class<ENTITY> clazz;
	protected HashCaller hashCaller;
	protected Serializer serializer;
	
	@SuppressWarnings("unchecked")
	public OTMRedisDao(String key, Serializer serializer) {
		this.serializer = serializer;
		this.key = RedisUtil.encode(key);
		Type superType = getClass().getGenericSuperclass();   
		Type[] generics = ((ParameterizedType) superType).getActualTypeArguments();  
		this.clazz = (Class<ENTITY>) generics[1];
	}

	@Override
	public boolean add(ENTITY entity) {
		byte[] data = serializer.serial(entity);
		long value = hashCaller.set(key, entity.key(), data);
		return 1 == value;
	}
	
	@Override
	public void add(Collection<ENTITY> entities) { 
		Map<Object, Object> map = new HashMap<Object, Object>();
		for (ENTITY entity : entities) 
			map.put(entity.key(), serializer.serial(entity));
		hashCaller.mset(key, map);
	}
	
	@Override
	public ENTITY getByKey(KEY key) {
		byte[] data = hashCaller.get(this.key, key);
		return null == data ? null : serializer.deserial(data, clazz);
	}
	
	@Override
	public Map<KEY, ENTITY> getByKeys(Collection<KEY> keys) {
		List<byte[]> list = hashCaller.mget(key, keys.toArray());
		Map<KEY, ENTITY> map = new HashMap<KEY, ENTITY>();
		for (byte[] data : list) {
			if (null == data)
				continue;
			ENTITY entity = serializer.deserial(data, clazz);
			map.put(entity.key(), entity);
		}
		return map;
	}
	
	@Override
	public long deleteByKey(KEY key) {
		return hashCaller.del(this.key, key);
	}
	
	@Override
	public long deleteByKeys(Collection<KEY> keys) {
		return hashCaller.del(this.key, keys.toArray());
	}
	
	public void setHashCaller(HashCaller hashCaller) {
		this.hashCaller = hashCaller;
	}
}
