package org.rubik.mybatis.provider;

import org.apache.ibatis.mapping.MappedStatement;
import org.rubik.mybatis.entity.DBEntity;
import org.rubik.mybatis.extension.DaoAccessor;
import org.rubik.mybatis.extension.SqlBuilder;

public class DeleteByQuerySQLProvider extends SQLProvider<String> {

	public DeleteByQuerySQLProvider(Class<?> mapperClass, DaoAccessor daoAccessor) {
		super(mapperClass, daoAccessor);
	}

	@Override
	public String effectiveSQL(MappedStatement ms) {
		DBEntity table = getEntityTable(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlBuilder.deleteFromTable(table));
        sql.append("<if test=\"null != criterias and !criterias.isEmpty\">");
			sql.append("<where>"
					+ "<foreach item=\"item\" collection=\"criterias\" separator=\"\">"
					+ "<choose>"
						+ "<when test=\"item.comparison==1\">"
							+ "<![CDATA[${andor} " + daoAccessor.reservedWordWrap("${item.col}") + "<#{item.value}]]> "
						+ "</when>"
						+ "<when test=\"item.comparison==2\">"
							+ "<![CDATA[${andor} " + daoAccessor.reservedWordWrap("${item.col}") + "<=#{item.value}]]> "
						+ "</when>"
						+ "<when test=\"item.comparison==3\">"
							+ "<![CDATA[${andor} " + daoAccessor.reservedWordWrap("${item.col}") + ">#{item.value}]]> "
						+ "</when>"
						+ "<when test=\"item.comparison==4\">"
							+ "<![CDATA[${andor} " + daoAccessor.reservedWordWrap("${item.col}") + ">=#{item.value}]]> "
						+ "</when>"
						+ "<when test=\"item.comparison==5\">"
							+ "${andor} " + daoAccessor.reservedWordWrap("${item.col}") + "=#{item.value} "
						+ "</when>"
						+ "<when test=\"item.comparison==6\">"
							+ "${andor} " + daoAccessor.reservedWordWrap("${item.col}") + "!=#{item.value} "
						+ "</when>"
						+ "<when test=\"item.comparison==7\">"
							+ "${andor} " + daoAccessor.reservedWordWrap("${item.col}") + " LIKE concat(concat('%',#{item.value}),'%') "
						+ "</when>"
						+ "<when test=\"item.comparison==8\">"
							+ "${andor} " + daoAccessor.reservedWordWrap("${item.col}") + " IN ("
								+ "<foreach item=\"item1\" collection=\"item.value\" separator=\",\">"
									+ "#{item1}"
								+ "</foreach>"
							+ ") "
						+ "</when>"
						+ "<when test=\"item.comparison==9\">"
							+ "${andor} " + daoAccessor.reservedWordWrap("${item.col}") + " NOT IN ("
								+ "<foreach item=\"item1\" collection=\"item.value\" separator=\",\">"
									+ "#{item1}"
								+ "</foreach>"
							+ ") "
						+ "</when>"
						+ "<when test=\"item.comparison==10\">"
							+ "${andor} " + daoAccessor.reservedWordWrap("${item.col}") + " IS NULL "
						+ "</when>"
						+ "<when test=\"item.comparison==11\">"
							+ "${andor} " + daoAccessor.reservedWordWrap("${item.col}") + " IS NOT NULL "
						+ "</when>"
						+ "<otherwise>"
							+ "${andor} " + daoAccessor.reservedWordWrap("${item.col}") + "BETWEEN #{item.value[0]} AND #{item.value[1]} "
						+ "</otherwise>"
					+ "</choose>" 
				+ "</foreach>"
			+ "</where>");
		sql.append("</if>");
        return sql.toString();
	}
}
