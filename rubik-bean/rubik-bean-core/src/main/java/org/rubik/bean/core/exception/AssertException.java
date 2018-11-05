package org.rubik.bean.core.exception;

import org.rubik.bean.core.model.Code;

public class AssertException extends RuntimeException {

	private static final long serialVersionUID = -563299440176820701L;

	private Code code;
	private String reason;
	
	public AssertException() {
		this(Code.SYS_ERR);
	}
	
	public AssertException(Code code) {
		this.code = code;
	}
	
	public AssertException(Code code, String reason) {
		this.code = code;
		this.reason = reason;
	}
	
	public AssertException(Code code, Throwable cause) {
		super(cause);
		this.code = code;
	}
	
	public Code code() {
		return code;
	}
	
	public String reason() {
		return reason;
	}
	
	public static final AssertException error(Code code) {
		return new AssertException(code);
	}
	
	public static final AssertException error(Code code, String reason) {
		return new AssertException(code, reason);
	}
	
	public static final AssertException error(Code code, Throwable cause) {
		return new AssertException(code, cause);
	}
}
