package com.datadigger.datainsight.bean;

import java.io.Serializable;
import java.util.List;

public class ParameterValue implements Serializable {

	private static final long serialVersionUID = 748224109350287743L;
	
	private List<ListParameter> standByList;
	
	private List<DateParameter> standByDate;
	
	private TreeNode standByRoot;

	public List<ListParameter> getStandByList() {
		return standByList;
	}

	public void setStandByList(List<ListParameter> standByList) {
		this.standByList = standByList;
	}

	public List<DateParameter> getStandByDate() {
		return standByDate;
	}

	public void setStandByDate(List<DateParameter> standByDate) {
		this.standByDate = standByDate;
	}

	public TreeNode getStandByRoot() {
		return standByRoot;
	}

	public void setStandByRoot(TreeNode standByRoot) {
		this.standByRoot = standByRoot;
	}

}
