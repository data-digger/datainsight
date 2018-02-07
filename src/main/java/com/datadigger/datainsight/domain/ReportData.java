package com.datadigger.datainsight.domain;

import java.util.List;
import java.util.Map;

import com.datadigger.datainsight.bean.GridData;

public class ReportData extends Report{
	private static final long serialVersionUID = 4822506708339397689L;
	private List<Map<String,GridData>> data;
	public ReportData(Report r) {
		this.setId(r.getId());
		this.setName(r.getName());
		this.setAlias(r.getAlias());
		this.setDefineJSON(r.getDefineJSON());
		this.setDesc(r.getDesc());
	}
	public List<Map<String,GridData>> getData() {
		return data;
	}
	public void setData(List<Map<String,GridData>> data) {
		this.data = data;
	}
}
