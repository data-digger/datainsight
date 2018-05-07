package com.datadigger.datainsight.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.datadigger.datainsight.bean.DefaultParameter;


public class ReportData extends Report{
	private static final long serialVersionUID = 4822506708339397689L;
	private List<ChartData> chartData;  //统计图数据
	private List<TableData> tableData;	//表格数据
	private Map<String,List<String>> standbyValueMap; //候选值列表
	
	public ReportData() {}
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
	public Map<String, List<String>> getStandbyValueMap() {
		return standbyValueMap;
	}
	public void setStandbyValueMap(Map<String, List<String>> standbyValueMap) {
		this.standbyValueMap = standbyValueMap;
	}
}
