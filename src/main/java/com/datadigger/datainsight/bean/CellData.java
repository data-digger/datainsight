package com.datadigger.datainsight.bean;

import java.io.Serializable;
import java.util.Date;
import com.datadigger.datainsight.util.ValueType;

public class CellData implements Serializable {
	private static final long serialVersionUID = 8061795478130919085L;
	private ValueType type;
	private boolean isNull;
	private String displayValue;
	private String stringValue;
	private int intValue;
	private double doubleValue;
	private Date dateValue;
	private long longValue;
	
	public Date getDateValue() {
		return dateValue;
	}
	public void setDateValue(ValueType type, Date dateValue) {
		this.type = type;
		this.dateValue = dateValue;
		if(dateValue != null && this.dateValue.getClass() != Date.class  )
			this.dateValue = new Date(this.dateValue.getTime());
	}
	public String getDisplayValue() {
		return displayValue;
	}
	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}
	public double getDoubleValue() {
		return doubleValue;
	}
	public void setDoubleValue(double doubleValue) {
		type = ValueType.DOUBLE;
		this.doubleValue = doubleValue;
		this.intValue = (int) doubleValue;
	}
	public int getIntValue() {
		return intValue;
	}
	public void setIntValue(int intValue) {
		type = ValueType.INTEGER;
		this.intValue = intValue;
		this.doubleValue = intValue;
	}
	public long getLongValue() {
		return longValue;
	}
	public void setLongValue(long longValue) {
		type = ValueType.LONG;
		this.longValue = longValue;
		this.doubleValue = longValue;
	}
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		type = ValueType.STRING;
		 
		if(stringValue == null){
			//这里可以做一些自定义处理
			this.stringValue = "";
		}else{
			this.stringValue = stringValue;
		}
	}
	public ValueType getType() {
		return type;
	}
	public void setType(ValueType type) {
		this.type = type;
	}
	
	public int hashCode() {
		return 0;
	}
	
	public boolean equals(Object obj) {
		if(!(obj instanceof CellData))
			return false;
		CellData tmp = (CellData) obj;
		if(this.type != tmp.type)
			return false;
		switch(type) {
			case INTEGER:
				return this.intValue == tmp.intValue;
			case DOUBLE:
				return this.doubleValue == tmp.doubleValue;
			case DATE:
			case DATETIME:
			case TIME:
				return this.dateValue.equals(tmp.dateValue);
			case STRING:
				return this.stringValue.equals(tmp.stringValue);
		}
		return false;
	}
	public boolean isNull() {
		return isNull;
	}
	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}
}
