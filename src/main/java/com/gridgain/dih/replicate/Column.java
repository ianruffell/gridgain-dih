package com.gridgain.dih.replicate;

import org.apache.commons.text.CaseUtils;

public class Column {

	enum TypeMapping {

		CHAR("String", "VARCHAR", "java.lang"), INT("Integer", "INTEGER", "java.lang"),
		INTEGER("Integer", "INTEGER", "java.lang"), SMALLINT("Integer", "INTEGER", "java.lang"),
		DECIMAL("Double", "DECIMAL", "java.lang"), ENUM("String", "VARCHAR", "java.lang"),
		VARCHAR("String", "VARCHAR", "java.lang"), BPCHAR("String", "VARCHAR", "java.lang"),
		NUMERIC("Integer", "INTEGER", "java.lang"), INT4("Integer", "INTEGER", "java.lang"),
		TIMESTAMP("Timestamp", "TIMESTAMP", "java.util"), TEXT("String", "VARCHAR", "java.lang"),
		STRING("String", "VARCHAR", "java.lang");

		private final String igniteType;
		private final String sqlType;
		private final String packageName;

		private TypeMapping(String igniteType, String sqlType, String packageName) {
			this.igniteType = igniteType;
			this.sqlType = sqlType;
			this.packageName = packageName;
		}

		public String getIgniteType() {
			return igniteType;
		}

		public String getSqlType() {
			return sqlType;
		}

		public String getPackageName() {
			return packageName;
		}

	}

	private String name;
	private String type;
	private boolean nullable;

	public Column(String name, String type, boolean nullable) {
		this.name = name;
		this.type = type;
		this.nullable = nullable;
	}

	public String getCcname() {
		return CaseUtils.toCamelCase(name, true, null);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIgnitetype() {
		return TypeMapping.valueOf(type.toUpperCase()).getIgniteType();
	}

	public String getSqltype() {
		return TypeMapping.valueOf(type.toUpperCase()).getSqlType();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	@Override
	public String toString() {
		return "Column [name=" + name + ", type=" + type + ", nullable=" + nullable + "]";
	}

}
