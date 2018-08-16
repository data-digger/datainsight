package com.datadigger.datainsight.report;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.datadigger.datainsight.repository.DataSourceRepository;
import com.datadigger.datainsight.type.DBType;
import com.datadigger.datainsight.util.StringUtil;
import com.datadigger.datainsight.bean.CellData;
import com.datadigger.datainsight.bean.GridData;
import com.datadigger.datainsight.domain.DataSource;
import com.datadigger.datainsight.domain.Parameter;
import com.datadigger.datainsight.util.ValueType;

import com.datadigger.datainsight.exception.DataDiggerErrorCode;
import com.datadigger.datainsight.exception.DataDiggerException;

public class SimpleReportBO implements Serializable {
	@Autowired
	private DataSourceRepository dastaSourceRespository;
	

	public String getValue(boolean isDefault) {
		if(isDefault) {
			return "default";
		} else {
			return "";
		}		
	};
	public ValueType getType() {
		return ValueType.STRING;
	};
	public DataSource getDataSource(String dataSourceId) {
		return dastaSourceRespository.findOne(dataSourceId);
	}
	public DBType getDBType(String dbType) {
		return DBType.MYSQL;
	}
	public boolean isMultSelect() {
		return false;
	}
	public ParamValue getParamValue(String paramId,boolean isDefault) {
		ParamValue v = null;
		if (StringUtil.isNullOrEmpty(getValue(isDefault))) {
			// TODO:�Ƿ�nullת��Ϊ''�����ǽ������Ϊѡ��
//			if (report.getBusinessView().isProcedureView() && ValueType.STRING.equals(getType())) {
//				JSONObject json = new JSONObject(getComponentDefine());
//				if (json.has("noNull") && "true".equalsIgnoreCase(json.getString("noNull"))) {
//					v = new StrValue("''");
//				}
//			}
			return v;
		}

		switch (getType()) {
			case STRING: {
				if (StringUtil.isNullOrEmpty(getValue(isDefault)))
					throw new DataDiggerException(DataDiggerErrorCode.EXPR_PARAME_NULL_VALUE);

				if (isMultSelect()) {
					v = new MultiValue(getValue(isDefault), getType());
				} 
				else {
					if (getValue(isDefault).trim().startsWith("'"))
						v = new StrValue(getValue(isDefault));
					else
						v = new StrValue("'" + getValue(isDefault) + "'");
				}
				break;
			}

			case INTEGER:
			case DOUBLE: {
				if (StringUtil.isNullOrEmpty(getValue(isDefault)))
					throw new DataDiggerException(DataDiggerErrorCode.EXPR_PARAME_NULL_VALUE);

				if (isMultSelect()) {
					v = new MultiValue(getValue(isDefault), getType());
				} 
				else {
					try {
						v = new NumValue(Double.valueOf(getValue(isDefault)), getType());
					} catch (NumberFormatException e) {
						throw new DataDiggerException(DataDiggerErrorCode.PARAM_TYPE_ERROR)
								.setDetail(getType().name() + ":" + getValue(isDefault));
					}
				}
				break;
			}
			case DATE:
			case TIME:
			case DATETIME:
				DataSource ds = getDataSource("");
				DBType dbType = ds == null ? null : getDBType(ds.getDriverType());
				//����ǹ̻��Ĳ�����־�����������ʹ�ȡ��ǰ֪ʶ�������
//				if (ds == null || getName().equals("SYSTEM������־��ʼʱ��")
//						|| getName().equals("SYSTEM������־����ʱ��")) {
//					dbType = FreeQueryModule.getInstance().getDaoModule().getRepository()
//							.getDatabaseType();
//				}
				v = new DateValue(getValue(isDefault), getType(), dbType);
				break;
			default:
				v = new StrValue(getValue(isDefault));
		}
		return v;
	}

	public Map<String,ParamValue> getParamValues(List<String> paramIds,boolean isDefault){
		Map<String,ParamValue> pvs = new HashMap<>();
		for(int i=0;i<paramIds.size();i++) {
			String pid = paramIds.get(i);
			ParamValue pv = getParamValue(pid,isDefault);
			pvs.put(pid, pv);
		}
		return pvs;
	}
	public BusinessViewState buildViewState(List<String> paramIds,boolean isDefault) {
		BusinessViewState state = new BusinessViewState();
		state.setParamValues(getParamValues(paramIds,isDefault));
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
		return state;
	}


}
