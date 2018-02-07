package com.datadigger.datainsight.domain;


import com.datadigger.datainsight.bean.GridData;

public class ChartData extends Chart{

	private GridData gridData;
    
	public ChartData(Chart chart){
		this.setId(chart.getId());
		this.setName(chart.getName());
		this.setAlias(chart.getAlias());
		this.setBizViewId(chart.getBizViewId());
		this.setDefineJSON(chart.getDefineJSON());
		this.setDesc(chart.getDesc());
		this.setType(chart.getType());
	}
	public GridData getGridData() {
		return gridData;
	}

	public void setGridData(GridData gridData) {
		this.gridData = gridData;
	}
	
}
