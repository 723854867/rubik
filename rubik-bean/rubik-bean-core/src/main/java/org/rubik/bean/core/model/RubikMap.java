package org.rubik.bean.core.model;

import java.util.HashMap;

import org.rubik.bean.core.Constants;

public class RubikMap extends HashMap<String, String> {

	private static final long serialVersionUID = 443955228775954727L;
	
	public <VALUE> VALUE get(String key, Class<VALUE> clazz) {
		String value = get(key);
		return null == value ? null : Constants.CONVERTER.convert(value, clazz);
	}
}
