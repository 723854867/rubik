package org.rubik.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.rubik.bean.core.Assert;
import org.rubik.bean.core.Identifiable;
import org.rubik.bean.core.model.Pager;
import org.rubik.bean.core.model.Pair;
import org.rubik.bean.core.model.Query;
import org.rubik.util.common.CollectionUtil;
import org.rubik.util.common.StringUtil;
import org.rubik.util.serializer.GsonSerializer;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class Mongo {

	private MongoClient mongo;
	private MongoConfig config;
	private MongoDatabase connection;
	
	public void init() {
		if (StringUtil.hasText(config.getUsername()) && StringUtil.hasText(config.getPassword())) {
			MongoCredential credential = MongoCredential.createCredential(config.getUsername(), config.getDb(), config.getPassword().toCharArray());
			ServerAddress address = new ServerAddress(config.getHost());
			this.mongo = new MongoClient(address, credential, MongoClientOptions.builder().build());
		} else 
			this.mongo = new MongoClient(config.getHost());
		this.connection = mongo.getDatabase(config.getDb());
	}
	
	public void insertOne(String collectionName, Object object) {
		MongoCollection<Document> collection = connection.getCollection(collectionName);
		collection.insertOne(serial(object));
	}
	
	public void insertMany(String collectionName, Collection<?> objects) {
		MongoCollection<Document> collection = collection(collectionName);
		List<Document> list = new ArrayList<Document>(objects.size());
		for (Object object : objects)
			list.add(serial(object));
		collection.insertMany(list);
	}
	
	public <T> List<T> find(String collectionName, Class<T> clazz) { 
		MongoCollection<Document> collection = collection(collectionName);
		FindIterable<Document> iterable = collection.find();
		List<T> list = new ArrayList<T>();
		MongoCursor<Document> cursor = iterable.iterator();
		while (cursor.hasNext()) 
			list.add(GsonSerializer.fromJson(cursor.next().toJson(), clazz));
		return list;
	}
	
	public <T> List<T> find(String collectionName, Bson filter, Class<T> clazz) { 
		MongoCollection<Document> collection = collection(collectionName);
		FindIterable<Document> iterable = collection.find(filter);
		List<T> list = new ArrayList<T>();
		MongoCursor<Document> cursor = iterable.iterator();
		while (cursor.hasNext()) 
			list.add(GsonSerializer.fromJson(cursor.next().toJson(), clazz));
		return list;
	}
	
	public <KEY, T extends Identifiable<KEY>> Map<KEY, T> findMap(String collectionName, Class<T> clazz) { 
		MongoCollection<Document> collection = collection(collectionName);
		FindIterable<Document> iterable = collection.find();
		Map<KEY, T> map = new HashMap<KEY, T>();
		MongoCursor<Document> cursor = iterable.iterator();
		while (cursor.hasNext()) {
			T t = GsonSerializer.fromJson(cursor.next().toJson(), clazz);
			map.put(t.key(), t);
		}
		return map;
	}
	
	public <KEY, T extends Identifiable<KEY>> Map<KEY, T> findMap(String collectionName, Bson filter, Class<T> clazz) { 
		MongoCollection<Document> collection = collection(collectionName);
		FindIterable<Document> iterable = collection.find(filter);
		Map<KEY, T> map = new HashMap<KEY, T>();
		MongoCursor<Document> cursor = iterable.iterator();
		while (cursor.hasNext()) {
			T t = GsonSerializer.fromJson(cursor.next().toJson(), clazz);
			map.put(t.key(), t);
		}
		return map;
	}
	
	public void bulkUpdateOne(String collectionName, Map<Bson, Bson> updates) {
		MongoCollection<Document> collection = collection(collectionName);
		List<UpdateOneModel<Document>> list = new ArrayList<UpdateOneModel<Document>>(updates.size());
		for (java.util.Map.Entry<Bson, Bson> entry : updates.entrySet())
			list.add(new UpdateOneModel<Document>(entry.getKey(), entry.getValue()));
		collection.bulkWrite(list);
	}
	
	public <KEY, MODEL extends Identifiable<KEY>> long bulkReplaceOne(String collectionName, Map<KEY, MODEL> replaces) {
		MongoCollection<Document> collection = collection(collectionName);
		List<ReplaceOneModel<Document>> list = new ArrayList<ReplaceOneModel<Document>>(replaces.size());
		for (MODEL model : replaces.values()) 
			list.add(new ReplaceOneModel<Document>(Filters.eq("_id", model.key()), serial(model), new ReplaceOptions().upsert(true)));
		BulkWriteResult result = collection.bulkWrite(list);
		return result.getModifiedCount();
	}
	
	/**
	 * 分页显示：不排序
	 */
	public <T> List<T> paging(String collectionName, int start, int pageSize, Class<T> clazz) {
		MongoCollection<Document> collection = collection(collectionName);
		FindIterable<Document> iterable = collection.find().skip(start).limit(pageSize);
		List<T> list = new ArrayList<T>(0);
		MongoCursor<Document> cursor = iterable.iterator();
		while (cursor.hasNext()) 
			list.add(GsonSerializer.fromJson(cursor.next().toJson(), clazz));
		return list;
	}
	
	/**
	 * 分页显示：不排序
	 */
	public <T> List<T> paging(String collectionName, Bson filter, int start, int pageSize, Class<T> clazz) {
		MongoCollection<Document> collection = collection(collectionName);
		FindIterable<Document> iterable = collection.find(filter).skip(start).limit(pageSize);
		List<T> list = new ArrayList<T>(0);
		MongoCursor<Document> cursor = iterable.iterator();
		while (cursor.hasNext()) 
			list.add(GsonSerializer.fromJson(cursor.next().toJson(), clazz));
		return list;
	}
	
	/**
	 * 分页显示：排序
	 */
	public <T> List<T> pagingAndSort(String collectionName, Bson sort, int start, int pageSize, Class<T> clazz) {
		MongoCollection<Document> collection = collection(collectionName);
		FindIterable<Document> iterable = collection.find().sort(sort).skip(start).limit(pageSize);
		List<T> list = new ArrayList<T>(0);
		MongoCursor<Document> cursor = iterable.iterator();
		while (cursor.hasNext()) 
			list.add(GsonSerializer.fromJson(cursor.next().toJson(), clazz));
		return list;
	}
	
	public <T> Pager<T> query(String collectionName, Query query, Class<T> clazz) {
		MongoCollection<Document> collection = collection(collectionName);
		Pager<T> pager = new Pager<T>();
		Bson filter = MongoUtil.filter(query);
		long total = null == filter ? collection.countDocuments() : collection.countDocuments(filter);
		if (total <= 0)
			return pager;
		FindIterable<Document> iterable = null == filter ? collection.find() : collection.find(filter);
		List<Pair<String, Boolean>> orders = query.getOrderBys();
		if (!CollectionUtil.isEmpty(orders)) {
			for (Pair<String, Boolean> pair : orders) 
				iterable.sort(pair.getValue() ? Sorts.ascending(pair.getKey()) : Sorts.descending(pair.getKey()));
		}
		if (null != query.getPage()) {
			long pageStart = pager.pageStart(query.getPage(), query.getPageSize(), total);
			iterable.skip((int) pageStart).limit(query.getPageSize()).batchSize(50);
		}
		MongoCursor<Document> cursor = iterable.iterator();
		List<T> list = new ArrayList<T>();
		while (cursor.hasNext()) 
			list.add(GsonSerializer.fromJson(cursor.next().toJson(), clazz));
		pager.setList(list);
		return pager;
	}
	
	/**
	 * 分页显示：排序
	 */
	public <T> List<T> pagingAndSort(String collectionName, Bson filter, Bson sort, int start, int pageSize, Class<T> clazz) {
		MongoCollection<Document> collection = collection(collectionName);
		FindIterable<Document> iterable = collection.find(filter).sort(sort).skip(start).limit(pageSize);
		List<T> list = new ArrayList<T>(0);
		MongoCursor<Document> cursor = iterable.iterator();
		while (cursor.hasNext()) 
			list.add(GsonSerializer.fromJson(cursor.next().toJson(), clazz));
		return list;
	}
	
	public long count(String collectionName) {
		MongoCollection<Document> collection = collection(collectionName);
		return collection.countDocuments();
	}
	
	public long count(String collectionName, Bson filter) {
		MongoCollection<Document> collection = collection(collectionName);
		return collection.countDocuments(filter);
	}
	
	public <T> T findOne(String collectionName, Bson filter, Class<T> clazz) { 
		MongoCollection<Document> collection = collection(collectionName);
		FindIterable<Document> iterable = collection.find(filter);
		Document document = iterable.first();
		return null == document ? null : deserial(document, clazz);
	}
	
	public <T> T findOne(String collectionName, Bson filter, Bson sort, Class<T> clazz) { 
		MongoCollection<Document> collection = collection(collectionName);
		FindIterable<Document> iterable = collection.find(filter).sort(sort);
		Document document = iterable.first();
		return null == document ? null : deserial(document, clazz);
	}
	
	public long replaceOne(String collectionName, Bson filter, Object replacement) { 
		MongoCollection<Document> collection = collection(collectionName);
		UpdateResult result = collection.replaceOne(filter, serial(replacement));
		Assert.isTrue(result.getModifiedCount() <= 1, "Mongo replace one while modified more than one!");
		return result.getModifiedCount();
	}
	
	public void replaceOne(String collectionName, Bson filter, Object replacement, ReplaceOptions options) {
		MongoCollection<Document> collection = collection(collectionName);
		collection.replaceOne(filter, serial(replacement), options);
	}
	
	public <T> T findOneAndUpdate(String collectionName, Bson filter, Bson update, FindOneAndUpdateOptions options, Class<T> clazz) { 
		MongoCollection<Document> collection = collection(collectionName);
		Document document = collection.findOneAndUpdate(filter, update, options);
		return null == document ? null : deserial(document, clazz);
	}
	
	public void update(String collectionName, Bson filter, Bson update) {
		MongoCollection<Document> collection = collection(collectionName);
		collection.updateMany(filter, update);
	}
	
	public long deleteMany(String collectionName, Bson filter) {
		MongoCollection<Document> collection = collection(collectionName);
		DeleteResult result = collection.deleteMany(filter);
		return result.getDeletedCount();
	}
	
	public long deleteOne(String collectionName, Bson filter) {
		MongoCollection<Document> collection = collection(collectionName);
		DeleteResult result = collection.deleteOne(filter);
		Assert.isTrue(result.getDeletedCount() <= 1, "Mongo delete one while deleted more than one!");
		return result.getDeletedCount();
	}
	
	public Document serial(Object model) {
		String json = GsonSerializer.toJson(model);
		return Document.parse(json);
	}
	
	public <T> T deserial(Document document, Class<T> clazz) {
		return GsonSerializer.fromJson(document.toJson(), clazz);
	}
	
	public MongoCollection<Document> collection(String collectionName) {
		return connection.getCollection(collectionName);
	}
	
	public void setConfig(MongoConfig config) {
		this.config = config;
	}
	
	public void dispose() {
		this.mongo.close();
	}
}
