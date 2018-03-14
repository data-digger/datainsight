package com.datadigger.datainsight.domain;


import com.datadigger.datainsight.bean.GridData;
import com.datadigger.datainsight.bean.ParamGridData;

public class ChartData extends Chart{

	private ParamGridData Data;
    private String portletID;
	public ChartData(Chart chart){
		this.setId(chart.getId());
		this.setName(chart.getName());
		this.setAlias(chart.getAlias());
		this.setBizViewId(chart.getBizViewId());
		this.setDefineJSON(chart.getDefineJSON());
		this.setDesc(chart.getDesc());
		this.setType(chart.getType());
	}

	public ParamGridData getData() {
		return Data;
	}

	public void setData(ParamGridData data) {
		Data = data;
	}

	public String getPortletID() {
		return portletID;
	}
	public void setPortletID(String portletID) {
		this.portletID = portletID;
	}
	
}
