package org.rubik.soa.authority.mybatis.dao;

import java.util.List;

import org.rubik.mybatis.extension.Dao;
import org.rubik.soa.authority.bean.entity.CfgModule;

public interface CfgModuleDao extends Dao<Integer, CfgModule>{

	List<CfgModule> selectList(int uid);

}
