package com.datadigger.datainsight.domain;

public enum DomainType {
    DS("DS"),
    CR("CR"),
    BZ("BZ"),
    RP("RP"),
	DT("DT"),
	PA("PA");
	
	private String name;
	DomainType(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	
	public String getDomainIDPrefix(){
		return name + ".";
	}
}
