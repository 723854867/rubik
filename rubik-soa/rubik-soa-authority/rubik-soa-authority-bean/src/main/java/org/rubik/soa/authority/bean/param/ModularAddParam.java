package org.rubik.soa.authority.bean.param;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.rubik.bean.core.param.SidParam;
import org.rubik.util.common.StringUtil;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModularAddParam extends SidParam {

	private static final long serialVersionUID = 2273920592031480392L;

	private String icon;
	@NotEmpty
	private String url;
	@NotEmpty
	private String name;
	@Min(0)
	private int priority;
	private Integer parent;
	
	@Override
	public void verify() {
		super.verify();
		if (null == icon)
			this.icon = StringUtil.EMPTY;
	}
}
