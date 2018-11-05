package org.rubik.mybatis.entity;

import javax.persistence.Column;
import javax.persistence.Id;

import org.rubik.bean.core.enums.ColStyle;
import org.rubik.bean.core.enums.DBDialect;
import org.rubik.util.reflect.EntityFactory;
import org.rubik.util.reflect.EntityField;

public class DBEntityFactory extends EntityFactory<DBEntity, DBEntityCol> {

	public DBEntityFactory() {
		this(DBDialect.mysql);
	}
	
	public DBEntityFactory(DBDialect dialect) {
		this.reservedWordWrapper = dialect.keyWordWrapper();
	}
	
	@Override
	protected DBEntityCol processField(DBEntity entity, ColStyle style, EntityField field) {
		DBEntityCol col = super.processField(entity, style, field);
		if (field.isAnnotationPresent(Id.class))
			col.setId(true);
		if (field.isAnnotationPresent(Column.class)) {
			Column column = field.getAnnotation(Column.class);
			col.setUpdatable(column.updatable());
			col.setInsertable(column.insertable());
		}
		return col;
	}
}
