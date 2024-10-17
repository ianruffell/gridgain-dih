package com.gridgain.dih.replicate;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySqlMetadata extends JdbcMetadata
{

	public static final String DB_HOSTNAME = "localhost";
	public static final int DB_PORT = 3306;
	public static final String DB_DB = "world";
	public static final String DB_USERNAME = "root";
	public static final String DB_PASSWORD = "password";
	public static final String DB_SCHEMA = "public";

	@Override
	public Connection getConnection() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
		Connection conn = DriverManager.getConnection(getJdbcUrl());
		return conn;
	}

	@Override
	public DB getDB() {
		return new DB(getJdbcUrl(), DB.DBType.JDBC, "MySQLDialect", "MysqlDataSource", "com.mysql.cj.jdbc", DB_SCHEMA, DB_USERNAME, DB_PASSWORD);
	}

	@Override
	public String getJdbcUrl() {
		return String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s",
				DB_HOSTNAME, DB_PORT, DB_DB, DB_USERNAME, DB_PASSWORD);
	}

	@Override
	public String getMetadataForTable() {
		return "Table";
	}

}
