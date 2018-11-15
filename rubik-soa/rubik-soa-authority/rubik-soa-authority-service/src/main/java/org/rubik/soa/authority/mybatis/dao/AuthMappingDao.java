package org.rubik.soa.authority.mybatis.dao;

import java.util.Collection;
import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.rubik.mybatis.extension.Dao;
import org.rubik.soa.authority.bean.entity.AuthMapping;

public interface AuthMappingDao extends Dao<Integer, AuthMapping>{

	void deleteModules(Collection<Integer> modulars);

	@Select("SELECT tid FROM auth_mapping WHERE `type`=#{type} AND sid=#{sid}")
	Set<Integer> tids(@Param("type") int type, @Param("sid") long sid);
	
	@Delete("DELETE FROM auth_mapping WHERE (`type`=3 AND tid=#{roleId}) OR (`type`=2 AND sid=#{roleId})")
	void deleteRoles(int roleId);

	@Delete("delete from auth_mapping where `type`=3 and sid=#{sid}")
	void deleteByUserId(long sid);

//	@Delete("delete from auth_mapping where `type`=2 and sid=#{sid}")
//	void deleteByRoleId(Integer sid);

	@Delete("delete from auth_mapping where `type`=2 and sid=#{roleId}")
	void deleteRoleMordulByRoleId(int roleId);

}
