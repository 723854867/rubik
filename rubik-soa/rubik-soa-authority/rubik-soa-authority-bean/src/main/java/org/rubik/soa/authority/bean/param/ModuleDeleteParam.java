package org.rubik.soa.authority.bean.param;

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.rubik.bean.core.param.Param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuleDeleteParam extends Param{

	private static final long serialVersionUID = -1525146750817802487L;
	
	@NotNull
	private Set<Integer> ids;

}
