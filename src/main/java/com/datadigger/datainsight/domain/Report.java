package com.datadigger.datainsight.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;


/**
 * 
 * defineJSON:{
 *    header:{conditions:[{type:"",objid:""}],
 *    content:{
 *        [solt:{
 *          id:"",
 *          name:"",
 *          layout:{x:1,y:1,h:1,w:1,i:1}
 *          tabs:[{title:"",objtype:"",objid:""}]
 *          }]
 *    },
 *    tail:{}
 *    
 * }
 * 
 * @author lvlz
 *
 */

@Entity
@Table(name = "t_report")
public class Report implements  Serializable {
	
	private static final long serialVersionUID = -2355777943571673147L;

	private String id;

	private String name;
	
	private String alias;

	private String desc;

	private String defineJSON;
	
	
	
	@Id
	@Column(name = "c_reportid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "c_reportname", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "c_reportalias")
	public String getAlias() {
		if (alias==null)
			return getName();
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Column(name = "c_reportdesc")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "c_define")
	public String getDefineJSON() {
		if(defineJSON == null)
			return defineJSON;
		return defineJSON.trim();
	}

	public void setDefineJSON(String defineJSON) {
		this.defineJSON = defineJSON.trim();
	}


	public boolean equals(Object obj) {
		if(obj instanceof Report) {
			return getId().equals(((Report)obj).getId());
		} else
			return false;
	}
	
	public String toJSON(){
		return JSON.toJSONString(this, true);
	}
	public String toString(){
		return "id:" + id + "\n"
			   +"name:" + name + "\n"
			   +"alias:" + alias + "\n"
			   +"desc:" + desc + "\n"
			   +"defineJSON:" + defineJSON + "\n";
	}
	

}
