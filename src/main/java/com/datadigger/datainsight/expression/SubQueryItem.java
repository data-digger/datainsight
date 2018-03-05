package com.datadigger.datainsight.expression;

import com.datadigger.datainsight.query.SQLPart;

public class SubQueryItem implements IExpressionItem {
	//private AbstractQuery query;
	private String queryId;
	
	
	public String getQueryId(){
		return queryId;
	}
	
	private SubQueryItem(){
		
	}
	
	public SubQueryItem(String queryId){
		this.queryId = queryId;
	}

	@Override
	public SQLPart getSQLPart() {
		// TODO Auto-generated method stub
		return null;
	}
	
//	public SubQueryItem(SubQueryNode node){
//		this.queryId = node.getSubQueryId();
//	}
//	
//	public AbstractQuery getQuery(){
//		if (query==null)
//			query = MetaDataRuntimeContext.getInstance().searchQuery(queryId);
//		return query;
//	}
//	
//	public SQLPart getSQLPart() {
//		
//		SQLPart rtn = getQuery().getExpression().getSQLPart();
//		 
//		rtn.setSqlStr("("+rtn.getSqlStr()+")");
//		
//		rtn.setType(SQLPartType.SQLSUBQUERY);
//		return rtn;
//	}
	
	
	
}
