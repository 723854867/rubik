package org.rubik.web;

import java.util.HashMap;

public class RequestContext {

	private static final ThreadLocal<HashMap<String, Object>> CONTEXT = new ThreadLocal<HashMap<String, Object>>();
	
	@SuppressWarnings("unchecked")
	public static final <T> T get(String key) {
		HashMap<String, Object> map = CONTEXT.get();
		if (null == map)
			return null;
		Object value = map.get(key);
		return null == value ? null : (T) value;
	}
	
	public static final void put(String key, Object value) { 
		HashMap<String, Object> map = CONTEXT.get();
		if (null == map)
			map = new HashMap<String, Object>();
		CONTEXT.set(map);
		map.put(key, value);
	}
	
	public static final void dispose() {
		CONTEXT.remove();
	}
}
