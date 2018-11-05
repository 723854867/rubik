package org.rubik.bean.core.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 服务端返回结果
 * <pre>
 * 如果 signType 不为 null 则 客户端需要根据 signType 对 attach 进行验签
 * </pre>
 * 
 * @author lynn
 * @param <T>
 */
@Getter
@Setter
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 7228944364074589207L;

	private T attach;
	private String code;
	private String desc;
	
	public Result() {}
	
	private Result(Code code) {
		this(code, null);
	}
	
	private Result(Code code, T attach) {
		this.attach = attach;
		this.code = code.key();
	}
	
	public static final <T> Result<T> ok() {
		return new Result<T>(Code.OK);
	}
	
	public static final <T> Result<T> ok(T attach) {
		return new Result<T>(Code.OK, attach);
	}
	
	public static final <T> Result<T> error(Code code) {
		return new Result<T>(code);
	}
}
