package com.gridgain.dih.replicate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Metadata {

	public static final String APP_PACKAGE = "com.gridgain.gen.app";
	public static final String REST_PACKAGE = "com.gridgain.gen.rest";
	public static final String POJO_PACKAGE = "com.gridgain.gen.model";
	public static final String CACHE_MODE = "PARTITIONED";
	public static final String CACHE_SCHEMA = "PUBLIC";

	public abstract DB getDB();

	public abstract String getMetadataForTable();

	public abstract List<Table> getTables() throws Exception;

	public Map<String, Object> getTemplateModel() {
		Map<String, Object> root = new HashMap<>();
		root.put("package_app", APP_PACKAGE);
		root.put("package_rest", REST_PACKAGE);
		root.put("package_pojo", POJO_PACKAGE);
		root.put("cachemode", CACHE_MODE);
		root.put("cacheschema", CACHE_SCHEMA);

		return root;
	}

	public String getPojoOutputPath() {
		return "src/main/java/" + POJO_PACKAGE.replaceAll("\\.", "/");
	}

	public String getRestOutputPath() {
		return "src/main/java/" + REST_PACKAGE.replaceAll("\\.", "/");
	}

	public String getAppOutputPath() {
		return "src/main/java/" + APP_PACKAGE.replaceAll("\\.", "/");
	}

}
