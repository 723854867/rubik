package org.rubik.soa.authority;

import org.rubik.soa.authority.bean.entity.AuthMapping;
import org.rubik.soa.authority.bean.entity.CfgModule;
import org.rubik.soa.authority.bean.entity.CfgRole;
import org.rubik.soa.authority.bean.enums.AuthMappingType;
import org.rubik.soa.authority.bean.param.ModularAddParam;
import org.rubik.util.common.DateUtil;

public class EntityGenerator {

	public static final CfgModule newCfgModular(ModularAddParam param, CfgModule parent) {
		CfgModule instance = new CfgModule();
		instance.setCss(param.getCss());
		instance.setUrl(param.getUrl());
		instance.setName(param.getName());
		instance.setPriority(param.getPriority());
		instance.setType(param.getType());
		instance.setParent(null == parent ? 0 : parent.getId());
		int time = DateUtil.current();
		instance.setCreated(time);
		instance.setUpdated(time);
		return instance;
	}
	
	public static final CfgRole newCfgRole(String name) {
		CfgRole instance = new CfgRole();
		instance.setName(name);
		int time = DateUtil.current();
		instance.setCreated(time);
		instance.setUpdated(time);
		return instance;
	}
	
	public static final AuthMapping newAuthMapping(AuthMappingType type, Integer sid, Integer tid) {
		AuthMapping instance = new AuthMapping();
		instance.setSid(sid);
		instance.setTid(tid);
		instance.setType(type);
		instance.setCreated(DateUtil.current());
		return instance;
	}
}
