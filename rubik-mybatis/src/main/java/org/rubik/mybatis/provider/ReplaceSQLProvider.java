package org.rubik.mybatis.provider;

import org.apache.ibatis.mapping.MappedStatement;
import org.rubik.mybatis.extension.DaoAccessor;

public class ReplaceSQLProvider extends SQLProvider<String> {

	public ReplaceSQLProvider(Class<?> mapperClass, DaoAccessor daoAccessor) {
		super(mapperClass, daoAccessor);
	}

	@Override
	public String effectiveSQL(MappedStatement ms) {
		// TODO Auto-generated method stub
		return null;
	}
}
