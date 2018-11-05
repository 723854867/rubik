package org.rubik.mybatis.entity;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.rubik.util.common.StringUtil;
import org.rubik.util.reflect.EntityCol;
import org.rubik.util.reflect.EntityField;

public class DBEntityCol extends EntityCol<DBEntity, DBEntityCol> {
	
	private boolean id = false;
	private JdbcType jdbcType;
	private boolean updatable = true;
	private boolean insertable = true;
	private Class<? extends TypeHandler<?>> typeHandler;

	public DBEntityCol(DBEntity entity, EntityField field) {
		super(entity, field);
	}

	public boolean isId() {
		return id;
	}
	
	public void setId(boolean id) {
		this.id = id;
	}
	
	public JdbcType getJdbcType() {
		return jdbcType;
	}
	
	public boolean isUpdatable() {
		return updatable;
	}
	
	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}
	
	public boolean isInsertable() {
		return insertable;
	}
	
	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}
	
	public Class<? extends TypeHandler<?>> getTypeHandler() {
		return typeHandler;
	}
	
	/**
	 * 返回格式如:#{age,jdbcType=NUMERIC,typeHandler=MyTypeHandler}
	 *
	 * @return
	 */
	public String getColumnHolder() {
		return getColumnHolder(null);
	}
	
	/**
	 * 返回格式如:colum = #{age,jdbcType=NUMERIC,typeHandler=MyTypeHandler}
	 *
	 * @return
	 */
	public String getColumnEqualsHolder() {
		return getColumnEqualsHolder(null);
	}
	
	/**
	 * 返回格式如:colum = #{age,jdbcType=NUMERIC,typeHandler=MyTypeHandler}
	 *
	 * @param entityName
	 * @return
	 */
	public String getColumnEqualsHolder(String entityName) {
		return name + " = " + getColumnHolder(entityName);
	}

	/**
	 * 返回格式如:#{entityName.age,jdbcType=NUMERIC,typeHandler=MyTypeHandler}
	 *
	 * @param entityName
	 * @return
	 */
	public String getColumnHolder(String entityName) {
		return getColumnHolder(entityName, null);
	}

	/**
	 * 返回格式如:#{entityName.age+suffix,jdbcType=NUMERIC,typeHandler=MyTypeHandler}
	 *
	 * @param entityName
	 * @param suffix
	 * @return
	 */
	public String getColumnHolder(String entityName, String suffix) {
		return getColumnHolder(entityName, null, null);
	}

	/**
	 * 返回格式如:#{entityName.age+suffix,jdbcType=NUMERIC,typeHandler=MyTypeHandler},
	 *
	 * @param entityName
	 * @param suffix
	 * @return
	 */
	public String getColumnHolderWithComma(String entityName, String suffix) {
		return getColumnHolder(entityName, suffix, ",");
	}
	
	public String getColumnHolder(String entityName, String suffix, String separator) {
		StringBuffer sb = new StringBuffer("#{");
		if (StringUtil.hasText(entityName)) {
			sb.append(entityName);
			sb.append(".");
		}
		sb.append(this.entityField.getName());
		if (StringUtil.hasText(suffix)) 
			sb.append(suffix);
		if (this.jdbcType != null) {
			sb.append(",jdbcType=");
			sb.append(this.jdbcType.toString());
		} else if (this.typeHandler != null) {
			sb.append(",typeHandler=");
			sb.append(this.typeHandler.getCanonicalName());
		} else if (!this.entityField.getJavaType().isArray()) {// 当类型为数组时，不设置javaType#103
			sb.append(",javaType=");
			sb.append(this.entityField.getJavaType().getCanonicalName());
		}
		sb.append("}");
		if (StringUtil.hasText(separator))
			sb.append(separator);
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DBEntityCol that = (DBEntityCol) o;
		if (id != that.id)
			return false;
		if (entity != null ? !entity.equals(that.entity) : that.entity != null)
			return false;
		if (entityField != null ? !entityField.equals(that.entityField) : that.entityField != null)
			return false;
		if (name != null ? !name.equals(that.name) : that.name != null)
			return false;
		if (jdbcType != that.jdbcType)
			return false;
		return !(typeHandler != null ? !typeHandler.equals(that.typeHandler) : that.typeHandler != null);
	}

	@Override
	public int hashCode() {
		int result = entity != null ? entity.hashCode() : 0;
		result = 31 * result + (id ? 1 : 0);
		result = 31 * result + (jdbcType != null ? jdbcType.hashCode() : 0);
		result = 31 * result + (entityField != null ? entityField.hashCode() : 0);
		result = 31 * result + (typeHandler != null ? typeHandler.hashCode() : 0);
		return result;
	}
}
