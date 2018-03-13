package com.datadigger.datainsight.bean;

import java.io.Serializable;

public class DefaultParameter implements Serializable {

	private static final long serialVersionUID = 2186952544250084288L;
	private String paramId;
	private String paramType;
	private ListParameter defaultListValue;
	private DateParameter defaultDate;
	public String getParamId() {
		return paramId;
	}
	public void setParamId(String paramId) {
		this.paramId = paramId;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public ListParameter getDefaultListValue() {
		return defaultListValue;
	}
	public void setDefaultListValue(ListParameter defaultListValue) {
		this.defaultListValue = defaultListValue;
	}
	public DateParameter getDefaultDate() {
		return defaultDate;
	}
	public void setDefaultDate(DateParameter defaultDate) {
		this.defaultDate = defaultDate;
	}
	
	
	
}
