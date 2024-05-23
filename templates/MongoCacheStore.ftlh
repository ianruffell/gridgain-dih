package ${package_pojo};

import static com.mongodb.client.model.Filters.eq;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;

import javax.cache.Cache;
import javax.cache.Cache.Entry;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;

import org.apache.ignite.IgniteException;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.ignite.lifecycle.LifecycleAware;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ${table.classname}MongoCacheStore extends CacheStoreAdapter<String, ${table.classname}> implements LifecycleAware {

	private MongoClient mongoClient;
	private MongoDatabase database;
	private MongoCollection<Document> collection;

	private final String HOSTNAME = "${db.hostname}";
	private final int PORT = ${db.port};
	private final String DB_NAME = "${db.schema}";
	private final String COLLECTION = "${table.name}";
	private Gson gson;

	@Override
	public void start() throws IgniteException {
		mongoClient = new MongoClient(HOSTNAME, PORT);
		database = mongoClient.getDatabase(DB_NAME);
		collection = database.getCollection(COLLECTION);
		gson = new Gson();

		System.out.printf("MongoDB started.\n");
	}

	@Override
	public void stop() throws IgniteException {
		mongoClient.close();
	}

	@Override
	public ${table.classname} load(String key) throws CacheLoaderException {
		System.out.println("Loading key: " + key);
		try {
			Document document = collection.find(eq("_id", new ObjectId(key))).first();
			${table.classname} user = to${table.classname}(document);
			return user;
		} catch (Exception e) {
			throw new CacheLoaderException(e);
		}

	}

	@Override
	public void write(Cache.Entry<? extends String, ? extends ${table.classname}> e) throws CacheWriterException {
		Document document = getDocument(e);
		collection.insertOne(document);
	}

	@Override
	public void delete(Object key) throws CacheWriterException {
		collection.deleteOne(eq("_id", new ObjectId((String) key)));
	}

	@Override
	public void loadCache(IgniteBiInClosure<String, ${table.classname}> clo, Object... args) throws CacheLoaderException {
		try {
			FindIterable<Document> find = collection.find();
			for (Document document : find) {
				${table.classname} ${table.name} = to${table.classname}(document);
				clo.apply(${table.name}.getId(), ${table.name});
			}
		} catch (Exception e) {
			throw new CacheLoaderException(e);
		}
	}

	private Document getDocument(Entry<? extends String, ? extends ${table.classname}> e) {
		Document doc = new Document(e.getKey(), gson.toJson(e.getValue()));
		return doc;
	}

	private ${table.classname} to${table.classname}(Document document) throws Exception {

		String id = document.getObjectId("_id").toString();

		${table.classname} ${table.name} = new ${table.classname}();
		Field idField = ${table.classname}.class.getDeclaredField("id");
		idField.setAccessible(true);
		idField.set(${table.name}, id);

		ObjectMapper mapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		HashMap<String, Object> map = mapper.readValue(document.toJson(), HashMap.class);
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String fieldName = it.next();
			if (!fieldName.equals("_id")) {
				Object val = map.get(fieldName);
				String clazzName = val.getClass().getSimpleName();

				try {
					Field field = ${table.classname}.class.getDeclaredField(fieldName);
					field.setAccessible(true);
	
					if (clazzName.equals("LinkedHashMap") || clazzName.equals("ArrayList")) {
						field.set(${table.name}, gson.toJson(val));
					} else {
						field.set(${table.name}, val);
					}
				} catch (NoSuchFieldException e) {
					System.err.println("Ignoring field " + fieldName);
				}
			}
		}

		return ${table.name};
	}

}
