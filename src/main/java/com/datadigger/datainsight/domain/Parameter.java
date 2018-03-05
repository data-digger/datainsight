package com.datadigger.datainsight.domain;

import java.io.Serializable;
import java.sql.Types;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.*;

import com.datadigger.datainsight.param.ParamValue;
import com.datadigger.datainsight.type.ParamExprType;
import com.datadigger.datainsight.type.ParamType;
import com.datadigger.datainsight.util.StringUtil;
import com.datadigger.datainsight.util.ValueType;


@Entity
@Table(name = "t_parameter")
public class Parameter implements Serializable{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4079564656142498445L;

	private String id;

	private String name;
	
	private String alias;

	private ValueType type;

	private String desc;

	private ParamExprType standByType;

	private String standByValue;

	private ParamExprType defaultType;

	private String defaultValue;

	private boolean display;

	private boolean manual;

	//private ComponentType componentType;

	private String componentDefine;
	
	private DataSource dataSource;
	
	private ParamValue paramValue;
	
	private ParamExprType rootType;
	
	private String rootValue;
	
	//private DropDownTreeExtendType treeExtType;

	private String checkedInStandby;
	
	private boolean multSelect;
	
	@Id
	@Column(name = "c_paramid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "c_paramname", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "c_paramalias")
	public String getAlias() {
		if (StringUtil.isNullOrEmpty(alias))
			return getName();
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Column(name = "c_paramtype")
	@Type(type = "org.hibernate.type.EnumType", parameters = {
			@org.hibernate.annotations.Parameter(name = org.hibernate.type.EnumType.ENUM, value = "bof.util.ValueType"),
			@org.hibernate.annotations.Parameter(name = org.hibernate.type.EnumType.TYPE, value = Types.VARCHAR + "") }) 	
	public ValueType getType() {
		return type;
	}

	public void setType(ValueType type) {
		this.type = type;
	}

	@Column(name = "c_paramdesc")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "c_defaulttype")
	@Type(type = "org.hibernate.type.EnumType", parameters = {
			@org.hibernate.annotations.Parameter(name = org.hibernate.type.EnumType.ENUM, value = "com.freequery.util.ParamExprType"),
			@org.hibernate.annotations.Parameter(name = org.hibernate.type.EnumType.TYPE, value = Types.VARCHAR + "") }) 	
	public ParamExprType getDefaultType() {
		return defaultType;
	}

	public void setDefaultType(ParamExprType defaultType) {
		this.defaultType = defaultType;
	}

	@Column(name = "c_default")
	@Type(type = "bof.repository.StringClobType")
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Column(name = "c_standbytype")
	@Type(type = "org.hibernate.type.EnumType", parameters = {
			@org.hibernate.annotations.Parameter(name = org.hibernate.type.EnumType.ENUM, value = "com.freequery.util.ParamExprType"),
			@org.hibernate.annotations.Parameter(name = org.hibernate.type.EnumType.TYPE, value = Types.VARCHAR + "") }) 	
	public ParamExprType getStandByType() {
		return standByType;
	}

	public void setStandByType(ParamExprType standByType) {
		this.standByType = standByType;
	}

	@Column(name = "c_standby")
	@Type(type = "bof.repository.StringClobType")
	public String getStandByValue() {
		return standByValue;
	}

	public void setStandByValue(String standByValue) {
		this.standByValue = standByValue;
	}

	@Column(name = "c_isdisplay")
	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	@Column(name = "c_multselect")
	public boolean isMultSelect() {
		return multSelect;
	}

	public void setMultSelect(boolean multSelect) {
		this.multSelect = multSelect;
	}

	@Column(name = "c_ismanual")
	public boolean isManual() {
		return manual;
	}

	public void setManual(boolean manual) {
		this.manual = manual;
	}



	@Column(name = "c_compdefine")
	@Type(type = "bof.repository.StringClobType")
	public String getComponentDefine() {
		//��Ϊ��ǰ�и��汾�����������Я���С�\u0000\u0000\u0000\u0000\u0000\u0000���ַ�����json�ַ������ͳ���
		componentDefine = removeUnicodeSpace(componentDefine);
		return componentDefine;
	}

	public void setComponentDefine(String componentDefine) {
		this.componentDefine = componentDefine;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "c_datasrcid")
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Transient
	public ParamValue getParamValue() {
		return paramValue;
	}

	public void setParamValue(ParamValue paramValue) {
		this.paramValue = paramValue;
	}
	
	public int hashCode() {
		return this.getId().hashCode();
	}
	
	public boolean equals(Object obj){
		if (obj instanceof Parameter){
			return this.getId().equals(((Parameter)obj).getId());
		}
		return false;
	}

	@Column(name = "c_roottype")
	@Type(type = "org.hibernate.type.EnumType", parameters = {
			@org.hibernate.annotations.Parameter(name = org.hibernate.type.EnumType.ENUM, value = "com.freequery.util.ParamExprType"),
			@org.hibernate.annotations.Parameter(name = org.hibernate.type.EnumType.TYPE, value = Types.VARCHAR + "") }) 	
	public ParamExprType getRootType() {
		return rootType;
	}

	public void setRootType(ParamExprType rootType) {
		this.rootType = rootType;
	}

	@Column(name = "c_root")
	@Type(type = "bof.repository.StringClobType")
	public String getRootValue() {
		return rootValue;
	}

	public void setRootValue(String rootValue) {
		this.rootValue = rootValue;
	}


	@Column(name = "c_checkinstandby")
	public String getCheckedInStandby() {
		return checkedInStandby;
	}

	public void setCheckedInStandby(String checkedInStandby) {
		this.checkedInStandby = checkedInStandby;
	}
	
	/**
	 * �Ƿ�ϲ��������
	 * Ĭ�ϵ�����Ǻϲ�
	 * @return true  �ϲ�
	 *         false ���ϲ�
	 */
	public boolean checkCombinPara()
	{
		int position = this.componentDefine.indexOf("combinPara");
		if (position<0)                                             
			return true;
		else
		{
			char charCheck = this.componentDefine.charAt(position+12);
			return (charCheck == 't');
		}
	}

	@Transient
	public ParamType getParamType() {
		return ParamType.OLTP;
	}

	public void setValue(String value, String displayValue) {
	}

	private String removeUnicodeSpace(String str){
		if(str == null)
			return "";
		
		StringBuilder sb = new StringBuilder();
		int lastIndex = 0;
		int currIndex;
		String s1 = "\\u0000";
		while ((currIndex = str.indexOf(s1, lastIndex)) != -1) {
			sb.append(str.substring(lastIndex, currIndex));
			lastIndex = currIndex + s1.length();
		}
		sb.append(str.substring(lastIndex));
		
		return sb.toString();
	}

}

