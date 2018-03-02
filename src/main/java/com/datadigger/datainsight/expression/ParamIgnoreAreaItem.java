package com.datadigger.datainsight.expression;

import java.util.Map;

import com.datadigger.datainsight.exception.DataDiggerException;

import com.datadigger.datainsight.exception.DataDiggerErrorCode;
import com.datadigger.datainsight.expression.ast.ParamIgnoreAreaNode;
import com.datadigger.datainsight.param.ParamValue;
import com.datadigger.datainsight.query.SQLPart;
import com.datadigger.datainsight.type.SQLPartType;


public class ParamIgnoreAreaItem implements IExpressionItem {
	private ParamIgnoreAreaNode node;
	private Expression expr;

	public ParamIgnoreAreaItem(ParamIgnoreAreaNode node) {
		this.node = node;
		this.expr = new Expression(this.node);
	}

	public SQLPart getSQLPart() {
		try{
			return this.expr.getSQLPart();
		}catch(DataDiggerException e){
			if (e.getErrorCode()==DataDiggerErrorCode.EXPR_PARAME_NULL_VALUE)
				return new SQLPart(SQLPartType.SQLUnknown," 1=1 ");
			else
				throw e;
		}
	}
	
	public Expression getExpression(){
		return this.expr;
	}
	
	public void setParametersValue(Map<String, ParamValue> paramValues) {
		expr.setParametersValue(paramValues);
	}
}
