package org.rubik.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.conversions.Bson;
import org.rubik.bean.core.Assert;
import org.rubik.bean.core.model.Criteria;
import org.rubik.bean.core.model.Query;
import org.rubik.util.common.CollectionUtil;

import com.mongodb.client.model.Filters;

public class MongoUtil {
	
	public static final Bson filter(Query query) {
		if (CollectionUtil.isEmpty(query.getCriterias()))
			return null;
		Assert.isTrue(query.getCriterias().size() == 1, "mongo first criterial length must one");
		return _filter(query.getCriterias().get(0));
	}
	
	private static final Bson _filter(Criteria criteria) { 
		switch (criteria.getComparison()) {
		case Criteria.EQ:
			return Filters.eq(criteria.getCol(), criteria.getValue());
		case Criteria.NEQ:
			return Filters.ne(criteria.getCol(), criteria.getValue());
		case Criteria.GT:
			return Filters.gt(criteria.getCol(), criteria.getValue());
		case Criteria.GTE:
			return Filters.gte(criteria.getCol(), criteria.getValue());
		case Criteria.LT:
			return Filters.lt(criteria.getCol(), criteria.getValue());
		case Criteria.LTE:
			return Filters.lte(criteria.getCol(), criteria.getValue());
		case Criteria.IN:
			return Filters.in(criteria.getCol(), criteria.getValue());
		case Criteria.NIN:
			return Filters.nin(criteria.getCol(), criteria.getValue());
		case Criteria.LIKE:
			return Filters.regex(criteria.getCol(), criteria.getValue().toString());
		case Criteria.ISNULL:
			return Filters.exists(criteria.getCol(), false);
		case Criteria.NOTNULL:
			return Filters.exists(criteria.getCol());
		case Criteria.BETWEEN:
			Object[] arr = (Object[]) criteria.getValue();
			return Filters.and(Filters.gte(criteria.getCol(), arr[0]), Filters.lte(criteria.getCol(), arr[1]));
		case Criteria.ALL:
			return Filters.all(criteria.getCol(), criteria.getValue());
		case Criteria.OR:
			List<Bson> filters = new ArrayList<Bson>();
			Collection<?> conditions = (Collection<?>) criteria.getValue(); 
			for (Object object : conditions) 
				filters.add(_filter((Criteria) object));
			return Filters.or(filters);
		case Criteria.AND:
			filters = new ArrayList<Bson>();
			conditions = (Collection<?>) criteria.getValue(); 
			for (Object object : conditions) 
				filters.add(_filter((Criteria) object));
			return Filters.and(filters);
		default:
			throw new RuntimeException("Unsupported operator : " + criteria.getComparison());
		}
	}

	public static final Bson or(String field, Collection<?> collection) {
		Set<Bson> set = new HashSet<Bson>();
		for (Object object : collection)
			set.add(Filters.eq(field, object));
		return Filters.or(set);
	}
	
	public static final Bson and(Map<String, Object> properties) {
		Set<Bson> set = new HashSet<Bson>();
		for (Entry<String, Object> entry : properties.entrySet())
			set.add(Filters.eq(entry.getKey(), entry.getValue()));
		return Filters.and(set);
	}
}
