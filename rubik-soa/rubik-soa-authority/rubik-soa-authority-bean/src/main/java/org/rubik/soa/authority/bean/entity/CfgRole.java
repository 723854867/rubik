package org.rubik.soa.authority.bean.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.rubik.bean.core.Identifiable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CfgRole implements Identifiable<Integer> {

	private static final long serialVersionUID = 2209686938044108245L;
	
	@Id
	@GeneratedValue
	private int id;
	private String name;
	private int created;
	private int updated;

	@Override
	public Integer key() {
		return this.id;
	}
}
