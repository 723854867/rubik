package org.rubik.mybatis.provider;

import java.util.List;

import org.apache.ibatis.mapping.MappedStatement;
import org.rubik.mybatis.entity.DBEntity;
import org.rubik.mybatis.entity.DBEntityCol;
import org.rubik.mybatis.extension.DaoAccessor;

public class ReplaceCollectionSQLProvider extends SQLProvider<String> {
	
	private static final String foreachSuffix = "</foreach>";
	private static final String foreachPrefix = "<foreach item=\"item\" collection=\"collection\" separator=\",\">";

	public ReplaceCollectionSQLProvider(Class<?> mapperClass, DaoAccessor daoAccessor) {
		super(mapperClass, daoAccessor);
	}

	@Override
	public String effectiveSQL(MappedStatement ms) {
        DBEntity entity = getEntityTable(ms);
		StringBuilder builder = new StringBuilder("REPLACE INTO ").append(entity.getName()).append("(");
		List<DBEntityCol> columns = entity.columns();
		for (DBEntityCol column : columns)
			builder.append(column.getName()).append(",");
		builder.deleteCharAt(builder.length() - 1).append(") VALUES").append(foreachPrefix).append("(");
		for (DBEntityCol column : columns) 
			builder.append("#{item.").append(column.getEntityField().getName()).append("},");
		builder.deleteCharAt(builder.length() - 1).append(")").append(foreachSuffix);
		return builder.toString();
	}
}
