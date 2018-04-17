package com.datadigger.datainsight.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "bizview_columns")
public class BizViewColumn implements  Serializable {
	
	private static final long serialVersionUID = 3518942739384743216L;

	private String id;
	
	private String bizViewId;

	private String columnName;
	
	private String columnAlias;

	private String columnType;
	
	private int groupby;
	
	private int filterable;
	
	private int countDistinct;
	
	private int sum;
	
	private int min;
	
	private int max;
	
	private int category;  //字段分类：0代表原始字段，1代表计算字段， 2代表聚合函数
	
	private String expression;
	
	@Id
	@Column(name = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	@Column(name = "bizview_id")
	public String getBizViewId() {
		return bizViewId;
	}

	public void setBizViewId(String bizViewId) {
		this.bizViewId = bizViewId;
	}
	
	@Column(name = "column_name")
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	@Column(name = "column_alias")
	public String getColumnAlias() {
		return columnAlias;
	}

	public void setColumnAlias(String columnAlias) {
		this.columnAlias = columnAlias;
	}
	
	@Column(name = "column_type")
	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	
	@Column(name = "groupby")
	public int getGroupby() {
		return groupby;
	}

	public void setGroupby(int groupby) {
		this.groupby = groupby;
	}
	
	@Column(name = "filterable")
	public int getFilterable() {
		return filterable;
	}

	public void setFilterable(int filterable) {
		this.filterable = filterable;
	}
	
	@Column(name = "count_distinct")
	public int getCountDistinct() {
		return countDistinct;
	}

	public void setCountDistinct(int countDistinct) {
		this.countDistinct = countDistinct;
	}
	
	@Column(name = "sum")
	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}
	
	@Column(name = "min")
	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}
	
	@Column(name = "max")
	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
	
	@Column(name = "category")
	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}
	@Column(name = "expression")
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public boolean equals(Object obj) {
		if(obj instanceof BizViewColumn) {
			return getId().equals(((BizViewColumn)obj).getId());
		} else
			return false;
	}

}
