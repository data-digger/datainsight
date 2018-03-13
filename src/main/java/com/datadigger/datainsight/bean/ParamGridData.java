package com.datadigger.datainsight.bean;

import java.io.Serializable;
import java.util.List;

public class ParamGridData implements Serializable {

	private static final long serialVersionUID = -5653452465323992408L;
	private GridData gridData;
	private List<DefaultParameter> defaultParameters;

	public GridData getGridData() {
		return gridData;
	}
	public void setGridData(GridData gridData) {
		this.gridData = gridData;
	}
	public List<DefaultParameter> getDefaultParameters() {
		return defaultParameters;
	}
	public void setDefaultParameters(List<DefaultParameter> defaultParameters) {
		this.defaultParameters = defaultParameters;
	}
	
}
