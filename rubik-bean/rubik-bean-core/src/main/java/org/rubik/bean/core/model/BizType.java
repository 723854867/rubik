package org.rubik.bean.core.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.rubik.bean.core.Assert;

public class BizType implements Serializable {

	private static final long serialVersionUID = 1235351480136169288L;

	private static final Map<Integer, BizType> types = new ConcurrentHashMap<Integer, BizType>();
	
	private int mark;
	
	private BizType(int mark) {
		this.mark = mark;
	}
	
	public int mark() {
		return mark;
	}
	
	public static final BizType find(int mark) {  
		return Assert.notNull(types.get(mark), "biz type [" + mark + "] not exist!");
	}
	
	public static final BizType create(int mark) {  
		BizType type = new BizType(mark);
		if (null != types.putIfAbsent(mark, type))
			throw new RuntimeException("biz type [" + mark + "] has duplicated!");
		return type;
	}
}
