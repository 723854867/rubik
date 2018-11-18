package org.rubik.soa.authority.bean.enums;

import org.rubik.bean.core.enums.IEnum;

public enum ModuleType implements IEnum<Integer>{
	
	//栏目
	COLUMN(1),
	//按钮操作
	BUTTON(2);

	private Integer mark;
	
	private ModuleType(int mark) {
		this.mark = mark;
	}
	@Override
	public Integer mark() {
		return mark;
	}

}
