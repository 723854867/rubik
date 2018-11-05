package org.rubik.util.serializer;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

import org.rubik.bean.core.Constants;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonSerializer implements Serializer {
	
	@Override
	public byte[] serial(Serializable model) {
		return toJson(model).getBytes(Constants.UTF_8);
	}

	@Override
	public <ENTITY extends Serializable> ENTITY deserial(byte[] data, Class<ENTITY> clazz) {
		return fromJson(new String(data, Constants.UTF_8), clazz);
	}
	
	private static final Gson GSON = new Gson();
	private static final Type MAP = new TypeToken<Map<String, String>>() {}.getType();	
	private static final Type TREEMAP = new TypeToken<TreeMap<String, String>>() {}.getType();
	
	public static final String toJson(Object bean) { 
		return GSON.toJson(bean);
	}
	
	public static final String toSortedJson(Object bean) {
		String json = GSON.toJson(bean);
		TreeMap<String, String> map = GSON.fromJson(json, TREEMAP);
		return toJson(map);
	}
	
	public static final Map<String, String> beanToMap(Object bean) {
		return beanToMap(GSON, bean);
	}
	
	public static final Map<String, String> beanToMap(Gson gson, Object bean) {
		String json = gson.toJson(bean);
		return gson.fromJson(json, MAP);
	}
	
	public static final <T> T fromJson(String json, Class<T> clazz) { 
		return GSON.fromJson(json, clazz);
	}
	
	public static final <T> T fromJson(String json, Type typeOfT) { 
		return GSON.fromJson(json, typeOfT);
	}
	
	public static final <T> T mapToBean(Map<String, ?> map, Class<T> clazz) {
		String json = GSON.toJson(map);
		return GSON.fromJson(json, clazz);
	}
}
