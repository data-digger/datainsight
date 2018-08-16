package com.datadigger.datainsight.report;

import java.util.*;

//import bof.util.DBType;

import com.datadigger.datainsight.param.DateValue;
import com.datadigger.datainsight.param.MultiValue;
import com.datadigger.datainsight.param.NumValue;
import com.datadigger.datainsight.param.ParamValue;
import com.datadigger.datainsight.param.StrValue;
//import com.freequery.metadata.OutputParameter;

public class BusinessViewState {
	private Map<String, ParamValue> paramValues;
	
	private int rowsPerPage ;
	//private List<ReportField> outputFields;
	//private List<OrderBy> orderBys;
	//private SimpleFilter simpleFilter;
	//private boolean distinct;
	//private ReportField distinctField;
	//private DBType dbType;
	private String charset = null; 
	//private SubtotalSetting subtotal;
	
	//private List<OutputParameter> outputParameters;

	//private ReportField transmitRelatedField;

	private String transmitTempTableName;
	
	private int retrieveCountSettingValue;
	
	private boolean isRawSQL;
	
	private String cacheIdentity = null;
//	public String getCacheIdentity() {
//		if (cacheIdentity == null){
//			StringBuffer buf = new StringBuffer();
//			List<String> paramKeys = new ArrayList<String>(paramValues.keySet());
//			Collections.sort(paramKeys);
//			for(int i=0;i<paramKeys.size();i++){
//				buf.append(paramKeys.get(i)).append(":").append(paramValues.get(paramKeys.get(i))).append(";");
//			}
//			buf.append(rowsPerPage);
//			for(int i=0;i<outputFields.size();i++){
//				buf.append(outputFields.get(i).getCacheIdentity()).append(";");
//			}
//			if (distinctField!=null)
//				buf.append(distinctField.getCacheIdentity()).append(";");
//			//buf.append(dbType).append(";");
//			buf.append(charset).append(";");
//
//			if (subtotal!=null)
//				buf.append(subtotal.getCacheIdentity()).append(";");
//			//buf.append(outputParameters.size()).append(";");
//			
//			if (transmitRelatedField!=null)
//				buf.append(transmitRelatedField.getCacheIdentity()).append(";");
//			buf.append(transmitTempTableName).append(";").append(retrieveCountSettingValue).append(isRawSQL);
//			
//			cacheIdentity = buf.toString();
//
//		}
//		return cacheIdentity;
//	}
	
	@Override
	public boolean equals(Object obj){
 		if (obj!=null && (obj instanceof BusinessViewState )){
			BusinessViewState state = (BusinessViewState)obj;
			if (equalTo(paramValues,state.getParamValues()) &&
				equalTo(rowsPerPage ,state.getRowsPerPage()) &&
				
				//(this.isRawSQL || equalTo(outputFields,state.getOutputFields())) &&
				
				//equalTo(orderBys,state.getOrderBys()) &&
				//equalTo(simpleFilter,state.getSimpleFilter()) &&
				//equalTo(distinctField,state.distinctField) &&
				//equalTo(dbType,state.getDbType()) &&
				//equalTo(subtotal,state.getSubtotal()) &&
				//equalTo(outputParameters.size(),state.getOutputParameters().size()) &&
				//equalTo(transmitRelatedField,state.getTransmitTempTableName()) &&
				equalTo(transmitTempTableName,state.getTransmitTempTableName()) &&
				equalTo(retrieveCountSettingValue ,state.getRetrieveCountSettingValue()))
				return true;
		}
		return false;
	}
	
