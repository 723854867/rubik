package org.rubik.bean.core.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pager<T> implements Serializable {

	private static final long serialVersionUID = 387439537008227162L;
	
	private long total;			// 总数据
	private long pages;			// 总页数
	private List<T> list;
	
	public long pageStart(long page, int pageSize, long total) {
		this.total = total;
		pageSize = Math.max(1, pageSize);    // 至少显示一条数据
		long mod = total % pageSize;
		this.pages = 0 == mod ? total / pageSize : (total / pageSize) + 1;
		page = Math.max(1, page);
		page = Math.min(this.pages, page);
		return (page - 1) * pageSize;
	}
	
	public static final <T> Pager<T> empty() {
		return new Pager<T>();
	}
}
