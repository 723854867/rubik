package org.rubik.soa.authority.bean.param;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.rubik.bean.core.param.Param;
import org.rubik.soa.authority.bean.enums.ModuleType;
import org.rubik.util.common.StringUtil;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModularAddParam extends Param {

	private static final long serialVersionUID = 2273920592031480392L;

	private String css;
	@NotEmpty
	private String url;
	@NotEmpty
	private String name;
	@Min(0)
	private int priority;
	@NotNull
	private ModuleType type;
	private Integer parent;
	
	@Override
	public void verify() {
		super.verify();
		if (null == css)
			this.css = StringUtil.EMPTY;
	}
}
