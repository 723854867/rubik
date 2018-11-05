package org.rubik.soa.config.api;

import java.util.Collection;

import org.rubik.bean.core.enums.Locale;
import org.rubik.bean.core.model.ConfigKey;
import org.rubik.bean.core.model.Query;
import org.rubik.soa.config.bean.entity.SysWord;
import org.rubik.soa.config.bean.model.RubikConfigs;
import org.rubik.soa.config.bean.model.RubikWords;

public interface RubikConfigService {

	RubikConfigs config(Query query);
	
	SysWord word(String key, Locale locale);
	
	RubikConfigs config(ConfigKey<?>... keys);

	<VALUE> VALUE config(ConfigKey<VALUE> key);
	
	RubikWords words(Collection<String> keys, Locale locale);
}
