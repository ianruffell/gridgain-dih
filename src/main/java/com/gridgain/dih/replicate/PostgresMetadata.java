package com.gridgain.dih.replicate;

import java.sql.Connection;
import java.sql.DriverManager;

public class PostgresMetadata extends JdbcMetadata {
	public static final String DB_HOSTNAME = "localhost";
	public static final int DB_PORT = 5432;
	public static final String DB_DB = "postgres";
	public static final String DB_USERNAME = "postgres";
	public static final String DB_PASSWORD = "password";
	public static final String DB_SCHEMA = "cd";

	@Override
	public Connection getConnection() throws Exception {
		Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();
		Connection conn = DriverManager.getConnection(getJdbcUrl());
		return conn;
	}

	@Override
	public String getMetadataForTable() {
		return "TABLE";
	}

	@Override
	public DB getDB() {
		return new DB(getJdbcUrl(), DB.DBType.JDBC, "BasicJdbcDialect", "PGSimpleDataSource", "org.postgresql.ds",
				"cd", DB_USERNAME, DB_PASSWORD);
	}

	@Override
	public String getJdbcUrl() {
		return String.format("jdbc:postgresql://%s:%d/%s?user=%s&password=%s&currentSchema=%s", DB_HOSTNAME, DB_PORT,
				DB_DB, DB_USERNAME, DB_PASSWORD, DB_SCHEMA);
	}

}
