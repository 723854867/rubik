package org.rubik.soa.authority.bean.param;

import java.util.Set;

import javax.validation.constraints.NotEmpty;

import org.rubik.bean.core.param.Param;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoleAddParam extends Param {

	private static final long serialVersionUID = -2697177843839151725L;

	@NotEmpty
	private String name;
	@NotEmpty
	private Set<Integer> ids;
}
