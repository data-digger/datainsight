package com.datadigger.datainsight.param;

import java.io.Serializable;

import com.datadigger.datainsight.type.DBType;
import com.datadigger.datainsight.util.ValueType;




public abstract class ParamValue implements Cloneable{
	protected DBType dbType;

	public DBType getDBType() {
		return dbType;
	}
	
	public abstract Object getValue();
	
	public abstract ValueType getValueType();
	
	public String toString(){
		return getValue().toString();
	}
	
	@Override
	public boolean equals(Object obj){
		if (obj!=null && (obj instanceof ParamValue)){
			return obj==this || ((ParamValue)obj).getValue().equals(getValue());
		}
		return false;
	}
	
	public ParamValue clone() throws CloneNotSupportedException {
		return (ParamValue)super.clone();
	}
}
