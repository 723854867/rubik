package org.rubik.soa.authority.bean.param;

import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.rubik.bean.core.param.Param;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthParam extends Param {

	private static final long serialVersionUID = -7110802930846787502L;

	@Min(1)
	private long sid;
	@NotNull
	private Set<Integer> tid;

}
