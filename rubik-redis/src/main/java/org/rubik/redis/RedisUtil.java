package org.rubik.redis;

import org.rubik.bean.core.Constants;

public class RedisUtil {

	public static final byte[] encode(Object value) {
		return (value instanceof byte[]) ? (byte[]) value : _encode(value.toString());
	}
	
	public static final byte[][] encode(Object... params) {
		byte[][] buffer = new byte[params.length][];
		for (int i = 0, len = params.length; i < len; i++) {
			if (params[i] instanceof byte[])
				buffer[i] = (byte[]) params[i];
			else
				buffer[i] = _encode(params[i].toString());
		}
		return buffer;
	}
	
	private static final byte[] _encode(String value) {
		return value.getBytes(Constants.UTF_8);
	}
}
