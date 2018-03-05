package com.datadigger.datainsight.param;

import com.datadigger.datainsight.util.ValueType;

public class MultiValue extends ParamValue {
	private String value;
	private ValueType type;
	public MultiValue(String value, ValueType type) {
		this.value = value;
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public ValueType getValueType() {
		return ValueType.SET;
	}

}
