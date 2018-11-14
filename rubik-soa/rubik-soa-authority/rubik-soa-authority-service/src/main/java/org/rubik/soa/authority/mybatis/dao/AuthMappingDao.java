package org.rubik.soa.authority.mybatis.dao;

import org.rubik.mybatis.extension.Dao;
import org.rubik.soa.authority.bean.entity.AuthMapping;

public interface AuthMappingDao extends Dao<Integer, AuthMapping>{

	void deleteModules(int id);

}
