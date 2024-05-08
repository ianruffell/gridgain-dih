package com.gridgain.dih.replicate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.CaseUtils;

public class Table {
	private final String name;
	private final String keyname;
	private final String keytype;
	private final String keysqltype;
	private final List<Column> columns = new ArrayList<>();

	public Table(String name, String keyname, String keytype, String keysqltype) {
		this.name = name;
		this.keyname = keyname;
		this.keytype = keytype;
		this.keysqltype = keysqltype;
	}

	public String getClassname() {
		return CaseUtils.toCamelCase(name, true, null);
	}

	public String getName() {
		return name;
	}

	public String getKeyname() {
		return keyname;
	}

	public String getKeytype() {
		return keytype;
	}

	public String getKeysqltype() {
		return keysqltype;
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

	@Override
	public String toString() {
		return "Table [name=" + name + ", keyname=" + keyname + ", keytype=" + keytype + ", keysqltype=" + keysqltype
				+ ", columns=" + columns + "]";
	}

}