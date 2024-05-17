package com.gridgain.dih.replicate;

public class DB {
	enum DBType {
		JDBC, CASSANDRA, MONGODB
	}

	public final String jdbcUrl;
	private final String type;
	public final String dialect;
	public final String datasourceClass;
	public final String datasourcePackage;
	private final String schema;
	private final String hostname;
	private final int port;

	public DB(String jdbcUrl, DBType type, String dialect, String datasourceClass, String datasourcePackage,
			String schema) {
		this.jdbcUrl = jdbcUrl;
		this.type = type.name();
		this.dialect = dialect;
		this.datasourceClass = datasourceClass;
		this.datasourcePackage = datasourcePackage;
		this.schema = schema;
		this.hostname = null;
		this.port = 0;

	}

	public DB(DBType type, String hostname, int port, String schema) {
		this.hostname = hostname;
		this.port = port;
		this.type = type.name();
		this.schema = schema;
		this.jdbcUrl = null;
		this.dialect = null;
		this.datasourceClass = null;
		this.datasourcePackage = null;

	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public String getDialect() {
		return dialect;
	}

	public String getDatasourceClass() {
		return datasourceClass;
	}

	public String getDatasourcePackage() {
		return datasourcePackage;
	}

	public String getSchema() {
		return schema;
	}

	public String getType() {
		return type;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

}
