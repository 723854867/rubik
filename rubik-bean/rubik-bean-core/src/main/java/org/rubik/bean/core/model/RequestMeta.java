package org.rubik.bean.core.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 请求元数据
 * 
 * @author lynn
 */
@Getter
@Setter
public class RequestMeta implements Serializable {

	private static final long serialVersionUID = -1624110602560363089L;

	// 请求ip
	private String ip;
	// 请求方法类型：GET、POST等
	private String type;
	// 请求路劲
	private String path;
	// url查询参数
	private String query;
	// 请求时间
	private int timestamp;
	// 请求接口描述
	private String apiDesc;
	// 请求类方法：类全名.方法名
	private String apiMethod;
	// 请求者
	private Requestor requestor;
	// 请求头
	private MultiListMap<String, String> headers;
	
	public Integer getIntHeader(String key) {
		List<String> list = headers.get(key.toLowerCase());
		if(null == list || list.isEmpty())
			return null;
		return Integer.valueOf(list.iterator().next());
	}
	
	public Long getLongHeader(String key) {
		List<String> list = headers.get(key.toLowerCase());
		if(null == list || list.isEmpty())
			return null;
		return Long.valueOf(list.iterator().next());
	}
	
	public String getStringHeader(String key) {
		List<String> list = headers.get(key.toLowerCase());
		if(null == list || list.isEmpty())
			return null;
		return list.iterator().next();
	}
}
