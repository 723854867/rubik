package org.rubik.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.rubik.bean.core.Constants;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class ConfigLoader {
	
	private static final Map<String, Long> modified = new ConcurrentHashMap<String, Long>();
	private static final PathMatchingResourcePatternResolver RESOLVER = new PathMatchingResourcePatternResolver();
	
	public static final Resource[] getResources(String locationPattern) {
		try {
			return RESOLVER.getResources(locationPattern);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 全部加载
	 * 
	 * @param path
	 * @return
	 */
	public static final ConfigMap load(String path) {
		ConfigMap map = new ConfigMap();
		try {
			Resource[] resources = RESOLVER.getResources(path);
			for (Resource resource : resources) {
				InputStream in = resource.getInputStream();
				Properties properties = new Properties();
				properties.load(new InputStreamReader(in, Constants.UTF_8));
				properties.entrySet().forEach(item -> map.put(item.getKey().toString().trim(), item.getValue()));
				in.close();
				if (resource.isFile())
					modified.put(resource.getFile().getCanonicalPath(), resource.lastModified());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return map;
	}
	
	/**
	 * 只加载修改过的文件
	 * 
	 * @param path
	 * @return
	 */
	public static final ConfigMap reload(String path) {
		ConfigMap map = new ConfigMap();
		try {
			Resource[] resources = RESOLVER.getResources(path);
			for (Resource resource : resources) {
				Long lastModified = modified.get(resource.getFile().getCanonicalPath());
				if (null != lastModified) {			
					// 如果没变化则不需要重新加载
					if (resource.lastModified() <= lastModified)
						continue;
				}
				InputStream in = resource.getInputStream();
				Properties properties = new Properties();
				properties.load(new InputStreamReader(in, Constants.UTF_8));
				properties.entrySet().forEach(item -> map.put(item.getKey().toString().trim(), item.getValue()));
				in.close();
				if (resource.isFile())
					modified.put(resource.getFile().getCanonicalPath(), resource.lastModified());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return map;
	}
}
