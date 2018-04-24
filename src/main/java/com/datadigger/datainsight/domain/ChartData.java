package com.datadigger.datainsight.domain;


import com.datadigger.datainsight.bean.GridData;
public class ChartData extends Chart{

	private static final long serialVersionUID = 2270290157790118353L;
	private GridData Data;
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

	public GridData getData() {
		return Data;
	}

	public void setData(GridData data) {
		Data = data;
	}

	public String getPortletID() {
		return portletID;
	}
	public void setPortletID(String portletID) {
		this.portletID = portletID;
	}
	
}
