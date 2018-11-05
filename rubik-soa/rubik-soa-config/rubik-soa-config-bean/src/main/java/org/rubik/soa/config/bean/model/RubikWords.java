package org.rubik.soa.config.bean.model;

import java.text.MessageFormat;
import java.util.HashMap;

import org.rubik.bean.core.Assert;
import org.rubik.bean.core.model.BizType;
import org.rubik.bean.core.model.Code;
import org.rubik.soa.config.bean.entity.SysWord;

public class RubikWords extends HashMap<String, SysWord> {

	private static final long serialVersionUID = 8739002686767731745L;

	public String get(String key, BizType bizType) {
		SysWord word = null;
		if (null != bizType) 
			word = get(MessageFormat.format(key + "_{0}", String.valueOf(bizType.mark())));
		if (null == word)
			word = get(key);
		return Assert.notNull(word, Code.WORD_NOT_EXIST, key).getValue();
	}
}
