package com.datadigger.datainsight.param;

import com.datadigger.datainsight.type.DBType;
import com.datadigger.datainsight.util.ValueType;

public class DateValue extends ParamValue {
	private String value;
	private ValueType valueType;

	public DateValue(String strValue, ValueType valueType, DBType dbType) {
		this.valueType = valueType;
		this.value = strValue;
		this.dbType = dbType;
	}
	
	public String getValue() {
		return value;
	}

	public ValueType getValueType() {
		return valueType;
	}
}
