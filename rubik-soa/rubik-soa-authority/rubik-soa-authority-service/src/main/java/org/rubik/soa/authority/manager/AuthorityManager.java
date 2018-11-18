package org.rubik.soa.authority.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.rubik.bean.core.Assert;
import org.rubik.bean.core.model.Criteria;
import org.rubik.bean.core.model.Query;
import org.rubik.soa.authority.EntityGenerator;
import org.rubik.soa.authority.bean.AuthCode;
import org.rubik.soa.authority.bean.entity.AuthMapping;
import org.rubik.soa.authority.bean.entity.CfgModule;
import org.rubik.soa.authority.bean.entity.CfgRole;
import org.rubik.soa.authority.bean.enums.AuthMappingType;
import org.rubik.soa.authority.bean.param.AuthParam;
import org.rubik.soa.authority.bean.param.ModularAddParam;
import org.rubik.soa.authority.bean.param.ModularModifyParam;
import org.rubik.soa.authority.bean.param.RoleAddParam;
import org.rubik.soa.authority.bean.param.RoleModifyParam;
import org.rubik.soa.authority.mybatis.dao.AuthMappingDao;
import org.rubik.soa.authority.mybatis.dao.CfgModuleDao;
import org.rubik.soa.authority.mybatis.dao.CfgRoleDao;
import org.rubik.util.common.CollectionUtil;
import org.rubik.util.common.DateUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
		if (null != param.getCss())
			modular.setCss(param.getCss());
		modular.setUrl(param.getUrl());
		modular.setName(param.getName());
		modular.setPriority(param.getPriority());
		modular.setParent(param.getParent());
		modular.setType(param.getType());
		modular.setUpdated(DateUtil.current());
		cfgModuleDao.update(modular);
	}

	public void deleteModule(Set<Integer> ids) {
		cfgModuleDao.deleteByKeys(ids);
		authMappingDao.deleteModules(ids);
	}


	public List<CfgModule> cfgModules() {
		Query query = new Query().orderByAsc("priority");
		return cfgModuleDao.queryList(query);
	}

	//获取用户的模块权限
	public List<CfgModule> cfgModules(int uid) {
		Set<Integer> set = authMappingDao.tids(AuthMappingType.USER_ROLE.mark(), uid);
		if (CollectionUtil.isEmpty(set))
			return CollectionUtil.emptyList();
		List<AuthMapping> l = authMappingDao.queryList(new Query().and(Criteria.eq("type", AuthMappingType.ROLE_MODULAR.mark()),Criteria.in("sid", set)));
		if (CollectionUtil.isEmpty(l))
			return CollectionUtil.emptyList();
		set.clear();
		l.forEach(item -> set.add(item.getTid()));
		return cfgModuleDao.queryList(new Query().and(Criteria.in("id", set)).orderByAsc("priority"));
	}
	//获取角色对应的权限
	public List<CfgModule> cfgRoleModules(int roleId) {
		List<AuthMapping> l = authMappingDao.queryList(new Query().and(Criteria.eq("type", AuthMappingType.ROLE_MODULAR.mark()),Criteria.eq("sid", roleId)));
		if (CollectionUtil.isEmpty(l))
			return CollectionUtil.emptyList();
		Set<Integer> set = new HashSet<Integer>();
		l.forEach(item -> set.add(item.getTid()));
		return cfgModuleDao.queryList(new Query().and(Criteria.in("id", set)).orderByAsc("priority"));
	}

	@Transactional
	public int addRole(RoleAddParam param) {
		CfgRole role = EntityGenerator.newCfgRole(param.getName());
		cfgRoleDao.insert(role);
		if(!param.getIds().isEmpty()) {
			List<AuthMapping> list = new ArrayList<AuthMapping>();
			param.getIds().forEach(item->{list.add(EntityGenerator.newAuthMapping(AuthMappingType.ROLE_MODULAR, role.getId(), item));});
			authMappingDao.insertMany(list);
		}
		return role.getId();
	}

	@Transactional
	public void modifyRole(RoleModifyParam param) {
		CfgRole role = cfgRoleDao.selectByKey(Integer.valueOf(param.getId()));
		Assert.notNull(role,AuthCode.ROLE_NOT_EXIST);
		role.setName(param.getName());
		role.setUpdated(DateUtil.current());
		cfgRoleDao.update(role);
		if(!param.getIds().isEmpty()) {
			List<AuthMapping> list = new ArrayList<AuthMapping>();
			param.getIds().forEach(item->{list.add(EntityGenerator.newAuthMapping(AuthMappingType.ROLE_MODULAR, role.getId(), item));});
			authMappingDao.deleteRoleMordulByRoleId(role.getId());
			authMappingDao.insertMany(list);
		}
	}

	public void deleteRole(int id) {
		cfgRoleDao.deleteByKey(id);
		authMappingDao.deleteRoles(id);
	}

	public List<CfgRole> cfgRoles(Query query) {
		return cfgRoleDao.queryList(query);
	}
	
	public List<CfgRole> userRoles(int uid) {
		Set<Integer> set = authMappingDao.tids(AuthMappingType.USER_ROLE.mark(), uid);
		return CollectionUtil.isEmpty(set) ? CollectionUtil.emptyList() : 
			cfgRoleDao.queryList(new Query().and(Criteria.in("id", set)));

	}

	//给用户分配角色
	@Transactional
	public void userAuth(AuthParam param) {
		if(param.getTid().isEmpty()) {
			authMappingDao.deleteByUserId(param.getSid());
		} else {
			Map<Integer, CfgRole> role = cfgRoleDao.selectByKeys(param.getTid());
			Assert.isTrue(role.size() == param.getTid().size(),AuthCode.MODULAR_NOT_EXIST);
			authMappingDao.deleteByUserId(param.getSid());
			List<AuthMapping> list = new ArrayList<>();
			role.values().forEach(item->list.add(EntityGenerator.newAuthMapping(AuthMappingType.USER_ROLE, param.getSid(), item.getId())));
			authMappingDao.insertMany(list);
		}
	}


}
