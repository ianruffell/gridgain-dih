package com.gridgain.imdb.app.replicate;

import java.util.HashMap;
import java.util.Map;

public class Settings {
	public static final DB MYSQL_DB = new DB("localhost", 3306, "root", "password", "world");
	public static final String POJO_PACKAGE = "com.gridgain.gen.model";
	public static final String CACHE_MODE = "PARTITIONED";
	public static final String CACHE_SCHEMA = "PUBLIC";

	public static Map<String, Object> getTemplateModel() {
		Map<String, Object> root = new HashMap<>();
		root.put("db", MYSQL_DB);
		root.put("package", POJO_PACKAGE);
		root.put("cachemode", CACHE_MODE);
		root.put("cacheschema", CACHE_SCHEMA);

		return root;
	}

	public static String getOutputPath() {
		return POJO_PACKAGE.replaceAll("\\.", "/");
	}

	public static class DB {
		public final String hostname;
		public final int port;
		public final String username;
		public final String password;
		public final String db;

		public DB(String hostname, int port, String username, String password, String db) {
			this.hostname = hostname;
			this.port = port;
			this.username = username;
			this.password = password;
			this.db = db;
		}

		public String getHostname() {
			return hostname;
		}

		public int getPort() {
			return port;
		}

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}

		public String getDb() {
			return db;
		}

	}

}
