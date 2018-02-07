package com.datadigger.datainsight.domain;

import com.datadigger.datainsight.bean.GridData;

public class ChartData extends Chart{

	private GridData gridData;

	public GridData getGridData() {
		return gridData;
	}

	public void setGridData(GridData gridData) {
		this.gridData = gridData;
	}
	
}
