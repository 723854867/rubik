package org.rubik.mybatis.entity;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;
import org.rubik.util.common.CollectionUtil;
import org.rubik.util.reflect.Entity;

public class DBEntity extends Entity<DBEntity, DBEntityCol> {
	
	public static final Pattern DELIMITER = Pattern.compile("^[`\\[\"]?(.*?)[`\\]\"]?$");
	
	// resultMap对象
	private ResultMap resultMap;
	
	public DBEntity(Class<?> clazz) {
		super(clazz);
	}
	
	public DBEntityCol PKCol() {
		for (DBEntityCol col : cols.values()) {
			if (col.isId())
				return col;
		}
		return null;
	}
	
	/**
	 * 生成当前实体的resultMap对象
	 */
	public ResultMap getResultMap(Configuration configuration) {
		if (null != this.resultMap)
			return this.resultMap;
		if (CollectionUtil.isEmpty(cols))
			return null;
		List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
		for (DBEntityCol col : cols.values()) {
			String column = col.getName();
			// 去掉可能存在的分隔符
			Matcher matcher = DELIMITER.matcher(column);
			if (matcher.find())
				column = matcher.group(1);
			ResultMapping.Builder builder = new ResultMapping.Builder(configuration, col.getEntityField().getName(), column, col.getEntityField().getJavaType());
			if (col.getJdbcType() != null) 
				builder.jdbcType(col.getJdbcType());
			if (col.getTypeHandler() != null) {
				try {
					builder.typeHandler(getInstance(col.getEntityField().getJavaType(), col.getTypeHandler()));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			List<ResultFlag> flags = new ArrayList<ResultFlag>();
			if (col.isId())
				flags.add(ResultFlag.ID);
			builder.flags(flags);
			resultMappings.add(builder.build());
		}
		ResultMap.Builder builder = new ResultMap.Builder(configuration, "BaseMapperResultMap", this.clazz, resultMappings, true);
		this.resultMap = builder.build();
		return this.resultMap;
	}
	
	/**
	 * 实例化TypeHandler
	 * 
	 * @param javaTypeClass
	 * @param typeHandlerClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> TypeHandler<T> getInstance(Class<?> javaTypeClass, Class<?> typeHandlerClass) {
		if (javaTypeClass != null) {
			try {
				Constructor<?> c = typeHandlerClass.getConstructor(Class.class);
				return (TypeHandler<T>) c.newInstance(javaTypeClass);
			} catch (NoSuchMethodException ignored) {
				// ignored
			} catch (Exception e) {
				throw new TypeException("Failed invoking constructor for handler " + typeHandlerClass, e);
			}
		}
		try {
			Constructor<?> c = typeHandlerClass.getConstructor();
			return (TypeHandler<T>) c.newInstance();
		} catch (Exception e) {
			throw new TypeException("Unable to find a usable constructor for " + typeHandlerClass, e);
		}
	}
}
