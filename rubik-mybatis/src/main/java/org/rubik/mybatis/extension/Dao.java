package org.rubik.mybatis.extension;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.rubik.bean.core.Identifiable;
import org.rubik.bean.core.model.Query;
import org.rubik.mybatis.provider.DeleteAllSQLProvider;
import org.rubik.mybatis.provider.DeleteByKeySQLProvider;
import org.rubik.mybatis.provider.DeleteByKeysSQLProvider;
import org.rubik.mybatis.provider.DeleteByQuerySQLProvider;
import org.rubik.mybatis.provider.InsertManySQLProvider;
import org.rubik.mybatis.provider.InsertSQLProvider;
import org.rubik.mybatis.provider.QuerySQLProvider;
import org.rubik.mybatis.provider.ReplaceCollectionSQLProvider;
import org.rubik.mybatis.provider.ReplaceMapSQLProvider;
import org.rubik.mybatis.provider.ReplaceSQLProvider;
import org.rubik.mybatis.provider.SelectAllSQLProvider;
import org.rubik.mybatis.provider.SelectByKeySQLProvider;
import org.rubik.mybatis.provider.SelectByKeysSQLProvider;
import org.rubik.mybatis.provider.UpdateSQLProvider;

public interface Dao<KEY, ENTITY extends Identifiable<KEY>> {

	@InsertProvider(type = InsertSQLProvider.class, method = "dynamicSQL")
	long insert(ENTITY entity);
	
	/**
	 * 该方法支持主键自增又支持已经设置了主键的批量插入，但是不支持混搭(即同时存在有主键和未设置主键的，一旦出现这种情况，entity 中主键值是不可预测的)
	 */
	@InsertProvider(type = InsertManySQLProvider.class, method = "dynamicSQL")
	void insertMany(Collection<ENTITY> entities);
	
	@SelectProvider(type = SelectAllSQLProvider.class, method = "dynamicSQL")
	Map<KEY, ENTITY> selectAll();
	
	@SelectProvider(type = SelectAllSQLProvider.class, method = "dynamicSQL")
	List<ENTITY> selectList();
	
	@SelectProvider(type = SelectByKeySQLProvider.class, method = "dynamicSQL")
	ENTITY selectByKey(KEY key);
	
	@SelectProvider(type = SelectByKeysSQLProvider.class, method = "dynamicSQL")
	Map<KEY, ENTITY> selectByKeys(Collection<KEY> keys);
	
	@SelectProvider(type = QuerySQLProvider.class, method = "dynamicSQL")
	ENTITY queryUnique(Query query);
	
	@SelectProvider(type = QuerySQLProvider.class, method = "dynamicSQL")
	Map<KEY, ENTITY> queryMap(Query query);
	
	@SelectProvider(type = QuerySQLProvider.class, method = "dynamicSQL")
	List<ENTITY> queryList(Query query);
	
	@UpdateProvider(type = UpdateSQLProvider.class, method = "dynamicSQL")
	long update(ENTITY entity);
	
	@InsertProvider(type = ReplaceSQLProvider.class, method = "dynamicSQL")
	long replace(ENTITY entity);
	
	@UpdateProvider(type = ReplaceMapSQLProvider.class, method = "dynamicSQL")
	int replaceMap(@Param("map") Map<KEY, ENTITY> map);
	
	@UpdateProvider(type = ReplaceCollectionSQLProvider.class, method = "dynamicSQL")
	int replaceCollection(Collection<ENTITY> entities);
	
	@DeleteProvider(type = DeleteAllSQLProvider.class, method = "dynamicSQL")
	long deleteAll();
	
	@DeleteProvider(type = DeleteByKeySQLProvider.class, method = "dynamicSQL")
	long deleteByKey(KEY key);
	
	@DeleteProvider(type = DeleteByQuerySQLProvider.class, method = "dynamicSQL")
	long deleteByQuery(Query query);
	
	@DeleteProvider(type = DeleteByKeysSQLProvider.class, method = "dynamicSQL")
	long deleteByKeys(Collection<KEY> keys);
}
