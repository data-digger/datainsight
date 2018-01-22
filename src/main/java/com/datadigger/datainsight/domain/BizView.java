package com.datadigger.datainsight.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "t_bizview")
public class BizView implements  Serializable {
	private static final long serialVersionUID = -5174357964526067818L;

	private String id;

	private String name;
	
	private String alias;

	private String desc;

	private String defineJSON;
	
	private String dataSourceId;
	
	
	@Id
	@Column(name = "c_viewid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "c_viewname", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "c_viewalias")
	public String getAlias() {
		if (alias==null)
			return getName();
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Column(name = "c_viewdesc")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "c_viewdefine")
	public String getDefineJSON() {
		if(defineJSON == null)
			return defineJSON;
		return defineJSON.trim();
	}

	public void setDefineJSON(String defineJSON) {
		this.defineJSON = defineJSON.trim();
	}


	@Column(name = "c_datasrcid")
	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof BizView) {
			return getId().equals(((BizView)obj).getId());
		} else
			return false;
	}

}
