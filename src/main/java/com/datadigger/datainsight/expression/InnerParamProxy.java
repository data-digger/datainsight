package com.datadigger.datainsight.expression;

import java.io.Serializable;
import java.util.Map;

import com.datadigger.datainsight.exception.DataDiggerException;
import com.datadigger.datainsight.param.ParamValue;
import com.datadigger.datainsight.query.SQLPart;
import com.datadigger.datainsight.exception.DataDiggerErrorCode;


public class InnerParamProxy implements IExpressionItem,Serializable {
	private String innerParamID; 
	private String outputParamID = null; 
	private boolean ignorable;
	Map<String, ParamValue> parametersValue;

	public String toString() {
		return getSQLPart().getSqlStr();
	}

	public InnerParamProxy(String innerParamID) {
		this.innerParamID = innerParamID;
	}

	public void setParametersValue(Map<String, ParamValue> parametersValue) {
		this.parametersValue = parametersValue;
	}

	public String getParamId() {
		return innerParamID;
	}

	public SQLPart getSQLPart() {
		if (parametersValue == null) {
			throw new DataDiggerException(DataDiggerErrorCode.EXPR_PARAME_NULL_VALUE).setDetail(" parameter value is null");
		}

		if (outputParamID != null) {
			if (parametersValue.get(outputParamID) == null)
				throw new DataDiggerException(DataDiggerErrorCode.EXPR_PARAME_NULL_VALUE)
						.setDetail("parameter (ID:" + outputParamID + ") is null");
			else
				return SQLPartBuilder.getSQLPart(parametersValue.get(outputParamID));
		}

		if (parametersValue.get(innerParamID) == null) {
			throw new DataDiggerException(DataDiggerErrorCode.EXPR_PARAME_NULL_VALUE).setDetail("parameter (ID:"
					+ innerParamID + ") value is null");
		} else {
			return SQLPartBuilder.getSQLPart(parametersValue.get(innerParamID));
		}
	}

	public void setOutputParamID(String outputParamID) {

		this.outputParamID = outputParamID;
	}

	public boolean isIgnorable() {
		return ignorable;
	}

	public void setIgnorable(boolean ignorable) {
		this.ignorable = ignorable;
	}

}