	public boolean equalTo(Object a,Object b){
		if (a==null && b==null)
			return true;
		else if(a==null || b==null)
			return false;
		
		if (a==b)
			return true;
		
		return a.equals(b);
		
	}
	
//	public List<OrderBy> getOrderBys() {
//		return orderBys;
//	}
//	public void setOrderBys(List<OrderBy> orderBys) {
//		this.orderBys = orderBys;
//	}
//	public List<ReportField> getOutputFields() {
//		return outputFields;
//	}
//	public void setOutputFields(List<ReportField> outputFields) {
//		if (outputFields==null){
//			this.outputFields = null;
//			return ;
//		}
//		
//		this.outputFields = new ArrayList<ReportField>();
//		for(ReportField f:outputFields){
//			this.outputFields.add(f.clone());
//		}
//	}
	public Map<String, ParamValue> getParamValues() {
		return paramValues;
	}
	public void setParamValues(Map<String, ParamValue> paramValues) {
		if(paramValues == null){
			this.paramValues = null;
			return ;
		}
			
		Map<String, ParamValue> tmpParamValues = new HashMap<String, ParamValue>();
		for(String key: paramValues.keySet()){
			ParamValue paramValue = paramValues.get(key);
			try {
				tmpParamValues.put(key, paramValue == null ? null : paramValue.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		this.paramValues = tmpParamValues;
	}
	public int getRowsPerPage() {
		return rowsPerPage;
	}
	public void setRowsPerPage(int rowsPerPage) {
		if(rowsPerPage<=0)
			rowsPerPage = 1;
		this.rowsPerPage = rowsPerPage;
	}
//	public SimpleFilter getSimpleFilter() {
//		return simpleFilter;
//	}
//	public void setSimpleFilter(SimpleFilter simpleFilter) {
//		this.simpleFilter = (simpleFilter != null)?simpleFilter.clone():null;
//	}

	public void makeAddedCondition() {
	}
//
//	public List<OutputParameter> getOutputParameters() {
//		if(outputParameters == null)
//			return new ArrayList<OutputParameter>();
//		return outputParameters;
//	}
//	public void setOutputParameters(
//			List<OutputParameter> outputParameters) {
//		this.outputParameters = outputParameters;
//	}
//
//	public DBType getDbType() {
//		return dbType;
//	}
//	public void setDbType(DBType dbType) {
//		this.dbType = dbType;
//	}
	public String getCharset(){
		return charset;
	}
	public void setCharset(String charset){
		this.charset = charset;
	}
//	public ReportField getDistinctField() {
//		return distinctField;
//	}
//	public void setDistinctField(ReportField distinctField) {
//		//this.distinctField = (distinctField != null)?distinctField.clone():null;
//		this.distinctField = distinctField;
//	}
//	public SubtotalSetting getSubtotal() {
//		return subtotal;
//	}
//	public void setSubtotal(SubtotalSetting subtotal) {
//		this.subtotal = (subtotal != null)?subtotal.clone():null;
//	}

//	public void setTransmitRelatedField(ReportField transmitRelatedField) {
//		this.transmitRelatedField = (transmitRelatedField != null)?transmitRelatedField.clone():null;
//		//this.transmitRelatedField = transmitRelatedField;
//	}

	public void setTransmitTempTableName(String transmitTempTableName) {
		this.transmitTempTableName = transmitTempTableName;
	}

	public String getTransmitTempTableName() {
		return transmitTempTableName;
	}

//	public ReportField getTransmitRelatedField() {
//		return transmitRelatedField;
//	}

	public int getRetrieveCountSettingValue() {
		return retrieveCountSettingValue;
	}

	public void setRetrieveCountSettingValue(int retrieveCountSettingValue) {
		this.retrieveCountSettingValue = retrieveCountSettingValue;
	}

	public boolean isRawSQL() {
		return isRawSQL;
	}

	public void setRawSQL(boolean isRawSQL) {
		this.isRawSQL = isRawSQL;
	}
	
//	public ReportField getReportFieldById(String fieldId) {
//		if (outputFields != null && fieldId != null) {
//			for (ReportField field : outputFields) {
//				if (fieldId.equals(field.getId()))
//					return field;
//			}
//		}
//		return null;
//	}

}
