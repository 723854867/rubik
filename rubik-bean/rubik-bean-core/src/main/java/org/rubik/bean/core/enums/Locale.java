package org.rubik.bean.core.enums;

public enum Locale implements IEnum<String> {

	ZH_CN("zh_CN"),
	EN_US("en_US");
	
	private String mark;
	
	private Locale(String mark) {
		this.mark = mark;
	}

	@Override
	public String mark() {
		return this.mark;
	}
}
