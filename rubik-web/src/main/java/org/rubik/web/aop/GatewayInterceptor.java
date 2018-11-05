package org.rubik.web.aop;

import java.io.IOException;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.rubik.bean.core.model.MultiListMap;
import org.rubik.bean.core.model.RequestMeta;
import org.rubik.bean.core.model.Result;
import org.rubik.bean.core.model.WrapResult;
import org.rubik.bean.core.param.Param;
import org.rubik.util.common.DateUtil;
import org.rubik.web.GatewayHook;
import org.rubik.web.RequestContext;
import org.rubik.web.ResultFactory;
import org.rubik.web.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class GatewayInterceptor {
	
	@Autowired(required = false)
	private GatewayHook gatewayHook;
	@Resource
	private ResultFactory resultFactory;
	
	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void pointcut() {}

	@Around("pointcut()")
	public Object controllerAround(ProceedingJoinPoint point) throws Throwable {
		RequestMeta meta = _requestMeta(point);
		Object response = null;
		try {
			if (null != gatewayHook)
				gatewayHook.postInvoke(meta);
			Object result = point.proceed();
			if (null != result) {
				if (result instanceof WrapResult)
					response = ((WrapResult) result).getAttach();
				else if (!(result instanceof Result<?>)) 
					response = resultFactory.ok(result);
				else
					response = resultFactory.wrap((Result<?>) result);
			} else
				response = resultFactory.ok();
			return response;
		} finally {
			try {
				if (null != gatewayHook)
					gatewayHook.afterInvoke(meta, point.getArgs(), response);
			} catch (Exception e) {
				log.warn("gateway hook after invoke failure!", e);
			}
			RequestContext.dispose();
		}
	}
	
	private final RequestMeta _requestMeta(ProceedingJoinPoint point) throws IOException {
		HttpServletRequest request = WebUtil.request();
		RequestMeta meta = new RequestMeta();
		meta.setIp(WebUtil.getIpAddress(request));
		meta.setTimestamp(DateUtil.current());
		meta.setType(request.getMethod());
		meta.setPath(request.getServletPath());
		meta.setQuery(request.getQueryString());
		String method = point.getTarget().getClass().getName() + "." + point.getSignature().getName();
		meta.setApiMethod(method);
		Enumeration<String> enumeration = request.getHeaderNames();
		MultiListMap<String, String> headers = new MultiListMap<String, String>();
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			Enumeration<String> temp = request.getHeaders(key);
			while (temp.hasMoreElements()) 
				headers.add(key.toLowerCase(), temp.nextElement());
		}
		meta.setHeaders(headers);
		
		// 参数处理：所有 param 的子类都设置 requestMeta
		Object[] params = point.getArgs();
		for (Object temp : params) {
			if (temp instanceof Param) {
				Param param = (Param) temp;
				param.setMeta(meta);
				// 参数基本验证
				param.verify();
			}
		}
		return meta;
	}
}
