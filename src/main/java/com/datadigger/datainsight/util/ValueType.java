package com.datadigger.datainsight.util;

public enum ValueType {
	INTEGER,
	DOUBLE,
	LONG,
	STRING("VARCHAR"),	
	DATE,
	TIME,
	DATETIME,
	ASCII,
	BINARY,
	SET,
	UNKNOWN,
	UNKNOW;
	
	private final String type;
	
	private ValueType() {
		
		if (this.name().equals("UNKNOW"))
			type = "UNKNOWN";
		else
			type = this.name();
	}
	
	private ValueType(String type) {
		if (type.equals("UNKNOW"))
			this.type = "UNKNOWN";
		else
			this.type = type;
	}

	public String getSQLType() {
		return type;
	}
}
