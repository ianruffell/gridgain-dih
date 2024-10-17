package com.gridgain.dih.replicate;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class JdbcMetadata extends Metadata {

	public abstract String getJdbcUrl();

	public abstract Connection getConnection() throws Exception;

	@Override
	public List<Table> getTables() throws Exception {
		Connection conn = getConnection();
		List<Table> tables = new ArrayList<>();

		DatabaseMetaData metaData = conn.getMetaData();
		ResultSet tablers = metaData.getTables(null, getDB().getSchema(), null, new String[] { getMetadataForTable() });

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
			ResultSet columns = metaData.getColumns(null, getDB().getSchema(), tableName, null);
			while (columns.next()) {
				String name = columns.getString("COLUMN_NAME");
				String type = columns.getString("TYPE_NAME");
				if (type.equals("TIMESTAMP(6)")) {
					type = "TIMESTAMP";
				}
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
