package org.rubik.soa.config.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.rubik.bean.core.Assert;
import org.rubik.bean.core.Constants;
import org.rubik.bean.core.enums.Locale;
import org.rubik.bean.core.model.Code;
import org.rubik.bean.core.model.ConfigKey;
import org.rubik.bean.core.model.Criteria;
import org.rubik.bean.core.model.Query;
import org.rubik.soa.config.api.RubikConfigService;
import org.rubik.soa.config.bean.entity.SysConfig;
import org.rubik.soa.config.bean.entity.SysWord;
import org.rubik.soa.config.bean.model.RubikConfigs;
import org.rubik.soa.config.bean.model.RubikWords;
import org.rubik.soa.config.manager.RubikConfigManager;
import org.rubik.util.common.CollectionUtil;
import org.springframework.stereotype.Service;

@Service("rubikConfigService")
public class RubikConfigServiceImpl implements RubikConfigService {
	
	@Resource
	private RubikConfigManager rubikConfigManager;

	@Override
	public RubikConfigs config(Query query) {
		RubikConfigs configs = new RubikConfigs();
		for (SysConfig config : rubikConfigManager.configs(query).values()) 
			configs.put(config.getKey(), config);
		return configs;
	}
	
	@Override
	public SysWord word(String key, Locale locale) {
		return rubikConfigManager.word(key, locale);
	}

	@Override
	public RubikConfigs config(ConfigKey<?>... keys) {
		Set<String> set = new HashSet<String>();
		for (ConfigKey<?> key : keys)
			set.add(key.key());
		if (CollectionUtil.isEmpty(set))
			return new RubikConfigs();
		Query query = new Query().and(Criteria.in("key",set));
		return config(query);
	}

	@Override
	public <VALUE> VALUE config(ConfigKey<VALUE> key) {
		SysConfig config = rubikConfigManager.config(key.key());
		VALUE value = null == config ? key.defaultValue() : Constants.CONVERTER.convert(config.getValue(), key.valueClass());
		return Assert.notNull(value, Code.CONFIG_NOT_EXIST, key.key());
	}

	@Override
	public RubikWords words(Collection<String> keys, Locale locale) {
		Query query = new Query().and(Criteria.eq("locale", locale), Criteria.in("key", keys));
		RubikWords words = new RubikWords();
		for (SysWord word : rubikConfigManager.words(query)) 
			words.put(word.getKey(), word);
		return words;
	}
}
