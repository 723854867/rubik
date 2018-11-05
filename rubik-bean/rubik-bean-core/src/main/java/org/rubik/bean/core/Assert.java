package org.rubik.bean.core;

import org.rubik.bean.core.exception.AssertException;
import org.rubik.bean.core.model.Code;

public class Assert {
	
	public static final <T> T notNull(T value) { 
		return notNull(value, Code.SYS_ERR);
	}
	
	public static final <T> T notNull(T value, Code code) { 
		if (null == value)
			throw AssertException.error(code);
		return value;
	}
	
	public static final <T> T notNull(T value, String reason) { 
		return notNull(value, Code.SYS_ERR, reason);
	}
	
	public static final <T> T notNull(T value, Code code, String reason) { 
		if (null == value)
			throw AssertException.error(code, reason);
		return value;
	}

	public static final void isNull(Object value) { 
		isNull(value, Code.SYS_ERR);
	}

	public static final void isNull(Object value, Code code) { 
		if (null != value)
			throw AssertException.error(code);
	}
	
	public static final void isNull(Object value, String message) { 
		isNull(value, Code.SYS_ERR, message);
	}
	
	public static final void isNull(Object value, Code code, String message) { 
		if (null != value)
			throw AssertException.error(Code.SYS_ERR, message);
	}
	
	public static final String hasText(String value) { 
		return hasText(value, Code.SYS_ERR);
	}
	
	public static final String hasText(String value, Code code) { 
		if (!_hasText(value))
			throw AssertException.error(code);
		return value;
	}
	
	public static final String hasText(String value, Code code, String reason) { 
		if (!_hasText(value))
			throw AssertException.error(code, reason);
		return value;
	}

	public static final void isTrue(boolean expression) { 
		isTrue(expression, Code.SYS_ERR);
	}
	
	public static final void isTrue(boolean expression, Code code) { 
		if (!expression)
			throw AssertException.error(code);
	}
	
	public static final void isTrue(boolean expression, String reason) { 
		isTrue(expression, Code.SYS_ERR, reason);
	}
	
	public static final void isTrue(boolean expression, Code code, String reason) { 
		if (!expression)
			throw AssertException.error(code, reason);
	}
	
	private static final boolean _hasText(CharSequence str) {
		if (!_hasLength(str))
			return false;
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i)))
				return true;
		}
		return false;
	}

	private static final boolean _hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}
}
