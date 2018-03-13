package com.datadigger.datainsight.bean;

import java.io.Serializable;

public class DateParameter implements Serializable {

	private static final long serialVersionUID = -7382401069575207186L;
	private String date;
	private String format;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}

}
