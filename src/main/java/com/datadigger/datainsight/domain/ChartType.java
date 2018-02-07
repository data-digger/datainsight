package com.datadigger.datainsight.domain;

public enum ChartType {
	PIE("PIE"),
    BAR("BAR"),
    LINE("LINE");
	
	private String name;
	ChartType(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	
	public String getDomainIDPrefix(){
		return name + ".";
	}
}
