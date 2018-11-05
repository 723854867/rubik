package org.rubik.bean.core.param;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LidParam extends Param {

	private static final long serialVersionUID = 4003053943481083846L;

	@Min(1)
	private long id;
}
