package org.rubik.soa.authority.manager;

import java.util.List;

import javax.annotation.Resource;

import org.rubik.bean.core.Assert;
import org.rubik.bean.core.model.Pager;
import org.rubik.soa.authority.EntityGenerator;
import org.rubik.soa.authority.bean.AuthCode;
import org.rubik.soa.authority.bean.entity.CfgModule;
import org.rubik.soa.authority.bean.entity.CfgRole;
import org.rubik.soa.authority.bean.param.AuthParam;
import org.rubik.soa.authority.bean.param.ModularAddParam;
import org.rubik.soa.authority.bean.param.ModularModifyParam;
import org.rubik.soa.authority.bean.param.RoleAddParam;
import org.rubik.soa.authority.bean.param.RoleModifyParam;
import org.rubik.soa.authority.mybatis.dao.AuthMappingDao;
import org.rubik.soa.authority.mybatis.dao.CfgModuleDao;
import org.rubik.soa.authority.mybatis.dao.CfgRoleDao;
import org.rubik.util.common.DateUtil;
import org.springframework.stereotype.Component;

@Component
public class AuthorityManager {

	@Resource
	private CfgRoleDao cfgRoleDao;
	@Resource
	private CfgModuleDao cfgModuleDao;
	@Resource
	private AuthMappingDao authMappingDao;
	
	public int addModule(ModularAddParam param) {
		CfgModule parent = param.getParent()==0 ? null : cfgModuleDao.selectByKey(param.getParent());
		if(param.getParent()!=0) {
			Assert.notNull(parent,AuthCode.MODULAR_NOT_EXIST);
		}
		CfgModule module = EntityGenerator.newCfgModular(param, parent);
		cfgModuleDao.insert(module);
		return module.getId();
	}

	public void modifyModule(ModularModifyParam param) {
		CfgModule modular = cfgModuleDao.selectByKey(Integer.valueOf(param.getId()));
		Assert.notNull(modular,AuthCode.MODULAR_NOT_EXIST);
		if (null != param.getIcon())
			modular.setIcon(param.getIcon());
		modular.setUrl(param.getUrl());
		modular.setName(param.getName());
		modular.setPriority(param.getPriority());
		modular.setParent(param.getParent());
		modular.setUpdated(DateUtil.current());
		cfgModuleDao.update(modular);
	}

	public void deleteModule(int id) {
		cfgModuleDao.deleteByKey(id);
		authMappingDao.deleteModules(id);
	}


	public List<CfgModule> cfgModules() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<CfgModule> cfgModules(int uid) {
		// TODO Auto-generated method stub
		return null;
	}

	public int addRole(RoleAddParam param) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void modifyRole(RoleModifyParam param) {
		// TODO Auto-generated method stub
		
	}

	public void deleteRole(int id) {
		// TODO Auto-generated method stub
		
	}

	public List<CfgRole> cfgRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	public void modularAuth(AuthParam param) {
		// TODO Auto-generated method stub
	}

	public void roleAuth(AuthParam param) {
		// TODO Auto-generated method stub
		
	}

	public Pager<CfgRole> userRoles(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
