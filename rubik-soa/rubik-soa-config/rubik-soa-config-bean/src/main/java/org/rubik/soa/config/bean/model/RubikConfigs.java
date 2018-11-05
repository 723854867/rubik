package org.rubik.soa.config.bean.model;

import java.text.MessageFormat;
import java.util.HashMap;

import org.rubik.bean.core.Constants;
import org.rubik.bean.core.model.BizType;
import org.rubik.bean.core.model.ConfigKey;
import org.rubik.soa.config.bean.entity.SysConfig;

public class RubikConfigs extends HashMap<String, SysConfig> {

	private static final long serialVersionUID = 8682543257865645911L;

	public <VALUE> VALUE get(ConfigKey<VALUE> key) {
		SysConfig config = get(key.key());
		return null == config ? key.defaultValue() : Constants.CONVERTER.convert(config.getValue(), key.valueClass());
	}
	
	public <VALUE> VALUE get(ConfigKey<VALUE> key, BizType bizType) {
		SysConfig config = null;
		if (null != bizType)
			config = get(MessageFormat.format(key.key() + "_{0}", String.valueOf(bizType.mark())));
		if (null == config)
			config = get(key.key());
		return null == config ? key.defaultValue() : Constants.CONVERTER.convert(config.getValue(), key.valueClass());
	}
}
