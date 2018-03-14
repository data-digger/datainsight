package com.datadigger.datainsight.domain;


import com.datadigger.datainsight.bean.GridData;
import com.datadigger.datainsight.bean.ParamGridData;

public class TableData extends DataTable{

	private ParamGridData Data;
    private String portletID;
	public TableData(DataTable table){
		this.setId(table.getId());
		this.setName(table.getName());
		this.setAlias(table.getAlias());
		this.setBizViewId(table.getBizViewId());
		this.setDesc(table.getDesc());
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
