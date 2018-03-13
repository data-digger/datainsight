package com.datadigger.datainsight.bean;

import java.io.Serializable;
import java.util.List;

public class TreeNode implements Serializable {

	private static final long serialVersionUID = -3512186092470748597L;

	private List<TreeNode> children;
	
	private String key;
	
	private String value;

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
	

}
