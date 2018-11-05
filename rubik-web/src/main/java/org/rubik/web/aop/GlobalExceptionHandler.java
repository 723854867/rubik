package org.rubik.web.aop;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.rubik.bean.core.exception.AssertException;
import org.rubik.bean.core.exception.SdkResponseFailure;
import org.rubik.bean.core.model.Code;
import org.rubik.bean.core.model.Result;
import org.rubik.util.common.StringUtil;
import org.rubik.web.ResultFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	
	@Resource
	private ResultFactory resultFactory;
	
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public Result<Void> handler(Exception e) {
		if (e instanceof IllegalArgumentException) {
			log.warn("Controller 方法参数绑定失败，请注意是否使用 @RequestParam！", e);
			return resultFactory.error(Code.SYS_ERR);
		} else {
			log.warn("系统错误！", e);
			return resultFactory.error(Code.SYS_ERR, e.getMessage());
		}
	}
	
	@ResponseBody
	@ExceptionHandler(SdkResponseFailure.class)
	public Result<Void> sdkExceptionHandler(SdkResponseFailure e) {
		return resultFactory.error(Code.SDK_INVOKE_FAILURE, e.desc());
	}

	@ResponseBody
	@ExceptionHandler({ AssertException.class })
	public Result<Void> bizExceptionHandler(AssertException e) {
		return StringUtil.hasText(e.reason()) ? resultFactory.error(e.code(), e.reason()) : resultFactory.error(e.code());
	}

	@ResponseBody
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Result<Void> httpRequestMethodNotSupportedHandler(HttpRequestMethodNotSupportedException ex) {
		return resultFactory.error(Code.UNSUPPORT_HTTP_METHOD, ex.getMethod());
	}

	@ResponseBody
	@ExceptionHandler({ HttpMessageNotReadableException.class, MissingServletRequestParameterException.class })
	public Result<Void> httpMessageNotReadableException(Exception ex) {
		return resultFactory.error(Code.PARAM_ERR, ex.toString());
	}

	@ResponseBody
	@ExceptionHandler(BindException.class)
	public Result<Void> bindExceptionHandler(BindException ex) {
		return _validatorError(ex.getBindingResult());
	}

	@ResponseBody
	@ExceptionHandler(ConstraintViolationException.class)
	public Result<Void> constraintViolationExceptionHandler(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> set = ex.getConstraintViolations();
		StringBuilder reason = new StringBuilder("");
		for (ConstraintViolation<?> constraintViolation : set) {
			Path path = constraintViolation.getPropertyPath();
			if (path instanceof PathImpl) 
				reason.append(((PathImpl) path).getLeafNode().getName()).append("-").append(constraintViolation.getMessage()).append(";");
		}
		reason.deleteCharAt(reason.length() - 1);
		return resultFactory.error(Code.PARAM_ERR, reason.toString());
	}

	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Result<Void> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
		return _validatorError(ex.getBindingResult());
	}

	/**
	 * 上传文件超过上限
	 * 
	 * @param ex
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public Result<Void> uploadSizeExceededHandler(MaxUploadSizeExceededException ex) {
		return resultFactory.error(Code.UPLOAD_SIZE_EXCEEDED, String.valueOf(ex.getMaxUploadSize()));
	}

	@ResponseBody
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public Result<Void> unsupportedMediaTypeHandler(HttpMediaTypeNotSupportedException ex) {
		return resultFactory.error(Code.UNSUPPORT_CONTENT_TYPE, ex.getContentType().getType());
	} 

	private Result<Void> _validatorError(BindingResult bindingResult) {
		List<FieldError> errors = bindingResult.getFieldErrors();
		StringBuilder reason = new StringBuilder();
		for (FieldError error : errors)
			reason.append(error.getField()).append("-").append(error.getDefaultMessage()).append(";");
		reason.deleteCharAt(reason.length() - 1);
		return resultFactory.error(Code.PARAM_ERR, reason.toString());
	}
}
