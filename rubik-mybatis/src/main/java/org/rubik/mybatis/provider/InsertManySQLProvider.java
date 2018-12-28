package org.rubik.mybatis.provider;

import java.util.List;

import javax.persistence.GeneratedValue;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.rubik.mybatis.entity.DBEntity;
import org.rubik.mybatis.entity.DBEntityCol;
import org.rubik.mybatis.extension.DaoAccessor;
import org.rubik.mybatis.extension.MultipleJdbc3KeyGenerator;
import org.rubik.mybatis.extension.SqlBuilder;

public class InsertManySQLProvider extends SQLProvider<String> {

	public InsertManySQLProvider(Class<?> mapperClass, DaoAccessor daoAccessor) {
		super(mapperClass, daoAccessor);
	}

	@Override
	public String effectiveSQL(MappedStatement ms) {
		DBEntity table = getEntityTable(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append(SqlBuilder.insertIntoTable(table.getName()));
        sql.append(SqlBuilder.insertColumns(table));
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"collection\" item=\"record\" separator=\",\" >");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        List<DBEntityCol> columnList = table.columns();
        for (DBEntityCol column : columnList) {
            if (column.isInsertable()) 
                sql.append(column.getColumnHolder("record") + ",");
        }
        sql.append("</trim>");
        sql.append("</foreach>");
        // 主键自增处理
        if (table.PKCol().getEntityField().isAnnotationPresent(GeneratedValue.class)) {
        	  MetaObject msObject = SystemMetaObject.forObject(ms);
              msObject.setValue("keyGenerator", new MultipleJdbc3KeyGenerator());
        }
        return sql.toString();
	}
}
