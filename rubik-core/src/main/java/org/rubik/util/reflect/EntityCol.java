package org.rubik.util.reflect;

public class EntityCol<ENTITY extends Entity<ENTITY, COLUMN>, COLUMN extends EntityCol<ENTITY, COLUMN>> {

	// 列名
	protected String name;
	// 对应实体类
	protected ENTITY entity;
	// 对应实体类字段
	protected EntityField entityField;
	
	public EntityCol(ENTITY entity, EntityField field) {
		this.entity = entity;
		this.entityField = field;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ENTITY getEntity() {
		return entity;
	}
	
	public void setEntity(ENTITY entity) {
		this.entity = entity;
	}
	
	public EntityField getEntityField() {
		return entityField;
	}
	
	public void setEntityField(EntityField entityField) {
		this.entityField = entityField;
	}
}
