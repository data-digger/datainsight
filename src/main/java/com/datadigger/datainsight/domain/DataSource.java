package com.datadigger.datainsight.domain;

import java.io.Serializable;
import javax.persistence.*;

import com.datadigger.datainsight.util.IConnectionInfo;

@Entity
@Table(name = "t_datasource")
public class DataSource implements IConnectionInfo,Serializable{

	private static final long serialVersionUID = 7794309602127014321L;
    @Id
	@Column(name = "c_datasrcid" ,nullable = false)
	private String id;
	
	@Column(name = "c_datasrcname" ,nullable = false)
	private String name;
	
	@Column(name = "c_datasrcalias" ,nullable = false)
	private String alias;

	@Column(name = "c_datasrcdesc" ,nullable = false)
	private String desc;

	@Column(name = "c_user" ,nullable = false)
	private String user;
	@Column(name = "c_password" ,nullable = false)
	private String password;
	@Column(name = "c_drivertype" ,nullable = false)
	private String driverType;
	@Column(name = "c_driver" ,nullable = false)
	private String driver;
	@Column(name = "c_url" ,nullable = false)
	private String url;
	@Column(name = "c_maxconnum" ,nullable = false)
	private int maxConnection;
	@Column(name = "c_dbcharset" ,nullable = false)
	private String dbCharset;
	@Column(name = "c_validation" ,nullable = false)
	private String validationQuery;
	@Column(name = "c_isolation" ,nullable = false)
	private int transactionIsolation;

	
	public DataSource(){
		// no-args constructor required by JPA spec
				// this one is protected since it shouldn't be used directly
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverType() {
		
		return driverType;
	}

	public void setDriverType(String driverType) {
		this.driverType = driverType;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getMaxConnection() {
		return maxConnection;
	}

	public void setMaxConnection(int maxConnection) {
		this.maxConnection = maxConnection;
	}

	public String getDbCharset() {
		return dbCharset;
	}

	public void setDbCharset(String dbCharset) {
		this.dbCharset = dbCharset;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	public int getTransactionIsolation() {
		return transactionIsolation;
	}

	public void setTransactionIsolation(int transactionIsolation) {
		this.transactionIsolation = transactionIsolation;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
}
