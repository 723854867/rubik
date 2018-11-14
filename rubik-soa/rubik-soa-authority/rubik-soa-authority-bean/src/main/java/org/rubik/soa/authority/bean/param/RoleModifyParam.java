package org.rubik.soa.authority.bean.param;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.rubik.bean.core.param.SidParam;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoleModifyParam extends SidParam {

	private static final long serialVersionUID = -2697177843839151725L;

	@NotEmpty
	private String name;
	
	@NotNull
	private Set<Integer> ids;
}
