package org.rubik.bean.core.enums;

/**
 * 设备类型
 * 
 * @author lynn
 */
public enum Device implements IEnum<Integer> {

	// 其他
	OTHER(1),
	// 平板
	TABLET(2),
	// 手机
	MOBILE(3),
	// 电脑
	COMPUTER(4);
	
	private int mark;
	
	private Device(int mark) {
		this.mark = mark;
	}
	
	@Override
	public Integer mark() {
		return mark;
	}
}
