package org.rubik.mybatis.extension;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.mapping.MappedStatement;

public class MsUtil {
	
	private static Map<String, Class<?>> entityClassMap = new ConcurrentHashMap<String, Class<?>>();
	
	public static Class<?> getEntityClass(MappedStatement ms, Class<?> mapperClass) {
		String msId = ms.getId();
		if (entityClassMap.containsKey(msId))
			return entityClassMap.get(msId);
		else {
			Class<?> clazz = MsUtil.getMapperClass(msId);
			Type[] types = clazz.getGenericInterfaces();
			for (Type type : types) {
				if (type instanceof ParameterizedType) {
					ParameterizedType t = (ParameterizedType) type;
					if (t.getRawType() == mapperClass || mapperClass.isAssignableFrom((Class<?>) t.getRawType())) {
						Class<?> returnType = (Class<?>) t.getActualTypeArguments()[1];
						entityClassMap.put(msId, returnType);
						return returnType;
					}
				}
			}
		}
		throw new RuntimeException("无法获取 " + msId + " 方法的泛型信息!");
	}
	
	/**
	 * 根据msId获取接口类
	 *
	 * @param msId
	 * @return
	 */
	public static Class<?> getMapperClass(String msId) {
		if (msId.indexOf(".") == -1)
			throw new RuntimeException("当前MappedStatement的id=" + msId + ",不符合MappedStatement的规则!");
		String mapperClassStr = msId.substring(0, msId.lastIndexOf("."));
		ClassLoader[] classLoader = getClassLoaders();
		Class<?> mapperClass = null;
		for (ClassLoader cl : classLoader) {
			if (null != cl) {
				try {
					mapperClass = Class.forName(mapperClassStr, true, cl);
					if (mapperClass != null)
						break;
				} catch (ClassNotFoundException e) {
					// we'll ignore this until all class loaders fail to locate the class
				}
			}
		}
		if (mapperClass == null)
			throw new RuntimeException("class loaders failed to locate the class " + mapperClassStr);
		return mapperClass;
	}

	private static ClassLoader[] getClassLoaders() {
		return new ClassLoader[] { Thread.currentThread().getContextClassLoader(), MsUtil.class.getClassLoader() };
	}

	/**
	 * 获取执行的方法名
	 */
	public static String getMethodName(MappedStatement ms) {
		return getMethodName(ms.getId());
	}

	/**
	 * 获取执行的方法名
	 */
	public static String getMethodName(String msId) {
		return msId.substring(msId.lastIndexOf(".") + 1);
	}
}
