package org.rubik.soa.config.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rubik.bean.core.enums.Locale;
import org.rubik.bean.core.model.Criteria;
import org.rubik.bean.core.model.Query;
import org.rubik.soa.config.bean.entity.SysConfig;
import org.rubik.soa.config.bean.entity.SysWord;
import org.rubik.soa.config.mybatis.dao.SysConfigDao;
import org.rubik.soa.config.mybatis.dao.SysWordDao;
import org.springframework.stereotype.Component;

@Component
public class RubikConfigManager {

	@Resource
	private SysWordDao sysWordDao;
	@Resource
	private SysConfigDao sysConfigDao;
	
	public SysConfig config(String key) {
		return sysConfigDao.selectByKey(key);
	}
	
	public List<SysWord> words(Query query) {
		return sysWordDao.queryList(query);
	}
	
	public SysWord word(String key, Locale locale) {
		Query query = new Query().and(Criteria.eq("key", key), Criteria.eq("locale", locale));
		return sysWordDao.queryUnique(query);
	}
	
	public Map<String, SysConfig> configs(Query query) {
		return sysConfigDao.queryMap(query);
	}
}
