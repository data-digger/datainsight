package com.datadigger.datainsight.domain;

import java.util.List;
import java.util.Map;


public class ReportData extends Report{
	private static final long serialVersionUID = 4822506708339397689L;
	private List<ChartData> chartData;
	private List<TableData> tableData;
	public ReportData(Report r) {
		this.setId(r.getId());
		this.setName(r.getName());
		this.setAlias(r.getAlias());
		this.setDefineJSON(r.getDefineJSON());
		this.setDesc(r.getDesc());
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
