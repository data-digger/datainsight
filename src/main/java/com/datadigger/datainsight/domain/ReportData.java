package com.datadigger.datainsight.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.datadigger.datainsight.bean.DefaultParameter;


public class ReportData extends Report{
	private static final long serialVersionUID = 4822506708339397689L;
	private Set<DefaultParameter> parameterSet;
	private List<ChartData> chartData;
	private List<TableData> tableData;
	public ReportData() {}
	public ReportData(Report r) {
		this.setId(r.getId());
		this.setName(r.getName());
		this.setAlias(r.getAlias());
		this.setDefineJSON(r.getDefineJSON());
		this.setDesc(r.getDesc());
	}

	public Set<DefaultParameter> getParameterSet() {
		return parameterSet;
	}



	public void setParameterSet(Set<DefaultParameter> parameterSet) {
		this.parameterSet = parameterSet;
	}



	public List<ChartData> getChartData() {
		return chartData;
	}
	public void setChartData(List<ChartData> chartData) {
		this.chartData = chartData;
	}
	public List<TableData> getTableData() {
		return tableData;
	}
	public void setTableData(List<TableData> tableData) {
		this.tableData = tableData;
	}
}
