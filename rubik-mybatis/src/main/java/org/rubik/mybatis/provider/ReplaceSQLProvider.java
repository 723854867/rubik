package org.rubik.mybatis.provider;

import java.util.List;

import org.apache.ibatis.mapping.MappedStatement;
import org.rubik.mybatis.entity.DBEntity;
import org.rubik.mybatis.entity.DBEntityCol;
import org.rubik.mybatis.extension.DaoAccessor;

public class ReplaceSQLProvider extends SQLProvider<String> {
	
	public ReplaceSQLProvider(Class<?> mapperClass, DaoAccessor daoAccessor) {
		super(mapperClass, daoAccessor);
	}

	@Override
	public String effectiveSQL(MappedStatement ms) {
		DBEntity entity = getEntityTable(ms);
		StringBuilder builder = new StringBuilder("REPLACE INTO ").append(entity.getName()).append("(");
		List<DBEntityCol> columns = entity.columns();
		for (DBEntityCol column : columns)
			builder.append(column.getName()).append(",");
		builder.deleteCharAt(builder.length() - 1).append(") VALUES").append("(");
		for (DBEntityCol column : columns) 
			builder.append("#{").append(column.getEntityField().getName()).append("},");
		builder.deleteCharAt(builder.length() - 1).append(")");
		return builder.toString();
	}
}
