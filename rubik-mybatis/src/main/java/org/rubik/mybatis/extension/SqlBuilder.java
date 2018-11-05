package org.rubik.mybatis.extension;

import java.util.Set;

import org.rubik.mybatis.entity.DBEntity;
import org.rubik.mybatis.entity.DBEntityCol;
import org.rubik.util.common.StringUtil;

public class SqlBuilder {
	
	public static String getBindCache(DBEntityCol column) {
		String property = column.getEntityField().getName();
        StringBuilder sql = new StringBuilder();
        sql.append("<bind name=\"");
        sql.append(property).append("_cache\" ");
        sql.append("value=\"").append(property).append("\"/>");
        return sql.toString();
    }
	
	public static String insertIntoTable(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(tableName);
        sql.append(" ");
        return sql.toString();
    }
	
	public static String insertColumns(DBEntity entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        Set<DBEntityCol> columnList = entity.columns();
        for (DBEntityCol column : columnList) {
            if (!column.isInsertable()) 
                continue;
            sql.append(column.getName() + ",");
        }
        sql.append("</trim>");
        return sql.toString();
    }
	
	public static String getIfCacheNotNull(DBEntityCol column, String contents) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"").append(column.getEntityField().getName()).append("_cache != null\">");
        sql.append(contents);
        sql.append("</if>");
        return sql.toString();
    }
	
	public static String getIfNotNull(DBEntityCol column, String contents, boolean empty) {
        return getIfNotNull(null, column, contents, empty);
    }
	
	public static String getIfNotNull(String entityName, DBEntityCol column, String contents, boolean empty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"");
        if (StringUtil.hasText(entityName))
            sql.append(entityName).append(".");
        sql.append(column.getEntityField().getName()).append(" != null");
        if (empty && column.getEntityField().getJavaType().equals(String.class)) {
            sql.append(" and ");
            if (StringUtil.hasText(entityName))
                sql.append(entityName).append(".");
            sql.append(column.getEntityField().getName()).append(" != '' ");
        }
        sql.append("\">");
        sql.append(contents);
        sql.append("</if>");
        return sql.toString();
    }
	
	public static String getIfCacheIsNull(DBEntityCol column, String contents) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"").append(column.getEntityField().getName()).append("_cache == null\">");
        sql.append(contents);
        sql.append("</if>");
        return sql.toString();
    }
	
	public static String getIfIsNull(DBEntityCol column, String contents, boolean empty) {
        return getIfIsNull(null, column, contents, empty);
    }
	
	public static String getIfIsNull(String entityName, DBEntityCol column, String contents, boolean empty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"");
        if (StringUtil.hasText(entityName))
            sql.append(entityName).append(".");
        sql.append(column.getEntityField().getName()).append(" == null");
        if (empty && column.getEntityField().getJavaType().equals(String.class)) {
            sql.append(" or ");
            if (StringUtil.hasText(entityName))
                sql.append(entityName).append(".");
            sql.append(column.getEntityField().getName()).append(" == '' ");
        }
        sql.append("\">");
        sql.append(contents);
        sql.append("</if>");
        return sql.toString();
    }
	
	public static String selectAllColumns(DBEntity entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("<trim prefix=\"\" suffix=\"\" suffixOverrides=\",\">");
        Set<DBEntityCol> columnList = entity.columns();
        for (DBEntityCol column : columnList) 
            sql.append(column.getName() + ",");
        sql.append("</trim>");
        return sql.toString();
    }
	
	public static String fromTable(DBEntity entity) {
        StringBuilder sql = new StringBuilder();
        sql.append(" FROM ");
        sql.append(entity.getName());
        sql.append(" ");
        return sql.toString();
    }
	
	public static String wherePKColumn(DBEntity entity) {
		StringBuilder sql = new StringBuilder();
        sql.append("<where>");
        DBEntityCol column = entity.PKCol();
        sql.append(column.getColumnEqualsHolder());
        sql.append("</where>");
        return sql.toString();
    }
	
	public static String whereColumnIn(DBEntity entity) {
		StringBuilder sql = new StringBuilder();
        DBEntityCol column = entity.PKCol();
    	sql.append("WHERE ").append(column.getName()).append(" IN");
    	sql.append("<foreach collection=\"collection\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">");
    		sql.append("#{item}");
    	sql.append("</foreach> ");
        return sql.toString();
    }
	
	public static String updateTable(DBEntity entity) {
		StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append(entity.getName());
        sql.append(" ");
        return sql.toString();
    }
	
	public static String updateSetColumns(DBEntity entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("<set>");
        Set<DBEntityCol> columnList = entity.columns();
        for (DBEntityCol column : columnList) {
            if (column.isId() || !column.isUpdatable()) 
            	continue;
            sql.append(column.getColumnEqualsHolder() + ",");
        }
        sql.append("</set>");
        return sql.toString();
    }
	
	public static String deleteFromTable(DBEntity entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append(entity.getName());
        sql.append(" ");
        return sql.toString();
    }
}
