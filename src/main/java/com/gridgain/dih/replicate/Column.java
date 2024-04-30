package com.gridgain.dih.replicate;

import org.apache.commons.text.CaseUtils;

public class Column {

	enum TypeMapping {

		CHAR("String", "VARCHAR"), INT("Integer", "INTEGER"), SMALLINT("Integer", "INTEGER"),
		DECIMAL("Double", "DECIMAL"), ENUM("String", "VARCHAR"), varchar("String", "VARCHAR"),
		bpchar("String", "VARCHAR"), numeric("Integer", "INTEGER"), int4("Integer", "INTEGER"),
		timestamp("Timestamp", "TIMESTAMP");

		private final String igniteType;
		private String sqlType;

		private TypeMapping(String igniteType, String sqlType) {
			this.igniteType = igniteType;
			this.sqlType = sqlType;
		}

		public String getIgniteType() {
			return igniteType;
		}

		public String getSqlType() {
			return sqlType;
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
		return CaseUtils.toCamelCase(name, false, null);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIgnitetype() {
		return TypeMapping.valueOf(type).getIgniteType();
	}

	public String getSqltype() {
		return TypeMapping.valueOf(type).getSqlType();
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
