package com.datadigger.datainsight.util;

public interface IConnectionInfo {
	

	public String getId();
	public String getName();
	public String getAlias();
	public String getDesc();
	
	
	public String getUser();
	
	
	public String getPassword();
	
	
	public String getDriverType();
	
	
	public String getDriver();
	
	public String getUrl();

	public int getMaxConnection();
	
	
	public String getDbCharset();
	
	
	public String getValidationQuery();
	
	public int getTransactionIsolation();
}
