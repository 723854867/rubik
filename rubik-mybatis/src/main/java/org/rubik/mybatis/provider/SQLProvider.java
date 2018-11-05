package org.rubik.mybatis.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.rubik.mybatis.entity.DBEntity;
import org.rubik.mybatis.extension.DaoAccessor;
import org.rubik.mybatis.extension.MsUtil;

public abstract class SQLProvider<T> {

	protected Class<?> mapperClass;
	protected DaoAccessor daoAccessor;
	protected Map<String, Class<?>> entityClassMap = new ConcurrentHashMap<String, Class<?>>();
	
	public SQLProvider(Class<?> mapperClass, DaoAccessor daoAccessor) {
		this.mapperClass = mapperClass;
		this.daoAccessor = daoAccessor;
	}
	
	public String dynamicSQL() {
		return "dynamicSQL";
	}
	
	public DBEntity getEntityTable(MappedStatement ms) { 
		Class<?> clazz = MsUtil.getEntityClass(ms, mapperClass);
		return null == clazz ? null : daoAccessor.getEntityFactory().get(clazz);
	}
	
	/**
	 * 设置返回值类型 - 为了让typeHandler在select时有效，改为设置resultMap
	 */
	protected void setResultType(MappedStatement ms, DBEntity entity) {
		List<ResultMap> resultMaps = new ArrayList<ResultMap>();
		resultMaps.add(entity.getResultMap(ms.getConfiguration()));
		MetaObject metaObject = SystemMetaObject.forObject(ms);
		metaObject.setValue("resultMaps", Collections.unmodifiableList(resultMaps));
	}
	
	/**
	 * 真实有效的 sql
	 */
	public abstract T effectiveSQL(MappedStatement ms);
	
	public Class<?> getMapperClass() {
		return mapperClass;
	}
}
