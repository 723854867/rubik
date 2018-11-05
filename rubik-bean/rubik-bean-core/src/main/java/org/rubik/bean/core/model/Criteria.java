package org.rubik.bean.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;

@Getter
public class Criteria implements Serializable {
	
	private static final long serialVersionUID = 7167607188227959520L;
	
	public static final int LT					= 0x01;
	public static final int LTE					= 0x02;
	public static final int GT					= 0x03;
	public static final int GTE					= 0x04;
	public static final int EQ					= 0x05;
	public static final int NEQ					= 0x06;
	public static final int LIKE				= 0x07;
	public static final int IN					= 0x08;
	public static final int NIN					= 0x09;
	public static final int ISNULL				= 0x0A;
	public static final int NOTNULL				= 0x0B;
	public static final int BETWEEN				= 0x0C;
	public static final int ALL					= 0x0D;
	public static final int OR					= 0x0E;
	public static final int AND					= 0x0F;
	
	private String col;
	private Object value;
	private int comparison;
	
	public Criteria() {}
	
	public Criteria(String col, int comparison) {
		this(col, comparison, null);
	}
	
	public Criteria(String col, int comparison, Object value) {
		this.col = col;
		this.value = value;
		this.comparison = comparison;
	}
	
	public static final Criteria isnull(String col) {
		return new Criteria(col, ISNULL);
	}
	
	public static final Criteria notnull(String col) {
		return new Criteria(col, NOTNULL);
	}
	
	public static final Criteria eq(String col, Object value) {
		return new Criteria(col, EQ, value);
	}
	
	public static final Criteria neq(String col, Object value) {
		return new Criteria(col, NEQ, value);
	}
	
	public static final Criteria lt(String col, Object value) {
		return new Criteria(col, LT, value);
	}
	
	public static final Criteria lte(String col, Object value) {
		return new Criteria(col, LTE, value);
	}
	
	public static final Criteria gt(String col, Object value) {
		return new Criteria(col, GT, value);
	}
	
	public static final Criteria gte(String col, Object value) {
		return new Criteria(col, GTE, value);
	}
	
	public static final Criteria like(String col, Object value) {
		return new Criteria(col, LIKE, value);
	}
	
	public static final Criteria in(String col, Object... values) {
		Set<Object> set = new HashSet<Object>();
		for (Object value : values)
			set.add(value);
		return new Criteria(col, IN, set);
	}
	
	public static final Criteria in(String col, Collection<?> values) {
		return new Criteria(col, IN, values);
	}
	
	public static final Criteria nin(String col, Object... values) {
		List<Object> list = new ArrayList<Object>();
		for (Object value : values)
			list.add(value);
		return new Criteria(col, NIN, list);
	}
	
	public static final Criteria nin(String col, Collection<?> values) {
		return new Criteria(col, NIN, values);
	}
	
	public static final Criteria between(String col, Object lower, Object upper) {
		Object[] arr = new Object[] {lower, upper};
		return new Criteria(col, BETWEEN, arr);
	}
}
