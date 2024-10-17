package com.gridgain.dih.replicate;

import org.apache.commons.text.CaseUtils;

public class Column {

	enum TypeMapping {

		CHAR("String", "VARCHAR", "string", "java.lang"), INT("Integer", "INTEGER", "integer", "java.lang"),
		INTEGER("Integer", "INTEGER", "integer", "java.lang"), SMALLINT("Integer", "INTEGER", "integer", "java.lang"),
		DECIMAL("Double", "DECIMAL", "number", "java.lang"), ENUM("String", "VARCHAR", "string", "java.lang"),
		VARCHAR("String", "VARCHAR", "string", "java.lang"), VARCHAR2("String", "VARCHAR", "string", "java.lang"),
		BPCHAR("String", "VARCHAR", "string", "java.lang"), NUMBER("Integer", "INTEGER", "number", "java.lang"),
		NUMERIC("Integer", "INTEGER", "number", "java.lang"), INT4("Integer", "INTEGER", "integer", "java.lang"),
		TIMESTAMP("Timestamp", "TIMESTAMP", "number", "java.sql"), DATE("Date", "DATE", "string", "java.sql"),
		TEXT("String", "VARCHAR", "string", "java.lang"), STRING("String", "VARCHAR", "string", "java.lang"),
		BLOB("Object", "BLOB", "object", "java.lang");

		private final String igniteType;
		private final String sqlType;
		private final String openapiType;
		private final String packageName;

		private TypeMapping(String igniteType, String sqlType, String openapiType, String packageName) {
			this.igniteType = igniteType;
			this.sqlType = sqlType;
			this.openapiType = openapiType;
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

		public String getOpenapiType() {
			return openapiType;
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

	public String getOpenapiType() {
		return TypeMapping.valueOf(type.toUpperCase()).getOpenapiType();
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
