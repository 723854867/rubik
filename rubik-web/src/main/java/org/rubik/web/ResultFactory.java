package org.rubik.web;

import javax.annotation.Resource;

import org.rubik.bean.core.enums.Locale;
import org.rubik.bean.core.model.Code;
import org.rubik.bean.core.model.Result;
import org.rubik.core.Rubik;
import org.rubik.soa.config.api.RubikConfigService;
import org.rubik.soa.config.bean.entity.SysWord;
import org.springframework.stereotype.Component;

@Component
public class ResultFactory {

	@Resource
	private RubikConfigService rubikConfigService;
	
	public Result<Void> ok() {
		return error(Code.OK, Rubik.locale());
	}
	
	public <T> Result<T> ok(T attach) {
		return ok(attach, Rubik.locale());
	}
	
	public Result<Void> error(Code code) {
		return error(code, Rubik.locale());
	}
	
	public Result<?> wrap(Result<?> result) { 
		return wrap(result, Rubik.locale());
	}
	
	public <T> Result<T> ok(T attach, Locale locale) {
		SysWord word = rubikConfigService.word(Code.OK.key(), locale);
		Result<T> result = Result.ok();
		if (null != word)
			result.setDesc(word.getValue());
		result.setAttach(attach);
		return result;
	}
	
	public Result<Void> error(Code code, String message) {
		return error(code, Rubik.locale(), message);
	}
	
	public Result<?> wrap(Result<?> result, Locale locale) { 
		SysWord word = rubikConfigService.word(result.getCode(), locale);
		if (null != word)
			result.setDesc(word.getValue());
		return result;
	}
	
	public Result<Void> error(Code code, Locale locale) {
		SysWord word = rubikConfigService.word(code.key(), locale);
		Result<Void> result = Result.error(code);
		if (null != word)
			result.setDesc(word.getValue());
		return result;
	}
	
	public Result<Void> error(Code code, Locale locale, String message) {
		SysWord word = rubikConfigService.word(code.key(), locale);
		Result<Void> result = Result.error(code);
		if (null != word)
			result.setDesc(word.getValue() + " - [" + message + "]");
		else 
			result.setDesc(message);
		return result;
	}
}
