package org.rubik.bean.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;


/**
 * 只支持简单的 and 和 or，不支持递归的 and 和 or
 * 
 * @author lynn
 *
 * @param <QUERY>
 */
@Getter
public class Query implements Serializable {

	private static final long serialVersionUID = 7213510348985683656L;

	private String andor;
	private boolean lock;
	private Integer page;
	private Integer limit;
	private Set<String> cols;
	private Integer pageSize;
	private String[] groupBys;
	private List<Criteria> criterias;
	private List<Pair<String, Boolean>> orderBys;
	
	public Query() {
		this.cols = new HashSet<String>();
		this.orderBys = new ArrayList<Pair<String, Boolean>>();
	}
	
	public Query forUpdate() { 
		this.lock = true;
		return this;
	}
	
	public Query page(int page) { 
		this.page = page;
		return this;
	}
	
	public Query limit(int limit) { 
		this.limit = limit;
		return this;
	}
	
	public Query cols(String... cols) { 
		for (String col : cols)
			this.cols.add(col);
		return this;
	}
	
	public Query pageSize(int pageSize) { 
		this.pageSize = pageSize;
		return this;
	}
	
	
	public Query orderByAsc(String col) { 
		this.orderBys.add(new Pair<String, Boolean>(col, true));
		return this;
	}
	
	public Query orderByDesc(String col) { 
		this.orderBys.add(new Pair<String, Boolean>(col, false));
		return this;
	}
	
	public Query groupBy(String... cols) { 
		this.groupBys = cols;
		return this;
	}
	
	public Query or(Criteria... conditions) { 
		this.andor = "OR";
		if (null == this.criterias)
			this.criterias = new ArrayList<Criteria>();
		this.criterias.clear();
		for (Criteria temp : conditions)
			this.criterias.add(temp);
		return this;
	}
	
	public Query or(Collection<Criteria> conditions) { 
		this.andor = "OR";
		if (null == this.criterias)
			this.criterias = new ArrayList<Criteria>();
		this.criterias.clear();
		this.criterias.addAll(conditions);
		return this;
	}
	
	public Query and(Criteria... conditions) { 
		this.andor = "AND";
		if (null == this.criterias)
			this.criterias = new ArrayList<Criteria>();
		this.criterias.clear();
		for (Criteria temp : conditions)
			this.criterias.add(temp);
		return this;
	}
	
	public Query and(Collection<Criteria> conditions) { 
		this.andor = "AND";
		if (null == this.criterias)
			this.criterias = new ArrayList<Criteria>();
		this.criterias.clear();
		this.criterias.addAll(conditions);
		return this;
	}
}
