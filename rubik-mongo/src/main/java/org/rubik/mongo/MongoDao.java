package org.rubik.mongo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import org.rubik.bean.core.Identifiable;
import org.rubik.bean.core.model.Pager;
import org.rubik.bean.core.model.Query;

import com.mongodb.client.model.Filters;

public class MongoDao<ENTITY extends Identifiable<String>> {
	
	protected static final String FIELD_ID					= "_id";

	protected Mongo mongo;
	protected String collection;
	protected Class<ENTITY> clazz;
	
	@SuppressWarnings("unchecked")
	public MongoDao(String collection) {
		this.collection = collection;
		Type superType = getClass().getGenericSuperclass();   
		Type[] generics = ((ParameterizedType) superType).getActualTypeArguments();  
		this.clazz = (Class<ENTITY>) generics[0];
	}
	
	public void insert(ENTITY entity) {
		mongo.insertOne(collection, entity);
	}
	
	public void insertMany(Collection<ENTITY> entities) {
		mongo.insertMany(collection, entities);
	}
	
	public ENTITY getByKey(String key) {
		return mongo.findOne(collection, Filters.eq(FIELD_ID, key), clazz);
	}
	
	public Map<String, ENTITY> getByKeys(Collection<String> keys) {
		return mongo.findMap(collection, MongoUtil.or(FIELD_ID, keys), clazz);
	}
	
	public Pager<ENTITY> query(Query query) {
		return mongo.query(collection, query, clazz);
	}
	
	public long update(ENTITY entity) {
		return mongo.replaceOne(collection, Filters.eq(FIELD_ID, entity.key()), entity);
	}
	
	public long updateMap(Map<String, ENTITY> entities) {
		return mongo.bulkReplaceOne(collection, entities);
	}
	
	public long deleteByKey(String key) {
		return mongo.deleteOne(collection, Filters.eq(FIELD_ID, key));
	}
	
	public long deleteByKeys(Collection<String> keys) {
		return mongo.deleteMany(collection, MongoUtil.or(FIELD_ID, keys));
	}
	
	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}
}
