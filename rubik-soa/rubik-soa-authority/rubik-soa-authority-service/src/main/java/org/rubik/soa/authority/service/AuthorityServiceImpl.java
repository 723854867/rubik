package org.rubik.soa.authority.service;

import java.util.List;

import javax.annotation.Resource;

import org.rubik.bean.core.model.Pager;
import org.rubik.soa.authority.api.AuthorityService;
import org.rubik.soa.authority.bean.entity.CfgModule;
import org.rubik.soa.authority.bean.entity.CfgRole;
import org.rubik.soa.authority.bean.param.AuthParam;
import org.rubik.soa.authority.bean.param.ModularAddParam;
import org.rubik.soa.authority.bean.param.ModularModifyParam;
import org.rubik.soa.authority.bean.param.RoleAddParam;
import org.rubik.soa.authority.bean.param.RoleModifyParam;
import org.rubik.soa.authority.manager.AuthorityManager;

public class AuthorityServiceImpl implements AuthorityService{

	@Resource
	AuthorityManager authorityManager;
	
	@Override
	public int addModule(ModularAddParam param) {
		return authorityManager.addModule(param);
	}

	@Override
	public void modifyModule(ModularModifyParam param) {
		authorityManager.modifyModule(param);
		
	}

	@Override
	public void deleteModule(int id) {
		authorityManager.deleteModule(id);
		
	}

	@Override
	public List<CfgModule> cfgModules() {
		return authorityManager.cfgModules();
	}

	@Override
	public List<CfgModule> cfgModules(int uid) {
		return authorityManager.cfgModules(uid);
	}

	@Override
	public int addRole(RoleAddParam param) {
		return authorityManager.addRole(param);
	}

	@Override
	public void modifyRole(RoleModifyParam param) {
		authorityManager.modifyRole(param);
		
	}

	@Override
	public void deleteRole(int id) {
		authorityManager.deleteRole(id);
	}

	@Override
	public List<CfgRole> cfgRoles() {
		return authorityManager.cfgRoles();
	}
	
	@Override
	public Pager<CfgRole> userRoles(int id) {
		return authorityManager.userRoles(id);
	}

	@Override
	public void modularAuth(AuthParam param) {
		authorityManager.modularAuth(param);
	}

	@Override
	public void roleAuth(AuthParam param) {
		authorityManager.roleAuth(param);
	}

}
