package com.gridgain.dih.replicate;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Metadata {

	public static final String POJO_PACKAGE = "com.gridgain.gen.model";
	public static final String CACHE_MODE = "PARTITIONED";
	public static final String CACHE_SCHEMA = "PUBLIC";

	public abstract Connection getConnection() throws Exception;

	public abstract DB getDB();

	public abstract String getMetadataForTable();

	public abstract String getJdbcUrl();

	public Map<String, Object> getTemplateModel() {
		Map<String, Object> root = new HashMap<>();
		root.put("package", POJO_PACKAGE);
		root.put("cachemode", CACHE_MODE);
		root.put("cacheschema", CACHE_SCHEMA);

		return root;
	}

	public String getOutputPath() {
		return "src/main/java/" + POJO_PACKAGE.replaceAll("\\.", "/");
	}


	public List<Table> getTables() throws Exception {
		Connection conn = getConnection();
		List<Table> tables = new ArrayList<>();

		DatabaseMetaData metaData = conn.getMetaData();
		ResultSet tablers = metaData.getTables(null, null, null, new String[] { getMetadataForTable() });

		List<String> tableNames = new ArrayList<>();
		while (tablers.next()) {
			String table = tablers.getString("TABLE_NAME");
			if (!table.equals("sys_config") && !table.startsWith("pg_") && !table.endsWith("_pkey")) {
				tableNames.add(table);
			}
		}

		for (String tableName : tableNames) {
			System.out.println(tableName);
			Table table = null;
			ResultSet columns = metaData.getColumns(null, null, tableName, null);
			while (columns.next()) {
				/*
				 * for (int i = 1; i <= columns.getMetaData().getColumnCount(); i++) {
				 * System.out.println(columns.getObject(i)); } System.out.println();
				 */
				String name = columns.getString("COLUMN_NAME");
				String type = columns.getString("TYPE_NAME");
				boolean nullable = columns.getBoolean("IS_NULLABLE");
				Column column = new Column(name, type, nullable);

				if (table == null) {
					table = new Table(tableName, name, column.getIgnitetype(), column.getSqltype());
				}
				table.addColumn(column);
			}
			System.out.println(table.toString());

			tables.add(table);
		}
		return tables;
	}
}
