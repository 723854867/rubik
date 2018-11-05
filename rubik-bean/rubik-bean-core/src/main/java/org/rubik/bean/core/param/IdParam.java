package org.rubik.bean.core.param;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdParam extends Param {

	private static final long serialVersionUID = 4594589314458475339L;

	@Min(1)
	private int id;
}
