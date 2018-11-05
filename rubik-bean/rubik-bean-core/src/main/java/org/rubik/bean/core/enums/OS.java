package org.rubik.bean.core.enums;

/**
 * 操作系统类型
 * 
 * @author lynn
 */
public enum OS implements IEnum<Integer> {

	// 苹果手机操作系统
	IOS(1),
	// 其他操作系统
	OTHER(2),
	// 苹果笔记本操作系统
	MAC_OS(4),
	// 安卓操作系统
	ANDROID(8),
	// windows操作系统
	WINDOWS(16),
	// windows phone 操作系统
	WINDOWS_PHONE(32);

	private int mark;

	private OS(int mark) {
		this.mark = mark;
	}

	@Override
	public Integer mark() {
		return mark;
	}
}
