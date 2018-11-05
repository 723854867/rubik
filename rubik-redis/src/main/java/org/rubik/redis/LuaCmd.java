package org.rubik.redis;

public enum LuaCmd implements ILuaCmd {
	
	// 相等则删除
	DEL_IF_EQUALS(1),
	// 范围设置bitmap
	SET_BIT_RANGE(1),
	// 接口限流
	GATEWAY_RATE_LIMIT(1),
	// 获取验证码
	CAPTCHA_STORAGE(2),
	CAPTCHA_VERIFY(0) {
		@Override
		public int keyCount() {
			throw new UnsupportedOperationException("lua CAPTCHA_VERIFY key is variable!");
		}
	},
	
	MULTILOCK(0),
	
	RELEASE_LOCK(0) {
		@Override
		public int keyCount() {
			throw new UnsupportedOperationException("lua RELEASE_LOCK key is variable!");
		}
	};
	private int keyCount;

	private LuaCmd(int keyCount) {
		this.keyCount = keyCount;
	}
	
	@Override
	public String key() {
		return name();
	}
	
	@Override
	public int keyCount() {
		return keyCount;
	}
}
