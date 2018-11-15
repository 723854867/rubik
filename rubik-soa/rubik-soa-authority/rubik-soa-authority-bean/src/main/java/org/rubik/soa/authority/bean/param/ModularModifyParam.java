package org.rubik.soa.authority.bean.param;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.rubik.bean.core.param.SidParam;
import org.rubik.soa.authority.bean.enums.ModuleType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModularModifyParam extends SidParam {

	private static final long serialVersionUID = 2388816560144542705L;

	@NotEmpty
	private String url;
	@NotEmpty
	private String name;
	private String css;
	private ModuleType type;
	private Integer parent;
	@Min(0)
	private int priority;
}
