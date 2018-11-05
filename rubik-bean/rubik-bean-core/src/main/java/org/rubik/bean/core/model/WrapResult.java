package org.rubik.bean.core.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WrapResult implements Serializable {

	private static final long serialVersionUID = 7052315875444292245L;

	private Object attach;
	
	public WrapResult() {}
	
	public WrapResult(Object attach) {
		this.attach = attach;
	}
}
