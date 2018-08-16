package com.datadigger.datainsight.param;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.datadigger.datainsight.expression.Expression;
import com.datadigger.datainsight.expression.IExpressionItem;
import com.datadigger.datainsight.param.DateValue;
import com.datadigger.datainsight.param.MultiValue;
import com.datadigger.datainsight.param.NumValue;
import com.datadigger.datainsight.param.ParamValue;
import com.datadigger.datainsight.param.StrValue;
import com.datadigger.datainsight.query.SQLExecutor;
import com.datadigger.datainsight.repository.DataSourceRepository;
import com.datadigger.datainsight.type.DBType;
import com.datadigger.datainsight.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datadigger.datainsight.bean.CellData;
import com.datadigger.datainsight.bean.GridData;
import com.datadigger.datainsight.bean.ListParameter;
import com.datadigger.datainsight.domain.DataSource;
import com.datadigger.datainsight.domain.Parameter;
import com.datadigger.datainsight.domain.ParameterData;
import com.datadigger.datainsight.util.ValueType;

import com.datadigger.datainsight.exception.DataDiggerErrorCode;
import com.datadigger.datainsight.exception.DataDiggerException;

public class ParamHandler implements Serializable {
	private DataSourceRepository dastaSourceRespository;
	private List<Parameter> parameters;
	
	public ParamHandler() {

   }
	
    public ParamHandler(DataSourceRepository dsr, List<Parameter> parameters) {
    	    this.dastaSourceRespository = dsr;
    		this.parameters = parameters;
    }
	public String getValue(Parameter param,boolean isDefault,JSONObject valueObject) {
		String value = "";
		JSONObject o = (JSONObject) JSON.parse(param.getDefineJSON());
		String componetType = o.getString("componenttype");
		if(isDefault) {
			if(componetType.equals("date")) {
				Date currentTime = new Date();
				String format = o.getString("formattype");
				SimpleDateFormat formatter = new SimpleDateFormat(format);
				String dateString = formatter.format(currentTime);
				value = dateString;
				
			} else if(componetType.equals("list")) {
				JSONObject defaultDefine = o.getJSONObject("defalutDefine");
				String dk = defaultDefine.getString("key");
				//String dv = defaultDefine.getString("value");
				value = dk;
			} 
		} else {
			if(componetType.equals("date")) {
				
			} else if(componetType.equals("list")) {
				if(valueObject != null) {
					value = valueObject.getString(param.getId());
				} else {
					value = "";
				}
				
			} 
			
		}
		return value;
	};
	public ValueType getType(Parameter param) {
		String valueType = "String";
		JSONObject o = (JSONObject) JSON.parse(param.getDefineJSON());
		valueType = o.getString("valuetype");
		switch(valueType){
		case "String":
			return ValueType.STRING;
		default:
			return ValueType.STRING;
		}
	};
	public DataSource getDataSource(String dataSourceId) {
		return this.dastaSourceRespository.findOne(dataSourceId);
	}
	public DBType getDBType(String dbType) {
		return DBType.MYSQL;
	}
	public boolean isMultSelect(Parameter param) {
		return false;
	}
	public ParamValue getParamValue(Parameter param,boolean isDefault,JSONObject valueObject ) {
		ParamValue v = null;
		if (StringUtil.isNullOrEmpty(getValue(param,isDefault,valueObject))) {
			// TODO:�Ƿ�nullת��Ϊ''�����ǽ������Ϊѡ��
//			if (report.getBusinessView().isProcedureView() && ValueType.STRING.equals(getType())) {
//				JSONObject json = new JSONObject(getComponentDefine());
//				if (json.has("noNull") && "true".equalsIgnoreCase(json.getString("noNull"))) {
//					v = new StrValue("''");
//				}
//			}
			return v;
		}

		switch (getType(param)) {
			case STRING: {
				if (StringUtil.isNullOrEmpty(getValue(param,isDefault,valueObject)))
					throw new DataDiggerException(DataDiggerErrorCode.EXPR_PARAME_NULL_VALUE);

				if (isMultSelect(param)) {
					v = new MultiValue(getValue(param,isDefault,valueObject), getType(param));
				} 
				else {
					if (getValue(param,isDefault,valueObject).trim().startsWith("'"))
						v = new StrValue(getValue(param,isDefault,valueObject));
					else
						v = new StrValue("'" + getValue(param,isDefault,valueObject) + "'");
				}
				break;
			}

			case INTEGER:
			case DOUBLE: {
				if (StringUtil.isNullOrEmpty(getValue(param,isDefault,valueObject)))
					throw new DataDiggerException(DataDiggerErrorCode.EXPR_PARAME_NULL_VALUE);

				if (isMultSelect(param)) {
					v = new MultiValue(getValue(param,isDefault,valueObject), getType(param));
				} 
				else {
					try {
						v = new NumValue(Double.valueOf(getValue(param,isDefault,valueObject)), getType(param));
					} catch (NumberFormatException e) {
						throw new DataDiggerException(DataDiggerErrorCode.PARAM_TYPE_ERROR)
								.setDetail(getType(param).name() + ":" + getValue(param,isDefault,valueObject));
					}
				}
				break;
			}
			case DATE:
			case TIME:
			case DATETIME:
				//DataSource ds = getDataSource("");
				//DBType dbType = ds == null ? null : getDBType(ds.getDriverType());
				//DataSource ds = getDataSource("");
				DBType dbType = getDBType("");
				//����ǹ̻��Ĳ�����־�����������ʹ�ȡ��ǰ֪ʶ�������
//				if (ds == null || getName().equals("SYSTEM������־��ʼʱ��")
//						|| getName().equals("SYSTEM������־����ʱ��")) {
//					dbType = FreeQueryModule.getInstance().getDaoModule().getRepository()
//							.getDatabaseType();
//				}
				v = new DateValue(getValue(param,isDefault,valueObject), getType(param), dbType);
				break;
			default:
				v = new StrValue(getValue(param,isDefault,valueObject));
		}
		return v;
	}
	public List<ListParameter> buildSqlStandBy(DataSource ds,String sql,List<ListParameter> standByValue){
		GridData gd = SQLExecutor.execute(ds, sql);
		int rows = gd.getRowsCount();
		for(int i=0; i<rows; i++) {
			ListParameter lp = new ListParameter();
			String key = gd.get(i, 0).getDisplayValue();
			String value = gd.get(i, 1).getDisplayValue();
			lp.setKey(key);
			lp.setValue(value);
			standByValue.add(lp);
		};
		return standByValue;
	}
	
