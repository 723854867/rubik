package org.rubik.soa.config.bean.entity;

import javax.persistence.Id;

import org.rubik.bean.core.Identifiable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysConfig implements Identifiable<String> {

	private static final long serialVersionUID = 1638844933121445893L;

	@Id
	private String key;
	private String desc;
	private int created;
	private int updated;
	private String value;
	// 是否前端可见，如果为false则只能后台可见
	private boolean visible;
	// 是否可编辑
	private boolean editable;
	
	@Override
	public String key() {
		return this.key;
	}
}
