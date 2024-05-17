package com.gridgain.dih.replicate;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.metadata.schema.ColumnMetadata;
import com.datastax.oss.driver.api.core.metadata.schema.KeyspaceMetadata;
import com.datastax.oss.driver.api.core.metadata.schema.TableMetadata;
import com.gridgain.dih.replicate.Column.TypeMapping;

public class CassandraMetadata extends Metadata {

	public static final String DB_HOSTNAME = "localhost";
	public static final int DB_PORT = 9042;
	public static final String DB_DB = "packt";
	public static final String DB_USERNAME = "root";
	public static final String DB_PASSWORD = "password";
	public static final String DB_SCHEMA = "packt";

	public static void main(String[] args) throws Exception {
		CassandraMetadata cm = new CassandraMetadata();
		List<Table> tables = cm.getTables();
		for (Table table : tables) {
			System.out.println(table.toString());
		}
	}

	public CassandraMetadata() {
	}

	@Override
	public DB getDB() {
		return new DB(DB.DBType.CASSANDRA, DB_HOSTNAME, DB_PORT, DB_SCHEMA);
	}

	@Override
	public String getMetadataForTable() {
		return "Table";
	}

	@Override
	public List<Table> getTables() throws Exception {
		List<Table> tables = new ArrayList<>();

		try (CqlSession session = CqlSession.builder().addContactPoint(new InetSocketAddress(DB_HOSTNAME, DB_PORT))
				.withLocalDatacenter("").build()) {
			KeyspaceMetadata keyspaceMetadata = session.getMetadata().getKeyspace(DB_SCHEMA).get();
			Map<CqlIdentifier, TableMetadata> tablemd = keyspaceMetadata.getTables();
			Iterator<CqlIdentifier> it2 = tablemd.keySet().iterator();
			while (it2.hasNext()) {
				TableMetadata tableMetadata = tablemd.get(it2.next());
				String tableName = tableMetadata.getName().asCql(true);
				ColumnMetadata keyCol = tableMetadata.getPrimaryKey().get(0);
				TypeMapping keyType = Column.TypeMapping.valueOf(keyCol.getType().asCql(false, true).toUpperCase());
				Table table = new Table(tableName, keyCol.getName().asCql(true), keyType.getPackageName(),
						keyType.getIgniteType(), keyType.getSqlType());
				Map<CqlIdentifier, ColumnMetadata> columns = tableMetadata.getColumns();
				Iterator<CqlIdentifier> it3 = columns.keySet().iterator();
				while (it3.hasNext()) {
					ColumnMetadata columnMetadata = columns.get(it3.next());
					if (!columnMetadata.getName().asCql(true).equals(keyCol.getName().asCql(true))) {
						Column column = new Column(columnMetadata.getName().asCql(true),
								columnMetadata.getType().asCql(false, true).toUpperCase(), true);
						table.addColumn(column);
					}
				}
				tables.add(table);
			}
		}

		return tables;
	}

}
