package org.rubik.bean.core.param;

import java.io.Serializable;

import javax.validation.constraints.Null;

import org.rubik.bean.core.Assert;
import org.rubik.bean.core.model.Code;
import org.rubik.bean.core.model.Query;
import org.rubik.bean.core.model.RequestMeta;
import org.rubik.bean.core.model.Requestor;
import org.rubik.bean.core.model.RubikMap;

import lombok.Getter;
import lombok.Setter;

@Setter
public class Param implements Serializable {

	private static final long serialVersionUID = 6919433985298886378L;

	@Null
	private Query query;
	@Getter
	private Integer page;
	// 扩展参数
	@Getter
	private RubikMap extras;
	@Null
	@Getter
	private RequestMeta meta;
	@Getter
	private Integer pageSize;
	
	public void verify() { 
		if (null != page)
			Assert.notNull(pageSize, Code.PARAM_ERR, "pageSize is null while page is not null");
	}
	
	public Query getQuery() {
		if (null == query) 
			this.query = new Query();
		if (null != page) {
			this.query.page(page);
			this.query.pageSize(pageSize);
		}
		return this.query;
	}
	
	public void pageCheck() { 
		Assert.notNull(page, Code.PARAM_ERR, "page is null");
	}
	
	@SuppressWarnings("unchecked")
	public <REQUESTOR extends Requestor> REQUESTOR requestor() {
		return Assert.notNull((REQUESTOR) meta.getRequestor(), Code.UNLOGIN);
	}
}
