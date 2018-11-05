package org.rubik.soa.config.bean.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.rubik.bean.core.Identifiable;
import org.rubik.bean.core.enums.Locale;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysWord implements Identifiable<Integer> {

	private static final long serialVersionUID = -9190013795109961854L;
	
	@Id
	@GeneratedValue
	private int id;
	private String key;
	private int created;
	private int updated;
	private String value;
	private Locale locale;

	@Override
	public Integer key() {
		return this.id;
	}
}
