package org.rubik.redis.dao;

import java.util.Collection;
import java.util.Map;

import org.rubik.bean.core.Identifiable;

public interface RedisDao<KEY, ENTITY extends Identifiable<KEY>> {

	/**
	 * 如果已经存在 entity 则返回true，否则返回 false
	 * 
	 * @param entity
	 * @return
	 */
	boolean add(ENTITY entity);
	
	/**
	 * 一次添加多个数据
	 * 
	 * @param entities
	 */
	void add(Collection<ENTITY> entities);
	
	ENTITY getByKey(KEY key);
	
	Map<KEY, ENTITY> getByKeys(Collection<KEY> keys);
	
	long deleteByKey(KEY key);
	
	long deleteByKeys(Collection<KEY> keys);
}
