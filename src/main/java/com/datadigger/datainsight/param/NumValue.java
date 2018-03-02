package com.datadigger.datainsight.param;

import com.datadigger.datainsight.util.ValueType;

public class NumValue extends ParamValue {
	private double num;
	private ValueType valueType;
	
	/** 
	 * @param data
	 * @param numberType should be NumValue.NUM_INTEGER or NumValue.NUM_DOUBLE
	 */
	public NumValue(double data, ValueType valueType) {
		this.num = data;
		this.valueType = valueType;
	}

	public double getNumber() {
		return num;
	}

	public Object getValue() {
		return num;
	}

	public ValueType getValueType() {
		return valueType;
	}
}
