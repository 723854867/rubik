package org.rubik.bean.core.model;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.rubik.bean.core.Assert;

public class ConfigKey<VALUE> implements Serializable {

	private static final long serialVersionUID = -2522983369861093232L;
	
	private static final Map<String, ConfigKey<?>> keys = new ConcurrentHashMap<String, ConfigKey<?>>();

	private String key;
	private Class<VALUE> clazz;
	private VALUE defaultValue;
	
	private ConfigKey(String key, Class<VALUE> clazz) {
		this.key = key;
		this.clazz = clazz;
	}
	
	private ConfigKey(String key, VALUE defaultValue, Class<VALUE> clazz) {
		this.key = key;
		this.clazz = clazz;
		this.defaultValue = defaultValue;
	}
	
	public VALUE defaultValue() {
		return defaultValue;
	}
	
	public Class<VALUE> valueClass() { 
		return clazz;
	}
	
	public String key(Object... arguments) {
		return MessageFormat.format(key, arguments);
	}
	
	@SuppressWarnings("unchecked")
	public static final <VALUE> ConfigKey<VALUE> find(String key) { 
		return Assert.notNull((ConfigKey<VALUE>) keys.get(key), "config key [" + key + "] not exist!");
	}
	
	public static final <VALUE> ConfigKey<VALUE> create(String key, Class<VALUE> clazz) {  
		ConfigKey<VALUE> configKey = new ConfigKey<VALUE>(key, clazz);
		Assert.isNull(keys.putIfAbsent(key, configKey), "config [" + key + "] has duplicated!");
		return configKey;
	}
	
	public static final <VALUE> ConfigKey<VALUE> create(String key, VALUE value, Class<VALUE> clazz) {  
		ConfigKey<VALUE> configKey = new ConfigKey<VALUE>(key, value, clazz);
		Assert.isNull(keys.putIfAbsent(key, configKey), "config [" + key + "] has duplicated!");
		return configKey;
	}
}
