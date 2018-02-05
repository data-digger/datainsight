package com.datadigger.datainsight.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 仪表盘实体对象
 * @author lvlz
 *
 */

@Entity
@Table(name = "t_chart")
public class Chart implements  Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4706224387346127438L;

	private String id;

	private String name;
	
	private String alias;
	
	private String type;

	private String desc;

	private String defineJSON;
	
	private String bizViewId;
	
	
	@Id
	@Column(name = "c_chartid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "c_chartname", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "c_chartalias")
	public String getAlias() {
		if (alias==null)
			return getName();
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	@Column(name = "c_charttype")
	public String getType() {
		
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "c_chartdesc")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "c_chartdefine")
	public String getDefineJSON() {
		if(defineJSON == null)
			return defineJSON;
		return defineJSON.trim();
	}

	public void setDefineJSON(String defineJSON) {
		if(defineJSON == null)
			defineJSON = "";
		this.defineJSON = defineJSON.trim();
	}


	@Column(name = "c_viewid")
	public String getBizViewId() {
		return bizViewId;
	}

	public void setBizViewId(String bizViewId) {
		this.bizViewId = bizViewId;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Chart) {
			return getId().equals(((Chart)obj).getId());
		} else
			return false;
	}

}
