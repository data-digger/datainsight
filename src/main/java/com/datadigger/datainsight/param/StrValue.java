package com.datadigger.datainsight.param;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.datadigger.datainsight.util.ValueType;


public class StrValue extends ParamValue implements Serializable{
	private String data;
	public StrValue(String data) {
		this.data = data;
	}

	public String getValue() {
		return data;
	}

	public ValueType getValueType() {
		return ValueType.STRING;
	}
	
	public String getMappingDateTime(String dtStr) {
		String result = dtStr;
		if (result != null) {
			String regex = "^'\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}'$|^'\\d{2}:\\d{2}'$";
			Matcher matcher = Pattern.compile(regex).matcher(dtStr);
			if (matcher.find())
				result = result.substring(0, result.length() - 1) + ":00'";
		}
		return result;
	};
}
