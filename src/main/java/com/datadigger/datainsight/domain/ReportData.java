package com.datadigger.datainsight.domain;

import java.util.List;
import java.util.Map;


public class ReportData extends Report{
	private static final long serialVersionUID = 4822506708339397689L;
	private List<ChartData> data;
	public ReportData(Report r) {
		this.setId(r.getId());
		this.setName(r.getName());
		this.setAlias(r.getAlias());
		this.setDefineJSON(r.getDefineJSON());
		this.setDesc(r.getDesc());
	}
	public List<ChartData> getData() {
		return data;
	}
	public void setData(List<ChartData> data) {
		this.data = data;
	}
}
