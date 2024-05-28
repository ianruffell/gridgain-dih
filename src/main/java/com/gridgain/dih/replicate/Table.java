package com.gridgain.dih.replicate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.CaseUtils;

public class Table {
	private final String name;
	private final String keyname;
	private final String keyPackage;
	private final String keytype;
	private final String keysqltype;
	private final List<Column> columns = new ArrayList<>();

	public Table(String name, String keyname, String keytype, String keysqltype) {
		this.name = name;
		this.keyname = keyname;
		this.keytype = keytype;
		this.keysqltype = keysqltype;
		this.keyPackage = null;
	}

	public Table(String name, String keyname, String keyPackage, String keytype, String keysqltype) {
		this.name = name;
		this.keyname = keyname;
		this.keyPackage = keyPackage;
		this.keytype = keytype;
		this.keysqltype = keysqltype;
	}

	public String getClassname() {
		return CaseUtils.toCamelCase(name, true, null);
	}

	public String getName() {
		return name.toLowerCase();
	}

	public String getKeyname() {
		return keyname;
	}

	public String getKeynameLc() {
		return keyname.toLowerCase();
	}

	public String getKeytype() {
		return keytype;
	}

	public String getKeytypeLc() {
		return keytype.toLowerCase();
	}

	public String getKeysqltype() {
		return keysqltype;
	}

	public String getKeyPackage() {
		return keyPackage;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public String getCCName() {
		return CaseUtils.toCamelCase(name, true, null);
	}

	public void addColumn(Column column) {
		columns.add(column);
	}

	public boolean getContainsTimestamp() {
		boolean ret = false;

		for (Column col : columns) {
			if (col.getSqltype().equals(Column.TypeMapping.TIMESTAMP.getSqlType())) {
				ret = true;
				break;
			}
		}

		return ret;
	}

	@Override
	public String toString() {
		return "Table [name=" + name + ", keyname=" + keyname + ", keytype=" + keytype + ", keysqltype=" + keysqltype
				+ ", columns=" + columns + "]";
	}

}
