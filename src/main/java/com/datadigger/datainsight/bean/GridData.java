package com.datadigger.datainsight.bean;

import java.io.Serializable;
import java.util.*;


public class GridData implements Serializable {
//	protected List<ReportField> headers;
//
//	protected List<OrderBy> orderBys;

	protected List<String> stringHeaders;
	
	protected List<String> columsType;
	
	protected List<String> tableNames;

	protected List<List<CellData>> data;

	protected Map<Integer, String> warnings = new LinkedHashMap<Integer, String>();

	private int totalRowsCount;
	
	public GridData() {
	}

	public CellData get(int row, int column) {
		return data.get(row).get(column);
	}

	public int getRowsCount() {
		return data.size();
	}

	public int getColumnsCount() {
		return getStringHeaders().size();
//		return data.size() == 0 ? 0 : data.get(0).size();
	}

//	public List<ReportField> getHeaders() {
//		return headers;
//	}
//
//	public void setHeaders(List<ReportField> headers) {
//		this.headers = headers;
//	}

	public List<List<CellData>> getData() {
		return data;
	}

	public void setData(List<List<CellData>> data) {
		this.data = data;
	}

	public List<String> getStringHeaders() {
//		if (null == stringHeaders) {
//			stringHeaders = new ArrayList<String>();
//			List<ReportField> headers = getHeaders();
//			for (ReportField field : headers) {
//				stringHeaders.add(field.getAlias());
//			}
//		}
		return stringHeaders;
	}

	public void setStringHeaders(List<String> stringHeaders) {
		this.stringHeaders = stringHeaders;
	}
	
	public List<String> getColumsType() {
		return columsType;
	}

	public void setColumsType(List<String> columsType) {
		this.columsType = columsType;
	}
	
	public List<String> getTableNames() {
		return tableNames;
	}

	public void setTableNames(List<String> tableNames) {
		this.tableNames = tableNames;
	}

	public void setWarning(int rowIndex, String warnId) {
		String old = warnings.put(rowIndex, warnId);
		if (old != null)
			warnings.put(rowIndex, old + "," + warnId);
	}

	public Map<Integer, String> getWarnings() {
		return warnings;
	}

//	public List<OrderBy> getOrderBys() {
//		return orderBys;
//	}
//
//	public void setOrderBys(List<OrderBy> orderBys) {
//		this.orderBys = orderBys;
//	}


	public GridData getSubGridData(int startRow,int rowCount)
	{
		return this.getSubGridData(startRow, Math.min(rowCount, this.getRowsCount()), 0, this.getColumnsCount());
	}
	
	/**
	 */
	public GridData getSubGridData(int startRow,int rowCount,int startColumn,int columnCount )
	{
		if (startRow>rowCount||startRow+rowCount>this.getRowsCount())
			throw new IndexOutOfBoundsException("ROW ERROR:"+startRow+" "+rowCount+" "+this.getRowsCount());
		
		if (startColumn>columnCount||startColumn+columnCount>this.getColumnsCount())
			throw new IndexOutOfBoundsException("Column ERROR:"+startColumn+" "+columnCount+" "+this.getColumnsCount());
		
		GridData subGridData = new GridData();
		
//		if (this.getHeaders() != null) {
//			List<ReportField> subHeaders = new ArrayList<ReportField>();
//			for (int j = 0; j < columnCount; j++) {
//				subHeaders.add(this.getHeaders().get(startColumn + j));
//			}
//			subGridData.setHeaders(subHeaders);
//		}
		
//		if (this.getOrderBys() != null) {
//			List<OrderBy> subOrderBys = new ArrayList<OrderBy>();
//			for (int j = 0; j < columnCount; j++) {
//				subOrderBys.add(this.getOrderBys().get(startColumn + j));
//			}
//			subGridData.setOrderBys(subOrderBys);
//		}
		
		if (this.getStringHeaders() != null) {
			List<String> subStringHeaders = new ArrayList<String>();
			for (int j = 0; j < columnCount; j++) {
				subStringHeaders.add(this.getStringHeaders().get(
						startColumn + j));
			}
			subGridData.setStringHeaders(subStringHeaders);
		}
		
		List<List<CellData>> subData= new ArrayList<List<CellData>>();
		for (int i=0;i<rowCount;i++)
		{
			List<CellData> subRow= new ArrayList<CellData>();
			for (int j=0;j<columnCount;j++)
			{
				subRow.add(this.get(startRow+i, startColumn+j));
			}
			subData.add(subRow);
		}
		subGridData.setData(subData);
		return subGridData;
	}


	public int getTotalRowsCount() {
		return totalRowsCount;
	}

	public void setTotalRowsCount(int totalRowsCount) {
		this.totalRowsCount = totalRowsCount;
	}
}
