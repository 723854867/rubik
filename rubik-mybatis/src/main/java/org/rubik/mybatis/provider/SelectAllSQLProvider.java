package org.rubik.mybatis.provider;

import org.apache.ibatis.mapping.MappedStatement;
import org.rubik.mybatis.entity.DBEntity;
import org.rubik.mybatis.extension.DaoAccessor;
import org.rubik.mybatis.extension.SqlBuilder;

public class SelectAllSQLProvider extends SQLProvider<String> {

	public SelectAllSQLProvider(Class<?> mapperClass, DaoAccessor daoAccessor) {
		super(mapperClass, daoAccessor);
	}

	@Override
	public String effectiveSQL(MappedStatement ms) {
		DBEntity entity = getEntityTable(ms);
        setResultType(ms, entity);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlBuilder.selectAllColumns(entity));
        sql.append(SqlBuilder.fromTable(entity));
        return sql.toString();
	}
}
