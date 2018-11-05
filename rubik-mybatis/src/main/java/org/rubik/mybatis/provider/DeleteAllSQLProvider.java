package org.rubik.mybatis.provider;

import org.apache.ibatis.mapping.MappedStatement;
import org.rubik.mybatis.entity.DBEntity;
import org.rubik.mybatis.extension.DaoAccessor;
import org.rubik.mybatis.extension.SqlBuilder;

public class DeleteAllSQLProvider extends SQLProvider<String> {

	public DeleteAllSQLProvider(Class<?> mapperClass, DaoAccessor daoAccessor) {
		super(mapperClass, daoAccessor);
	}

	@Override
	public String effectiveSQL(MappedStatement ms) {
		DBEntity entity = getEntityTable(ms);
		return SqlBuilder.deleteFromTable(entity);
	}
}
