package com.datadigger.datainsight.domain;

import java.util.List;

import com.datadigger.datainsight.bean.DateParameter;
import com.datadigger.datainsight.bean.ListParameter;


public class ParameterData extends Parameter{

	private static final long serialVersionUID = 6722418251047452387L;
	
	private ListParameter defaultListValue; //默认列表值
	private DateParameter defaultDate; //默认日期值
    private List<ListParameter> standByList; //候选列表值
	//private List<DateParameter> standByDate; //候选列表值
	
	public ParameterData() {}
	public ParameterData(Parameter p) {
		this.setId(p.getId());
		this.setName(p.getName());
		this.setAlias(p.getAlias());
		this.setDefineJSON(p.getDefineJSON());
		this.setDesc(p.getDesc());
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
	public List<ListParameter> getStandByList() {
		return standByList;
	}
	public void setStandByList(List<ListParameter> standByList) {
		this.standByList = standByList;
	}

}