	public List<ListParameter> buildListStandBy(JSONArray values,List<ListParameter> standByValue) {
		for(int i=0; i<values.size(); i++) {
			ListParameter lp = new ListParameter();
			JSONObject value = values.getJSONObject(i);
			lp.setKey(value.getString("key"));
			lp.setValue(value.getString("value"));
			standByValue.add(lp);
		};
		return standByValue;
	}
	public List<ListParameter> getStandByValue(Parameter param){
		JSONObject o = (JSONObject) JSON.parse(param.getDefineJSON());
		JSONObject standByDefine = o.getJSONObject("standbyDefine");
		String standBySource = standByDefine.getString("valueSource");
		List<ListParameter> standByValue = new ArrayList<>();
		switch (standBySource) {
		case "SQL":
			String dataSourceId = standByDefine.getString("dataSourceID");
	    		DataSource ds = getDataSource(dataSourceId) ;
			String sql = standByDefine.getString("values");
			buildSqlStandBy(ds,sql,standByValue);
			break;
		case "static":
			JSONArray values = standByDefine.getJSONArray("values");
			buildListStandBy(values,standByValue);
			break;
   		} 
		return standByValue;
		
	}
	
	public Map<String,List<ListParameter>> getStandByValues(){
		Map<String,List<ListParameter>> standByValues = new HashMap<>();
		for(int i = 0; i < parameters.size(); i++) {
			String key = parameters.get(i).getId();
			List<ListParameter> value = getStandByValue(parameters.get(i));
			standByValues.put(key, value);
		}
		return standByValues;
	}

	public Map<String,ParamValue> getDefaultParamValues(){
		Map<String,ParamValue> pvs = new HashMap<>();
		for(int i=0;i<parameters.size();i++) {
			Parameter param = parameters.get(i);
			ParamValue pv = getParamValue(param,true,null);
			pvs.put(param.getId(), pv);
		}
		return pvs;
	}
	public Map<String,ParamValue> getSelectedParamValues(JSONObject valueObject){
		Map<String,ParamValue> pvs = new HashMap<>();
		for(int i=0;i<parameters.size();i++) {
			Parameter param = parameters.get(i);
			ParamValue pv = getParamValue(param,false,valueObject);
			pvs.put(param.getId(), pv);
		}
		return pvs;
	}
	
	
	public Map<String,ParameterData> getParameterDatas(){
		//Expression exp = new Expression(expStr);
		//List<String> paramIds = exp.getParameterIDsByList();
		Map<String,ParameterData> pds = new HashMap<>();
		if(parameters.size() > 0) {
			Map<String,List<ListParameter>> standByValues = getStandByValues();
			for(int i = 0; i < parameters.size(); i++) {
				Parameter pd = parameters.get(i);
				ParameterData pdd = new ParameterData(pd);
				pdd.setStandByList(standByValues.get(pd.getId()));
				pds.put(pd.getId(), pdd);
			}
		
		}
		return pds;
	}
	
	
//	public BusinessViewState buildViewState(List<String> paramIds,boolean isDefault) {
//		BusinessViewState state = new BusinessViewState();
//		state.setParamValues(getParamValues(paramIds,isDefault));
//		if(getBusinessView().getId().equals("I2c94902b163282dd0116328ec4d90037") || 
//				getBusinessView().getId().equals("SYSTEM��Դ��˲�ѯ"))
//			state.setDbType(FreeQueryModule.getInstance().getDaoModule().getRepository().getDatabaseType());
//		else if(getBusinessView().getDataSource() != null) {
//			state.setDbType(getBusinessView().getDataSource().getDriverType());
//			state.setCharset(getBusinessView().getDataSource().getDbCharset());
//		}
//		state.setSubtotal(subtotalSetting);
////		state.setInitiatingReportId(getId());
//		state.setTransmitRelatedField(transmitRelatedField);
//		state.setTransmitTempTableName(transmitTempTableName);
//		state.setRetrieveCountSettingValue(getRetrieveCountSettingValue());
//		return state;
//	}


}
