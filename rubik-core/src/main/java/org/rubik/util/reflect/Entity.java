package org.rubik.util.reflect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Entity<ENTITY extends Entity<ENTITY, COLUMN>, COLUMN extends EntityCol<ENTITY, COLUMN>> {

	protected String name;
	protected Class<?> clazz;
	protected Map<String, COLUMN> cols = new HashMap<String, COLUMN>();
	
	public Entity(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}
	
	public void addCol(COLUMN col) {
		this.cols.put(col.getEntityField().getName(), col);
	}
	
	public Set<COLUMN> columns() {
		return new HashSet<COLUMN>(cols.values());
	}
}
