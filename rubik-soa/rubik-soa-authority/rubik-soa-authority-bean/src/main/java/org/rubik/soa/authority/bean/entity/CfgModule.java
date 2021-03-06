package org.rubik.soa.authority.bean.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.rubik.bean.core.Identifiable;
import org.rubik.soa.authority.bean.enums.ModuleType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CfgModule implements Identifiable<Integer> {

	private static final long serialVersionUID = 5158869666833211454L;
	
	@Id
	@GeneratedValue
	private int id;
	private String name;
	private String url;
	private String css;
	private ModuleType type;
	private int priority;
	private int parent;
	private int created;
	private int updated;

	@Override
	public Integer key() {
		return id;
	}

}
