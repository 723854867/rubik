package org.rubik.bean.core.enums;

/**
 * 客户端类型
 * 
 * @author lynn
 */
public enum Client implements IEnum<Integer> {

	// 自身sdk调用
	SDK(1),
	// 其他
	OTHER(2),
	// 浏览器客户端
	BROWSER(4),
	// 原生客户端
	ORIGINAL(8);
	
	private int mark;
	
	private Client(int mark) {
		this.mark = mark;
	}
	
	@Override
	public Integer mark() {
		return mark;
	}
}
