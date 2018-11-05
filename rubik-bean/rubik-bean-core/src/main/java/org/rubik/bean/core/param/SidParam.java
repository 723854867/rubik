package org.rubik.bean.core.param;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SidParam extends Param {

	private static final long serialVersionUID = 5560710089110700871L;

	@NotEmpty
	private String id;
}
