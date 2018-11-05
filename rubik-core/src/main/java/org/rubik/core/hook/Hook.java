package org.rubik.core.hook;

public interface Hook {

	/**
	 * 初始化钩子
	 * 
	 * @throws Exception
	 */
	void invoke() throws Exception;
	
	/**
	 * 越小越先初始化，因此如果初始化钩子有先后顺序则需要重写该方法
	 * 
	 * @return
	 */
	default int priority() {
		return 0;
	}
}
