package com.gridgain.dih.replicate;

public class DB {
	public final String jdbcUrl;
	public final String dialect;
	public final String datasourceClass;
	public final String datasourcePackage;
	private final String schema;

	public DB(String jdbcUrl, String dialect, String datasourceClass, String datasourcePackage,
			String schema) {
		this.jdbcUrl = jdbcUrl;
		this.dialect = dialect;
		this.datasourceClass = datasourceClass;
		this.datasourcePackage = datasourcePackage;
		this.schema = schema;
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

}
