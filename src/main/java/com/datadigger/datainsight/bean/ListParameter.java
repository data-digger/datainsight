package com.datadigger.datainsight.bean;

import java.io.Serializable;
import java.util.Date;
import com.datadigger.datainsight.util.ValueType;

public class ListParameter implements Serializable {

	private static final long serialVersionUID = 5659105019701819702L;
	private String key;
	private String value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
