package com.datadigger.datainsight.bean;

import com.datadigger.datainsight.util.StringUtil;

public class JDBCTable implements Comparable{
	private static final long serialVersionUID = 2866903617126803937L;

	public enum Type {
		TABLE,
		VIEW,
		PROC,
		MACRO
	}
	
	private String catalog;
	private String schema;
	private String name;
	private Type type;
	private String desc;
	private JDBCTableType tableType;

	public JDBCTable() {
	}
	
	public JDBCTable(String catalog, String schema, String name, Type type, String desc) {
		this.catalog = catalog;
		this.schema = schema;
		this.name = name;
		this.type = type;
		this.desc = desc;
	}
	
	public JDBCTable(String schema, String name, Type type, String desc) {
		this.schema = schema;
		this.name = name;
		this.type = type;
		this.desc = desc;
	}
	
	public JDBCTable(String schema, String name, Type type, JDBCTableType tableType, String desc) {
		this.schema = schema;
		this.name = name;
		this.type = type;
		this.tableType = tableType;
		this.desc = desc;
	}

	public String getSchema() {
		return schema;
	}

	public String getName() {
		return name;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append('@');
		if(schema != null && schema.length() > 0)
			sb.append(schema).append('.');
		sb.append(name);
		if(desc != null && desc.length() > 0)
			sb.append('(').append(desc).append(')');
		return sb.toString();
	}

	public String getAlias() {
		return getName();
	}

	public int compareTo(Object obj) {
		if(!(obj instanceof JDBCTable))
			return -1;
		JDBCTable table = (JDBCTable) obj;
		int result = StringUtil.compareTo(getSchema(), table.getSchema());
		if(result == 0)
			result = getName().compareTo(table.getName());
		return result;
	}

	public Type getType() {
		return type;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	
	public JDBCTableType getTableType(){
		return tableType;
	}
	
	public void setTableType(JDBCTableType tableType){
		this.tableType = tableType;
	}
}
