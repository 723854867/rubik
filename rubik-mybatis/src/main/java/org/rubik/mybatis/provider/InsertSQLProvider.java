package org.rubik.mybatis.provider;

import java.util.List;

import javax.persistence.GeneratedValue;

import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.rubik.mybatis.entity.DBEntity;
import org.rubik.mybatis.entity.DBEntityCol;
import org.rubik.mybatis.extension.DaoAccessor;
import org.rubik.mybatis.extension.SqlBuilder;

public class InsertSQLProvider extends SQLProvider<String> {

	public InsertSQLProvider(Class<?> mapperClass, DaoAccessor daoAccessor) {
		super(mapperClass, daoAccessor);
	}

	@Override
	public String effectiveSQL(MappedStatement ms) {
        DBEntity entity = getEntityTable(ms);
        StringBuilder sql = new StringBuilder();
        List<DBEntityCol> columns = entity.columns();
        Boolean hasIdentityKey = false;
        for (DBEntityCol column : columns) {
            if (!column.isInsertable()) 
                continue;
            if (column.getEntityField().isAnnotationPresent(GeneratedValue.class)) {		// 自增处理
            	 sql.append(SqlBuilder.getBindCache(column));
                 if (hasIdentityKey)
                     throw new RuntimeException(ms.getId() + "对应的实体类" + entity.getClazz().getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
                 _autoIncrement(ms, column);
                 hasIdentityKey = true;
            }
        }
        sql.append(SqlBuilder.insertIntoTable(entity.getName()));
        sql.append(SqlBuilder.insertColumns(entity));
        sql.append("<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">");
        for (DBEntityCol column : columns) {
            if (!column.isInsertable())
                continue;
            if (column.getEntityField().isAnnotationPresent(GeneratedValue.class))
                sql.append(SqlBuilder.getIfCacheNotNull(column, column.getColumnHolder(null, "_cache", ",")));
            else 
                sql.append(SqlBuilder.getIfNotNull(column, column.getColumnHolder(null, null, ","), false));
            if (column.getEntityField().isAnnotationPresent(GeneratedValue.class)) 
                sql.append(SqlBuilder.getIfCacheIsNull(column, column.getColumnHolder() + ","));
            else 
                sql.append(SqlBuilder.getIfIsNull(column, column.getColumnHolder(null, null, ","), false));
        }
        sql.append("</trim>");
        return sql.toString();
	}
	
	private void _autoIncrement(MappedStatement ms, DBEntityCol column) {
		String keyId = ms.getId() + "!selectKey";
		if (ms.getConfiguration().hasKeyGenerator(keyId))
			return;
		KeyGenerator keyGenerator = new Jdbc3KeyGenerator();
		try {
			MetaObject msObject = SystemMetaObject.forObject(ms);
			msObject.setValue("keyGenerator", keyGenerator);
			msObject.setValue("keyProperties", new String[] {column.getEntity().PKCol().getEntityField().getName()});
			msObject.setValue("keyColumns", new String[] {column.getEntity().PKCol().getName()});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
