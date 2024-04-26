package com.gridgain.imdb.app.replicate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.CaseUtils;

public class Table {
	private String name;
	private String keyname;
	private String keytype;
	private String keysqltype;
	private List<Column> columns = new ArrayList<>();

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

	public void setName(String name) {
		this.name = name;
	}

	public String getKeyname() {
		return keyname;
	}

	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}

	public String getKeytype() {
		return keytype;
	}

	public void setKeytype(String keytype) {
		this.keytype = keytype;
	}

	public String getKeysqltype() {
		return keysqltype;
	}

	public void setKeysqltype(String keysqltype) {
		this.keysqltype = keysqltype;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public String getCCName() {
		return CaseUtils.toCamelCase(name, true, null);
	}

	public void addColumn(Column column) {
		columns.add(column);
	}

	@Override
	public String toString() {
		return "Table [name=" + name + ", columns=" + columns + "]";
	}

}
