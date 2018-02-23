package com.datadigger.datainsight.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 数据表格实体对象
 * @author luxd
 *
 */

@Entity
@Table(name = "t_table")
public class DataTable implements  Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 659410867464323261L;

	private String id;

	private String name;
	
	private String alias;
	
	private String desc;

	private String bizViewId;
	
	
	@Id
	@Column(name = "c_tableid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "c_tablename", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "c_tablealias")
	public String getAlias() {
		if (alias==null)
			return getName();
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	@Column(name = "c_tabledesc")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "c_viewid")
	public String getBizViewId() {
		return bizViewId;
	}

	public void setBizViewId(String bizViewId) {
		this.bizViewId = bizViewId;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof DataTable) {
			return getId().equals(((DataTable)obj).getId());
		} else
			return false;
	}

}

