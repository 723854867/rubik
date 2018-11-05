package org.rubik.util.reflect;

import java.beans.Transient;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Table;

import org.rubik.bean.core.anno.NameStyle;
import org.rubik.bean.core.enums.ColStyle;
import org.rubik.util.common.StringUtil;

@SuppressWarnings("unchecked")
public abstract class EntityFactory<ENTITY extends Entity<ENTITY, COLUMN>, COLUMN extends EntityCol<ENTITY, COLUMN>> {

	protected Class<ENTITY> entityClazz;
	protected Class<COLUMN> columnClazz;
	// 关键字包装字符
	protected String reservedWordWrapper;
	// 字段映射风格：默认驼峰
	protected ColStyle style = ColStyle.camel2underline;
	protected Map<Class<?>, ENTITY> entities = new ConcurrentHashMap<Class<?>, ENTITY>();
	
	public EntityFactory() {
		Type superType = getClass().getGenericSuperclass();   
		Type[] generics = ((ParameterizedType) superType).getActualTypeArguments();  
		this.entityClazz = (Class<ENTITY>) generics[0];
		this.columnClazz = (Class<COLUMN>) generics[1];
	}
	
	public ENTITY get(Class<?> clazz) {
		ENTITY entity = entities.get(clazz);
		if (null == entity) 
			return init(clazz);
		return entity;
	}
	
	private synchronized ENTITY init(Class<?> clazz) {
		ENTITY entity = entities.get(clazz);
		if (null != entity)
			return entity;
		entity = instance(clazz);
		if (clazz.isAnnotationPresent(NameStyle.class)) { 
			NameStyle nameStyle = clazz.getAnnotation(NameStyle.class);
			style = nameStyle.value();
		}
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = clazz.getAnnotation(Table.class);
			entity.setName(StringUtil.hasText(table.name()) ? table.name() : style.convert(clazz.getSimpleName()));
		} else
			entity.setName(style.convert(clazz.getSimpleName()));
		List<EntityField> fields = FieldHelper.getFields(clazz);
		for (EntityField field : fields) {
			if (field.isAnnotationPresent(Transient.class))
				continue;
			entity.addCol(processField(entity, style, field));
		}
		entities.put(clazz, entity);
		return entity;
	}
	
	protected COLUMN processField(ENTITY entity, ColStyle style, EntityField field) {
		COLUMN col = column(entity, field);
		String columnName = null;
		if (field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			columnName = column.name();
		}
		if (!StringUtil.hasText(columnName))
			columnName = field.getName();
		columnName = style.convert(field.getName());
		col.setName(reservedWordWrap(columnName));
		return col;
	}
	
	protected ENTITY instance(Class<?> clazz) {
		try {
			Constructor<?> constructor = entityClazz.getConstructor(new Class[] {Class.class});
			return (ENTITY) constructor.newInstance(clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected COLUMN column(ENTITY entity, EntityField field) {
		try {
			Constructor<?> constructor = columnClazz.getConstructor(new Class[] {entity.getClass(), field.getClass()});
			return (COLUMN) constructor.newInstance(entity, field);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setStyle(ColStyle style) {
		this.style = style;
	}
	
	public Set<COLUMN> getColumns(Class<?> clazz) { 
		ENTITY entity = get(clazz);
		return entity.columns();
	}
	
	public String reservedWordWrap(String value) {
		return MessageFormat.format(reservedWordWrapper, value);
	}
}
