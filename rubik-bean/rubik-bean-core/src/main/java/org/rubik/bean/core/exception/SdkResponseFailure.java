package org.rubik.bean.core.exception;

public class SdkResponseFailure extends RuntimeException {

	private static final long serialVersionUID = 9011696017622574471L;
	
	private String desc;
	private String code;
	
	public SdkResponseFailure() {}
	
	public SdkResponseFailure(String code, String desc) {
		super(desc);
		this.desc = desc;
		this.code = code;
	}
	
	public String desc() {
		return desc;
	}
	
	public String code() {
		return code;
	}
}

