package org.rubik.soa.authority.bean.enums;

import org.rubik.bean.core.enums.IEnum;

public enum AuthMappingType implements IEnum<Integer> {
	
	// 模块api映射
	MODULAR_API(1),
	// 角色模块映射
	ROLE_MODULAR(2),
	// 用户角色映射
	USER_ROLE(3);

	private int mark;
	
	private AuthMappingType(int mark) {
		this.mark = mark;
	}

	@Override
	public Integer mark() {
		// TODO Auto-generated method stub
		return mark;
	}
	
}
