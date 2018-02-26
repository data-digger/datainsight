package com.datadigger.datainsight.domain;


import com.datadigger.datainsight.bean.GridData;

public class TableData extends DataTable{

	private GridData gridData;
    private String portletID;
	public TableData(DataTable table){
		this.setId(table.getId());
		this.setName(table.getName());
		this.setAlias(table.getAlias());
		this.setBizViewId(table.getBizViewId());
		this.setDesc(table.getDesc());
	}
	public GridData getGridData() {
		return gridData;
	}
	public void setGridData(GridData gridData) {
		this.gridData = gridData;
	}
	public String getPortletID() {
		return portletID;
	}
	public void setPortletID(String portletID) {
		this.portletID = portletID;
	}
	
}
