package org.rubik.util.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtil {
	
	public static <T> Set<T> emptySet() { 
		return new HashSet<T>();
	}
	
	public static <T> List<T> emptyList() { 
		return new ArrayList<T>();
	}
	
	public static <T, K> Map<T, K> emptyMap() { 
		return new HashMap<T, K>();
	}

	public static boolean isEmpty(Map<?, ?> map) { 
		return null == map || map.isEmpty();
	}
	
	public static boolean isEmpty(Collection<?> collection) { 
		return null == collection || collection.isEmpty();
	}
	
	public static String toString(Collection<?> set, String split) {
		if (isEmpty(set))
			return null;
		StringBuilder builder = new StringBuilder();
		for(Object object : set)
			builder.append(object.toString()).append(split);
		return builder.deleteCharAt(builder.length() - 1).toString();
	}
	
	public static Set<String> toSet(String source, String seperator) {
		if (!StringUtil.hasText(source))
			return emptySet();
		String[] arr = source.split(seperator);
		Set<String> set = new HashSet<String>();
		for (String string : arr) 
			set.add(string);
		return set;
	}
}
