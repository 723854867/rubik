package org.rubik.mybatis.provider;

import org.apache.ibatis.mapping.MappedStatement;
import org.rubik.mybatis.entity.DBEntity;
import org.rubik.mybatis.extension.DaoAccessor;
import org.rubik.mybatis.extension.SqlBuilder;

public class DeleteByKeysSQLProvider extends SQLProvider<String> {

	public DeleteByKeysSQLProvider(Class<?> mapperClass, DaoAccessor daoAccessor) {
		super(mapperClass, daoAccessor);
	}

	@Override
	public String effectiveSQL(MappedStatement ms) {
		DBEntity entity = getEntityTable(ms);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlBuilder.deleteFromTable(entity));
		sql.append(SqlBuilder.whereColumnIn(entity));
		return sql.toString();
	}
}
