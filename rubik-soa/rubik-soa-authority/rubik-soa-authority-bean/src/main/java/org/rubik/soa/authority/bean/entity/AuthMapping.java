package org.rubik.soa.authority.bean.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.rubik.bean.core.Identifiable;
import org.rubik.soa.authority.bean.enums.AuthMappingType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthMapping implements Identifiable<Integer> {
	
	private static final long serialVersionUID = -7116802290438815685L;
	
	@Id
	@GeneratedValue
	private int id;
	private int sid;
	private int tid;
	private AuthMappingType type;
	private int created;

	@Override
	public Integer key() {
		return id;
	}
}
