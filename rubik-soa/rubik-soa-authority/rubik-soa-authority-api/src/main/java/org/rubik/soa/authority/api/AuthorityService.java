package org.rubik.soa.authority.api;
/*
 * 权限接口
 */

import java.util.List;

import org.rubik.bean.core.model.Pager;
import org.rubik.bean.core.param.SidParam;
import org.rubik.soa.authority.bean.entity.CfgModule;
import org.rubik.soa.authority.bean.entity.CfgRole;
import org.rubik.soa.authority.bean.param.AuthParam;
import org.rubik.soa.authority.bean.param.ModularAddParam;
import org.rubik.soa.authority.bean.param.ModularModifyParam;
import org.rubik.soa.authority.bean.param.RoleAddParam;
import org.rubik.soa.authority.bean.param.RoleModifyParam;

public interface AuthorityService {
	//添加模块
	int addModule(ModularAddParam param);
	//修改模块
	void modifyModule(ModularModifyParam param);
	//删除模块
	void deleteModule(int id);
	//查询所有模块
	List<CfgModule> cfgModules();
	//查询用户的模块
	List<CfgModule> cfgModules(int uid);
	//添加角色
	int addRole(RoleAddParam param);
	//修改角色
	void modifyRole(RoleModifyParam param);
	//删除角色
	void deleteRole(int id);
	//查询所有角色列表
	List<CfgRole> cfgRoles();
	//查询用户角色
	Pager<CfgRole> userRoles(int id);
	//给角色分配模块
	void modularAuth(AuthParam param);
	//给用户分配角色
	void roleAuth(AuthParam param);
}

