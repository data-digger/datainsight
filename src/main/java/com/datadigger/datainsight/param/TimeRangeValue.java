package com.datadigger.datainsight.param;

//import com.freequery.metadata.SQLPart;

public abstract class TimeRangeValue extends ParamValue {

	private String start;

	private String end;

	public DateValue theDateValue;

	public NumValue theIntValue;

	public TimeRangeValue() {

	}

//	public SQLPart getSQLPart() {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
