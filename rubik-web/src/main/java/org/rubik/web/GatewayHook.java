package org.rubik.web;

import org.rubik.bean.core.model.RequestMeta;

public interface GatewayHook {

	void postInvoke(RequestMeta meta);
	
	void afterInvoke(RequestMeta meta, Object[] params, Object response);
}
