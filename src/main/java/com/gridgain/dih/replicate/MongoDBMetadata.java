package com.gridgain.dih.replicate;

import static com.gridgain.dih.replicate.DB.DBType.MONGODB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoDBMetadata extends Metadata {

	public static final String DB_HOSTNAME = "localhost";
	public static final int DB_PORT = 27017;
	public static final String DB_DB = "mflix";
	public static final String DB_USERNAME = "root";
	public static final String DB_PASSWORD = "password";

	public static void main(String[] args) throws Exception {

		MongoDBMetadata cm = new MongoDBMetadata();
		List<Table> tables = cm.getTables();
		for (Table table : tables) {
			System.out.println(table.toString());
		}
	}

	public MongoDBMetadata() {
	}

	@Override
	public DB getDB() {
		return new DB(MONGODB, DB_HOSTNAME, DB_PORT, DB_DB);
	}

	@Override
	public String getMetadataForTable() {
		return "Table";
	}

	@Override
	public List<Table> getTables() throws Exception {
		List<Table> tables = new ArrayList<>();

		try (MongoClient mongoClient = new MongoClient(DB_HOSTNAME, DB_PORT)) {
			MongoDatabase database = mongoClient.getDatabase(DB_DB);
			MongoCursor<String> collectionNames = database.listCollectionNames().iterator();
			while (collectionNames.hasNext()) {
				String tableName = collectionNames.next();
				MongoCollection<Document> collection = database.getCollection(tableName);
				System.out.println(tableName);
				String json = collection.find().first().toJson();
				System.out.println(json);

				Table table = getTable(tableName, json);
				tables.add(table);
			}
		}
		return tables;
	}

	public Table getTable(String tableName, String json) throws JsonMappingException, JsonProcessingException {
		Table table = new Table(tableName, "id", "String", "VARCHAR");
		table.addColumn(new Column("id", "String", false));

		ObjectMapper mapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		HashMap<String, Object> map = mapper.readValue(json, HashMap.class);
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (!key.equals("_id")) {
				Object val = map.get(key);
				String clazzName = val.getClass().getSimpleName();

				Column column;
				if (clazzName.equals("LinkedHashMap") || clazzName.equals("ArrayList")) {
					column = new Column(key, "String", true);
				} else {
					column = new Column(key, clazzName, true);
				}

				// System.out.printf("%s[%s]\n", column.getName(), column.getIgnitetype());
				table.addColumn(column);
			}
		}

		return table;
	}

}
