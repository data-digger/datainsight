package com.datadigger.datainsight.expression;

import com.datadigger.datainsight.query.SQLPart;

public class UserPropertyItem implements IExpressionItem {
	private String propName;
	
	public SQLPart getSQLPart() {		
//		UserPropertyBO prop = FreeQueryDAOFactory.getUserPropertyDAO().getPropertyByName(propName);
//		if(prop == null)
//			throw new BOFException(FreeQueryErrorCode.USER_PROPERTY_NOT_EXIST).setDetail(propName);
//		String sqlPart = prop.getValue();
//		SQLPart rtn = new SQLPart(SQLPartType.SQLSTR,sqlPart);
//		return rtn;
		return null;
	}

	public String getPropertyName(){
		return propName;
	}
	
	public UserPropertyItem(String propName){
		this.propName = propName;
	}
	
//	public UserPropertyItem(UserPropNode node){
//		this.propName = node.getPropName();
//	}
	
}
