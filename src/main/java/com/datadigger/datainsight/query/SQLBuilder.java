package com.datadigger.datainsight.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.datadigger.datainsight.expression.Expression;
import com.datadigger.datainsight.util.StringUtil;


public class SQLBuilder {
	private static final Logger log = Logger.getLogger(SQLBuilder.class);
	
//	public static String getSQL(Expression expr, BusinessViewState state) {
//		setParametersValue(expr, state);
//		
//		StringBuilder rtn = new StringBuilder();
//		StringBuilder exprBuff = new StringBuilder();
//		exprBuff.append(expr.getSQLPart().getSqlStr());
//		int len = exprBuff.length() - 1;
//		for(; len >= 0; len--) {
//			char ch = exprBuff.charAt(len);
//			if(ch != ' ' && ch != ';' && ch != '\n' && ch != '\r')
//				break;
//		}
//		exprBuff.setLength(len+1);
//		String sql = exprBuff.toString();
//		String last = sql;
//		
//		if(expr.isMutilSql()) {
//			int end = sql.length();
//			int start = sql.lastIndexOf(';', end);
//			if(start >= 0) {
//				last = sql.substring(start + 1, end).trim();
//				rtn.append(sql.substring(0, start)).append(';');
//			} else
//				last = expr.getSQLPart().getSqlStr();
//		} else
//			last = expr.getSQLPart().getSqlStr();
//
//		String tableAlias = null;//ֻΪInformix��
//		if(state.getDbType() == DBType.INFORMIX) {
//			int index = last.indexOf('(');
//			int lastIndex = last.lastIndexOf(')');
//			tableAlias = last.substring(lastIndex + 1);
//			rtn.append(last.substring(index + 1, lastIndex) + " into temp FQIfxTemp;\n");
//		}
//		rtn.append("select ");
//		if(state.getDistinctField() != null)
//			rtn.append(" distinct ");
//		
//		List<BusinessViewOutputFieldBO> selectedFieldList = new ArrayList<BusinessViewOutputFieldBO>();
//		List<BusinessViewOutputFieldBO> groupbyFieldList = new ArrayList<BusinessViewOutputFieldBO>();
//		
//		List<ReportField> tmpList = state.getOutputFields(); 
//		int selectedOutPutFieldCount = tmpList.size();
//		
//		for (int i = 0; i < selectedOutPutFieldCount; i++) {
//			BusinessViewOutputFieldBO tmpField = null;
//			
//			//�����ڴ������������Ƿ���ڸ��ֶ�
//			tmpField =(BusinessViewOutputFieldBO)MetaDataRuntimeContext.getInstance().searchAbstractFieldBO(tmpList.get(i).getId());
//			
//			if(tmpField==null)
//				tmpField = new BusinessViewOutputFieldBO(tmpList.get(i).getId());
//			
//			AggregateMethod tmpMethod = tmpList.get(i).getAggregateMethod();
//			
//			tmpField.setAggregateMethod(tmpMethod);
//			
//			selectedFieldList.add(tmpField);
//			boolean memoryCalcField = false;
//			if(tmpField.getReferencedField() instanceof BusinessViewCalcFieldBO){
//				memoryCalcField = !((BusinessViewCalcFieldBO)tmpField.getReferencedField()).isBuildSql();
//			}else if(tmpField.getReferencedField() instanceof CalcFieldBO){
//				memoryCalcField = !((CalcFieldBO)tmpField.getReferencedField()).getIsBuildSql();
//			}else if(tmpField.getReferencedField() instanceof BusinessAttributeBO){
//				memoryCalcField = !((BusinessAttributeBO)tmpField.getReferencedField()).getIsBuildSql();
//			}
//			
//			if(tmpMethod == null || tmpMethod.equals(AggregateMethod.NULL)){
//				if(!memoryCalcField) {
//					groupbyFieldList.add(tmpField);
//					tmpField.setAggregateMethod(AggregateMethod.NULL);
//				}
//			}				
//			if(state.getDistinctField() == null || state.getDistinctField() == tmpList.get(i)) {
//				if(memoryCalcField)
//					rtn.append("0 as ").append(SessionUUIDGenerator.buildFieldTempID(tmpField.getId())).append(',');
//				else
//					rtn.append(tmpField.getSelectSqlPart().getSqlStr()).append(',');
//			}
////			if (i == selectedOutPutFieldCount - 1)
////				rtn.append(" ");
////			else
////				rtn.append(",");
//		}
//		rtn.setCharAt(rtn.length() - 1, ' ');
//		
//		rtn.append(" from ");
//		if(state.getDbType() != DBType.INFORMIX)
//			rtn.append(last);
//		else
//			rtn.append(" FQIfxTemp as ").append(tableAlias);
////		if(expr.isMutilSql()) {
////			rtn.append(last);
////		} else
////			rtn.append(expr.getSQLPart().getSqlStr());
//		
//		Expression simpleFilterExpr = null;
//		if(state.getSimpleFilter() != null && (simpleFilterExpr = state.getSimpleFilter().getExpression(state.getDbType())) != null){
//			String addedConditionSqlStr = simpleFilterExpr.getSQLPart().getSqlStr().trim();
//			if(!"".equals(addedConditionSqlStr) && !"''".equals(addedConditionSqlStr)){
//				rtn.append(" where ");
//				rtn.append(addedConditionSqlStr);
//			}
//		}
//		
//		//��groupbyField��selectedOutPutField��ȫһ����ʱ�򣬲���ƴGroup����
//		if(groupbyFieldList.size() > 0 && groupbyFieldList.size() != selectedOutPutFieldCount){
//			int count = groupbyFieldList.size();
//			rtn.append(" group by ");
//			for (int i = 0; i < count; i++) {
//				BusinessViewOutputFieldBO tmp = groupbyFieldList.get(i);
//				String tmpStr = tmp.getSQLPart().getSqlStr();
//				rtn.append(tmpStr);
//				if (i == count - 1)
//					rtn.append(" ");
//				else
//					rtn.append(",");
//			}			
//		}
//		
//
//		if (state.getDistinctField() == null && state.getOrderBys() != null && state.getOrderBys().size() > 0) {
//			HashSet<String> selectedFieldId = new HashSet<String>();
//			for(BusinessViewOutputFieldBO tmp : selectedFieldList){
//				selectedFieldId.add(tmp.getId());
//			}
//			
//			LinkedHashMap<String, OrderByType> orderByInfo = new LinkedHashMap<String, OrderByType>();
//			HashMap<String, BusinessViewOutputFieldBO> orderByFields = null;
//			if (state.getDbType() == DBType.EXCEL || state.getDbType() == DBType.DB2
//					|| state.getDbType() == DBType.DB2_400 || state.getDbType() == DBType.DB2_V9)
//				orderByFields = new HashMap<String, BusinessViewOutputFieldBO>();
//
//			for(com.freequery.report.OrderBy tmp : state.getOrderBys()){
//				if(selectedFieldId.contains(tmp.getFieldId())) {
//					orderByInfo.put(tmp.getFieldId(), tmp.getOrderByType());
//
//					if(orderByFields != null) {
//						for(BusinessViewOutputFieldBO fieldBO: selectedFieldList) {
//							if(fieldBO.getId().equals(tmp.getFieldId()))
//									orderByFields.put(tmp.getFieldId(), fieldBO);
//						}
//					}
//				}
//			}
//
//
//			if(orderByInfo.size() > 0) {
//				rtn.append(" order by ");
//				for(Map.Entry<String, OrderByType> entry : orderByInfo.entrySet()) {
//					String fieldId = entry.getKey();
//					OrderByType orderByType = entry.getValue();
//
//					if(orderByFields == null)
//						rtn.append(SessionUUIDGenerator.buildFieldTempID(fieldId)).append(' ');
//					else {
//						String str = orderByFields.get(fieldId).getSelectSqlPart().getSqlStr();
//						str = str.substring(0, str.indexOf(" as "));
//						rtn.append(str).append(' ');
//					}
//
//					rtn.append(orderByType.toString()).append(',');
//				}
//				rtn.setCharAt(rtn.length() - 1, ' ');
//			}
//		}
//		
//		if(log.isDebugEnabled())
//			log.debug("BusinessView.GetSQL: " + rtn.toString());
//		
//		String sqlStr = rtn.toString();
//		sqlStr = StringUtil.changeCharsetTo(sqlStr, state.getCharset());
//		
//		return appendTransmitInfo(sqlStr, expr, state);
//	}
	
//	public static String getRawSQL(Expression expr, BusinessViewState state) {
//		setParametersValue(expr, state);
//		
//		StringBuilder rtn = new StringBuilder();
//		StringBuilder exprBuff = new StringBuilder();
//		exprBuff.append(expr.getSQLPart().getSqlStr());
//		int len = exprBuff.length() - 1;
//		for(; len >= 0; len--) {
//			char ch = exprBuff.charAt(len);
//			if(ch != ' ' && ch != ';' && ch != '\n' && ch != '\r')
//				break;
//		}
//		exprBuff.setLength(len+1);
//		String sql = exprBuff.toString();
//		String last = sql;
//		
//		if(expr.isMutilSql()) {
//			int end = sql.length();
//			int start = sql.lastIndexOf(';', end);
//			if(start >= 0) {
//				last = sql.substring(start + 1, end).trim();
//				rtn.append(sql.substring(0, start)).append(';');
//			} else
//				last = expr.getSQLPart().getSqlStr();
//		} else
//			last = expr.getSQLPart().getSqlStr();
//		
//		String sqlStr = last;
//		sqlStr = StringUtil.changeCharsetTo(sqlStr, state.getCharset());
//		
//		Pattern pattern = Pattern.compile("^[^(]*(\\(.+\\))[^)]+$");
//		sqlStr = sqlStr.replaceAll("\\s+", " ");
//		Matcher matcher = pattern.matcher(sqlStr);
//		if (matcher.find()) {
//			sqlStr = matcher.group(1);
//			sqlStr = sqlStr.substring(1, sqlStr.length() - 1).trim();
//		}
//
//		if(log.isDebugEnabled())
//			log.debug("BusinessView.GetSQL: " + sqlStr);
//		
//		return sqlStr;
//	}
//	
//	public static String getSQLFromAllFields(Expression expr, BusinessViewState state, boolean withTransmitInfo) {
//		setParametersValue(expr, state);
//		
//		StringBuilder rtn = new StringBuilder();
//		StringBuilder exprBuff = new StringBuilder();
//		exprBuff.append(expr.getSQLPart().getSqlStr());
//		int len = exprBuff.length() - 1;
//		for(; len >= 0; len--) {
//			char ch = exprBuff.charAt(len);
//			if(ch != ' ' && ch != ';' && ch != '\n' && ch != '\r')
//				break;
//		}
//		exprBuff.setLength(len+1);
//		String sql = exprBuff.toString();
//		String last = sql;
//		
//		if(expr.isMutilSql()) {
//			int end = sql.length();
//			int start = sql.lastIndexOf(';', end);
//			if(start >= 0) {
//				last = sql.substring(start + 1, end).trim();
//				rtn.append(sql.substring(0, start)).append(';');
//			} else
//				last = expr.getSQLPart().getSqlStr();
//		} else
//			last = expr.getSQLPart().getSqlStr();
//		
//		String sqlStr = last;
//		sqlStr = StringUtil.changeCharsetTo(sqlStr, state.getCharset());
//		
//		if ( withTransmitInfo ) {
//			sqlStr = appendTransmitInfo(sqlStr, expr, state);
//		}
//		
//		sqlStr = "select * from " + sqlStr;
//
//		if(log.isDebugEnabled())
//			log.debug("BusinessView.GetSQL: " + sqlStr);
//		
//		return sqlStr;
//	}
//
//	private static String appendTransmitInfo(String sql, Expression expr, BusinessViewState viewState) {
//
//		ReportField transmitRelatedField = viewState.getTransmitRelatedField();
//		String transmitTempTableName = viewState.getTransmitTempTableName();
//		if ((transmitRelatedField != null)
//				&& (viewState.getDbType() == DBType.ORACLE || viewState.getDbType() == DBType.ORACLE_OCI)) {
//				String fieldName = transmitRelatedField.getTempId();
//				String regex = "[\\s+|\\,]([^\\s\\,]+)\\s+as\\s+" + fieldName + "[\\s+|\\,]";
//				Pattern pat = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
//				Matcher mat = pat.matcher(sql);
//				if (mat.find())
//					fieldName = mat.group(1);
//				String viewId = transmitRelatedField.getBusinessViewField()
//						.getBusinessViewId();
//				String tableId = SessionUUIDGenerator.buildTableTempID(viewId);
//				String tid = " " + tableId + " ";
//				String[] sqlParts = sql.split(tid);
//				if (sqlParts.length > 1)
//					sql = new StringBuilder().append(sqlParts[0]).append(" ").append(
//							tableId).append(" inner join " + transmitTempTableName + " on " + transmitTempTableName + ".col1=")
//							.append(fieldName).append(" ").append(sqlParts[1]).toString();
//				else
//					sql = new StringBuilder().append(sql).append(
//							" inner join " + transmitTempTableName + " on " + transmitTempTableName + ".col1=").append(fieldName)
//							.toString();
//		}
//		return sql;
//	}
//
//	public static void setParametersValue(Expression expr, BusinessViewState state)
//	{
//
//		Map<String, ParamValue> paramValues = new LinkedHashMap<String, ParamValue>();
//		for (OutputParameter tmp : state.getOutputParameters())
//		{
//			ParamValue tmpValue = state.getParamValues().get(tmp.getId());
//			for (String tmpId : tmp.getChildren())
//			{
//				if(tmpId.startsWith("APPARAM."))
//					paramValues.put(tmpId, state.getParamValues().get(tmpId));
//				else
//					paramValues.put(tmpId, tmpValue);
//			}
//			paramValues.put(tmp.getId(), tmpValue); // �����������ʵ�ʲ����Ĺ�ϵҲ����ȥ
//		}
//		expr.setParametersValue(paramValues);
//	}
//
//	public static void setParametersValue(InnerParamProxy item, BusinessViewState state)
//	{
//		Map<String, ParamValue> paramValues = new LinkedHashMap<String, ParamValue>();
//		for (OutputParameter tmp : state.getOutputParameters())
//		{
//			ParamValue tmpValue = state.getParamValues().get(tmp.getId());
//			for (String tmpId : tmp.getChildren())
//			{
//				paramValues.put(tmpId, tmpValue);
//			}
//			paramValues.put(tmp.getId(), tmpValue); // �����������ʵ�ʲ����Ĺ�ϵҲ����ȥ
//		}
//		item.setParametersValue(paramValues);
//		
//	}
//	
//	public static void setParametersValue(ParamIgnoreAreaItem item,
//			BusinessViewState state) {
//		Map<String, ParamValue> paramValues = new LinkedHashMap<String, ParamValue>();
//		for (OutputParameter tmp : state.getOutputParameters())
//		{
//			ParamValue tmpValue = state.getParamValues().get(tmp.getId());
//			for (String tmpId : tmp.getChildren())
//			{
//				paramValues.put(tmpId, tmpValue);
//			}
//			paramValues.put(tmp.getId(), tmpValue); // �����������ʵ�ʲ����Ĺ�ϵҲ����ȥ
//		}
//		item.setParametersValue(paramValues);
//	}
//	
//	public static String getAggregateSQL(Expression expr, BusinessViewState state) {
//		setParametersValue(expr, state);
//		
//		StringBuilder rtn = new StringBuilder();
//		StringBuilder exprBuff = new StringBuilder();
//		exprBuff.append(expr.getSQLPart().getSqlStr());
//		int len = exprBuff.length() - 1;
//		for(; len >= 0; len--) {
//			char ch = exprBuff.charAt(len);
//			if(ch != ' ' && ch != ';' && ch != '\n' && ch != '\r')
//				break;
//		}
//		exprBuff.setLength(len+1);
//		String sql = exprBuff.toString();
//		String last = sql;
//		
//		if(expr.isMutilSql()) {
//			int end = sql.length();
//			int start = sql.lastIndexOf(';', end);
//			if(start >= 0) {
//				last = sql.substring(start + 1, end).trim();
//				rtn.append(sql.substring(0, start)).append(';');
//			} else
//				last = expr.getSQLPart().getSqlStr();
//		} else
//			last = expr.getSQLPart().getSqlStr();
//
//		String tableAlias = null;//ֻΪInformix��
//		if(state.getDbType() == DBType.INFORMIX) {
//			int index = last.indexOf('(');
//			int lastIndex = last.lastIndexOf(')');
//			tableAlias = last.substring(lastIndex + 1);
//			rtn.append(last.substring(index + 1, lastIndex) + " into temp FQIfxTemp;\n");
//		}
//		rtn.append("select ");
//		
//		List<ReportField> tmpList = state.getOutputFields(); 
//		int selectedOutPutFieldCount = tmpList.size();
//		
//		for (int i = 0; i < selectedOutPutFieldCount; i++) {
//			BusinessViewOutputFieldBO tmpField = null;
//			
//			//�����ڴ������������Ƿ���ڸ��ֶ�
//			tmpField =(BusinessViewOutputFieldBO)MetaDataRuntimeContext.getInstance().searchAbstractFieldBO(tmpList.get(i).getId());
//			
//			if(tmpField==null)
//				tmpField = new BusinessViewOutputFieldBO(tmpList.get(i).getId());
//			
//			AggregateMethod tmpMethod = null;
//			if (state.getSubtotal() != null){
//				for (SubtotalFieldDefine define : state.getSubtotal().getYAxis()) {
//					AggregateMethod method = define.getType();		
//					if (method != null){
//						if (define.getId().equals(tmpList.get(i).getId())){
//							tmpMethod = method;
//						}
//						
//					}
//				}
//			}
//			
//			tmpField.setAggregateMethod(tmpMethod);
//
//			boolean memoryCalcField = false;
//			if(tmpField.getReferencedField() instanceof BusinessViewCalcFieldBO){
//				memoryCalcField = !((BusinessViewCalcFieldBO)tmpField.getReferencedField()).isBuildSql();
//			}else if(tmpField.getReferencedField() instanceof CalcFieldBO){
//				memoryCalcField = !((CalcFieldBO)tmpField.getReferencedField()).getIsBuildSql();
//			}else if(tmpField.getReferencedField() instanceof BusinessAttributeBO){
//				memoryCalcField = !((BusinessAttributeBO)tmpField.getReferencedField()).getIsBuildSql();
//			}
//			
//			if (tmpMethod == null || tmpMethod.equals(AggregateMethod.NULL)){
//				if(!memoryCalcField) {
//					tmpField.setAggregateMethod(AggregateMethod.NULL);
//				}
//			}				
//			if (state.getDistinctField() == null || state.getDistinctField() == tmpList.get(i)) {
//				if (tmpMethod != null && !tmpMethod.equals(AggregateMethod.NULL)) {
//					if (memoryCalcField)
//						rtn.append("0 as ").append(
//								SessionUUIDGenerator.buildFieldTempID(tmpField
//										.getId())).append(',');
//					else
//						rtn.append(tmpField.getSelectSqlPart().getSqlStr())
//								.append(',');
//				}
//				else
//					rtn.append("\'\'").append(',');
//			}
//		}
//		rtn.setCharAt(rtn.length() - 1, ' ');
//		
//		rtn.append(" from ");
//		if(state.getDbType() != DBType.INFORMIX)
//			rtn.append(last);
//		else
//			rtn.append(" FQIfxTemp as ").append(tableAlias);
//		
//		Expression simpleFilterExpr = null;
//		if(state.getSimpleFilter() != null && (simpleFilterExpr = state.getSimpleFilter().getExpression(state.getDbType())) != null){
//			String addedConditionSqlStr = simpleFilterExpr.getSQLPart().getSqlStr().trim();
//			if(!"".equals(addedConditionSqlStr) && !"''".equals(addedConditionSqlStr)){
//				rtn.append(" where ");
//				rtn.append(addedConditionSqlStr);
//			}
//		}
//		
//		if(log.isDebugEnabled())
//			log.debug("BusinessView.GetSQL: " + rtn.toString());
//		
//		String sqlStr = rtn.toString();
//		sqlStr = StringUtil.changeCharsetTo(sqlStr, state.getCharset());
//		
//		return appendTransmitInfo(sqlStr, expr, state);
//	}
}
