package com.datadigger.datainsight.expression;

import java.io.Serializable;
import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.datadigger.datainsight.domain.Parameter;
import com.datadigger.datainsight.exception.DataDiggerErrorCode;
import com.datadigger.datainsight.exception.DataDiggerException;
import com.datadigger.datainsight.expression.ast.Node;
import com.datadigger.datainsight.expression.ast.ParamNode;
import com.datadigger.datainsight.expression.ast.TextNode;
import com.datadigger.datainsight.param.ParamValue;
import com.datadigger.datainsight.query.SQLPart;
import com.datadigger.datainsight.query.SQLPartList;
import com.datadigger.datainsight.query.SQLQuery;
import com.datadigger.datainsight.type.ParamExprType;
import com.datadigger.datainsight.type.SQLPartType;
import com.datadigger.datainsight.util.StringUtil;


public class Expression implements IExpressionItem,Serializable{
	private int outputSql = -1;
	
	private String expressionText;
	
	private Node root;
	
	private String refViewID = null;    // ��ʾ�ñ��ʽ������Ĳ�ѯ�� ID��
	
	private List<IExpressionItem> expressionItems = new ArrayList<IExpressionItem>();

	Map<String,ParamValue> parametersValue = new LinkedHashMap<String,ParamValue>();
	public String toString(){
		return getSQLPart().getSqlStr();
	}
	
	public List<IExpressionItem> getExpressionItems(){
		return expressionItems;
	}

	public void addExpressionItem(IExpressionItem item){
		expressionItems.add(item);
	}
	
	public void addAllExpressionItems(List<IExpressionItem> items){
		expressionItems.addAll(items);
	}
	
	public Expression(Node expNode){
		this.root = expNode;
		processIgnorable(this.root);
		com.datadigger.datainsight.expression.ast.Node tmpNode ; 
		for (int i=0;i<root.getNumberOfChildren();i++){
			tmpNode = root.getChild(i);
			if(tmpNode instanceof TextNode){
				TextExpressionItem tei = new TextExpressionItem((TextNode)tmpNode);	
				expressionItems.add(tei);
			}
			//else if (tmpNode instanceof FunctionNode){
			//	Function fn = Function.buildFrom((FunctionNode)tmpNode);
			//	expressionItems.add(fn);
			//} 
			else if (tmpNode instanceof ParamNode) {
				ParamNode paramNode = (ParamNode)tmpNode;				
				if(paramNode.getParamId()==null || "".equals(paramNode.getParamId())){
					throw new DataDiggerException(DataDiggerErrorCode.EXPR_PARAME_NULL_VALUE).
						setDetail("���ʽ����Ϊ�ջ�û�����ó�ʼֵ.");
				}
				
				InnerParamProxy param = createParameProxyById(paramNode.getParamId());
				param.setIgnorable(paramNode.isIgnorable());
				expressionItems.add(param);
			} 
//			else if (tmpNode instanceof ColumnNode) {
//				AbstractFieldBO tmpField = MetaDataRuntimeContext.getInstance().searchAbstractFieldBO(((ColumnNode)tmpNode).getColumnId());				
//				expressionItems.add(tmpField);
//			}
			//else if (tmpNode instanceof TableNode) {
			//	AbstractTable tmpTable = MetaDataRuntimeContext.getInstance().searchInnerTable(((TableNode)tmpNode).getTableId());
			//	expressionItems.add(tmpTable);
			//}
			//else if (tmpNode instanceof SubQueryNode) {
			//	SubQueryItem tmpSubQuery = new SubQueryItem((SubQueryNode)tmpNode);
			//	expressionItems.add(tmpSubQuery);
			//}
			//else if (tmpNode instanceof UserPropNode) {
			//	UserPropertyItem tmpProp = new UserPropertyItem((UserPropNode)tmpNode);
			//	expressionItems.add(tmpProp);
			//}
			//else if (tmpNode instanceof ParamIgnoreAreaNode) {
			//	ParamIgnoreAreaItem tmpIgnoreArea = new ParamIgnoreAreaItem((ParamIgnoreAreaNode)tmpNode);
			//	expressionItems.add(tmpIgnoreArea);
			//}
			else{
				throw new DataDiggerException(DataDiggerErrorCode.EXPR_UNKNOWN_NODE_TYPE).
					setDetail("δ֪�ı��ʽ�������:"+tmpNode.getClass().getName());
			}
			
		}
		this.expressionText = this.getExpressionText();
	}
	
	/**
	 * ���������ʽ�ڵ�ת����ExpressionItem����
	 */
	public Expression(String expStr){	
		this(ExpressionBuilder.parseExpression(expStr));
	}
	
	/**
	 * ��xml�ڵ���ض���
	 * @param node
	 */
	public Expression(Element node){		
		this(node.getAttribute("TextValue"));
	}

/*	
	private ExpressionNode buildExpressionTextByItems(List<IExpressionItem> expItems){
		ExpressionNode expNode = new ExpressionNode();
		
		for (IExpressionItem item : expItems) {
			if (item instanceof TextExpressionItem) {
				TextNode textNode = ((TextExpressionItem)item).getTextNode();
				expNode.addChild(textNode);
			} else if (item instanceof Function) {
				FunctionNode functionNode = ((Function)item).getFunctionNode();
				expNode.addChild(functionNode);
			} else if (item instanceof InnerParamProxy) {
				ParamNode paramNode = new ParamNode();
				paramNode.setText(((InnerParamProxy)item).getParamId());
				expNode.addChild(paramNode);
			} else if (item instanceof AbstractTable) {
				TableNode tableNode = new TableNode();
				tableNode.setText(((AbstractTable)item).getId());
				expNode.addChild(tableNode);
			} else if (item instanceof AbstractFieldBO) {
				ColumnNode columnNode = new ColumnNode();
				columnNode.setText(((AbstractFieldBO)item).getId());
				expNode.addChild(columnNode);
			} else if (item instanceof Expression) {
				Expression expression = (Expression)item;
				ExpressionNode tmpExpNode = expression.buildExpressionTextByItems(expression.expressionItems);
				expNode.addChild(tmpExpNode);
			}else if (item instanceof SubQueryItem) {
				SubQueryNode subQueryNode = new SubQueryNode();
				subQueryNode.setText(((SubQueryItem)item).getQueryId());
				expNode.addChild(subQueryNode);
			}else if (item instanceof UserPropertyItem) {
				UserPropNode propNode = new UserPropNode();
				propNode.setText(((UserPropertyItem)item).getPropertyName());
				expNode.addChild(propNode);
			}  
			else
				throw new DataDiggerException(ErrorCode.EXPR_NOT_SUPPORTED_ITEM_TYPE).
				setDetail(" TYPE:"+item.getClass().getName());
			
		}
		return expNode;
	}
*/
	/**
	 * ��ȡExpression��Ԥ��SQL��
	 * Ч����getSQLStr���ƣ���������û��ʵ������
	 * @return
	 */
	public String getPreviewText() {
		return getPreviewText(false);
	}

	public String getPreviewText(boolean needFormat) {
		SQLPartList ret = new SQLPartList();
		_getPreviewText(ret);
		return ret.getSqlStr(needFormat);
	}
	
	public void _getPreviewText(SQLPartList ret){
		for(IExpressionItem item:expressionItems){
			if (item==null)
				throw new DataDiggerException(DataDiggerErrorCode.EXPR_PARSE_ITEM_LOST);			
			if (item instanceof InnerParamProxy) {
				ParamNode paramNode = new ParamNode();
				paramNode.setText(((InnerParamProxy)item).getParamId()); 
				ret.add(new SQLPart(SQLPartType.SQLSTR,paramNode.getExpressionText()));
			} 
			else if (item instanceof Expression) {
				Expression expression = (Expression)item;
				expression._getPreviewText(ret);				
			}
//			else if (item instanceof BusinessAttributeBO){
//				BusinessAttributeBO attributeBO = (BusinessAttributeBO)item;
//				attributeBO.getExpression()._getPreviewText(ret);
//			}
//			else if (item instanceof BusinessViewCalcFieldBO){
//				BusinessViewCalcFieldBO bizViewCalcField = (BusinessViewCalcFieldBO)item;
//				bizViewCalcField.getExpression()._getPreviewText(ret);
//			}
//			else if (item instanceof CalcFieldBO){
//				CalcFieldBO calcFieldBO = (CalcFieldBO)item;
//				calcFieldBO.getExpression()._getPreviewText(ret);
//			}
//			else if (item instanceof BusinessViewBO){
//				BusinessViewBO bizViewBO = (BusinessViewBO)item;
//				bizViewBO.getTableExpression()._getPreviewText(ret);
//			}		
//			else if (item instanceof SubQueryItem){
//				SubQueryItem queryItem = (SubQueryItem)item;
//				queryItem.getQuery().getExpression()._getPreviewText(ret);
//			}	
//			else if (item instanceof UserPropertyItem){
//				UserPropertyItem propertyItem = (UserPropertyItem)item;
//				UserPropNode userPropNode = new UserPropNode();
//				userPropNode.setText(propertyItem.getPropertyName()); 
//				ret.add(new SQLPart(SQLPartType.SQLSTR,userPropNode.getExpressionText()));
//				//UserPropertyItem propertyItem = (UserPropertyItem)item;
//				//IUserProperty prop = FreeQueryModule.getInstance().getUserManagerModule().getUserPropertyByName(propertyItem.getPropertyName());
//				//if(prop == null)
//				//	throw new DataDiggerException(DataDiggerErrorCode.USER_PROPERTY_NOT_EXIST).setDetail(propertyItem.getPropertyName());
//				//new Expression(prop.getExpression())._getPreviewText(ret);
//			}	
//			else if (item instanceof BusinessViewBO){
//				throw new DataDiggerException(DataDiggerErrorCode.UNKOWN_ERROR).setDetail("������Ҫ�޸ġ���û��֧��");
//			}	
//			else if (item instanceof ParamIgnoreAreaItem){
//				ParamIgnoreAreaItem piai = (ParamIgnoreAreaItem)item;
//				piai.getExpression()._getPreviewText(ret);
//			}
//			else if (item instanceof GetFDPickerValue) { // "Ƶ�����ڿؼ�"����ʹ���ˡ���������������ֵ����Ϊ�գ��ʲ�ִ�и��ຯ����ԭ������
//				GetFDPickerValue fdpItem = (GetFDPickerValue) item;
//				FunctionNode funcNode = fdpItem.getFunctionNode();
//				ret.add(new SQLPart(SQLPartType.SQLSTR, funcNode.getExpressionText()));
//			}
			else
				ret.add(item.getSQLPart());
		}
	}
	
	/**
	 * ��ȡ���ʽ���ڲ������ʽ
	 * @return
	 */
	public String getExpressionText(){
		return getExpressionText(0, expressionItems.size());
	}

	public String getExpressionText(int from, int to){
		StringBuffer sb = new StringBuffer();
		_getExpressionText(sb, from, to);
		return sb.toString();
	}
	
	public void _getExpressionText(StringBuffer ret, int from, int to){
		for (int i = from; i < to; i++) {
			IExpressionItem item = expressionItems.get(i);
			if (item==null)
				continue;
			if (item instanceof TextExpressionItem) {
				TextNode textNode = ((TextExpressionItem)item).getTextNode();
				ret.append(textNode.getExpressionText());
//			} else if (item instanceof Function) {
//				FunctionNode functionNode = ((Function)item).getFunctionNode();
//				ret.append(functionNode.getExpressionText());
//			} else if (item instanceof InnerParamProxy) {
//				ParamNode paramNode = new ParamNode();
//				paramNode.setText(((InnerParamProxy)item).getParamId());
//				ret.append(paramNode.getExpressionText());
//			} else if (item instanceof AbstractTable) {
//				TableNode tableNode = new TableNode();
//				tableNode.setText(((AbstractTable)item).getId());
//				ret.append(tableNode.getExpressionText());
//			} else if (item instanceof AbstractFieldBO) {
//				ColumnNode columnNode = new ColumnNode();
//				columnNode.setText(((AbstractFieldBO)item).getId());
//				ret.append(columnNode.getExpressionText());
//			} else if (item instanceof Expression) {
//				Expression expression = (Expression)item;
//				expression._getExpressionText(ret, 0, expression.getExpressionItems().size());				
//			} else if (item instanceof SubQueryItem) {
//				SubQueryNode subQueryNode = new SubQueryNode();
//				subQueryNode.setText(((SubQueryItem)item).getQueryId());
//				ret.append(subQueryNode.getExpressionText());
//			} else if (item instanceof UserPropertyItem) {
//				UserPropNode propNode = new UserPropNode();
//				propNode.setText(((UserPropertyItem)item).getPropertyName());
//				ret.append(propNode.getExpressionText());
//			}  else if (item instanceof ParamIgnoreAreaItem){
//				ret.append("{[").append(((ParamIgnoreAreaItem)item).getExpression().getExpressionText()).append("]}");
			}
			else
				throw new DataDiggerException(DataDiggerErrorCode.EXPR_NOT_SUPPORTED_ITEM_TYPE).
				setDetail(" ����:"+item.getClass().getName());
			
		}
		
	}	
	
	public synchronized SQLPart getSQLPart(){
		String sqlStr = "", prepareSQL = "", cleanupSQL = "";
		SQLPartList sqlPartList = getSQLPartList();
		if (isMutilSql()) {
			int outputSqlIndex = getOutputSql();
			int size = getExpressionItems().size();
			sqlStr = getExpressionItems().get(outputSqlIndex).getSQLPart().getSqlStr();
			for ( int i = 0; i < outputSqlIndex; i++ ) {
				prepareSQL += getExpressionItems().get(i).getSQLPart().getSqlStr();
			}
			for ( int i = outputSqlIndex+1; i < size; i++ ) {
				cleanupSQL += getExpressionItems().get(i).getSQLPart().getSqlStr();
			}
		} else {
			sqlStr = sqlPartList.getSqlStr().trim();
			prepareSQL = sqlPartList.getPrepareSqlStr();
			cleanupSQL = sqlPartList.getCleanupSqlStr();
		}
		SQLPart result = null;
		if("".equals(sqlStr) && sqlPartList.getType() == SQLPartType.SQLSTR)
			result = new SQLPart(SQLPartType.SQLSTR, "''");
		else
			result = new SQLPart(SQLPartType.SQLSTR, sqlStr);
		result.setPrepareSqlStr(prepareSQL);
		result.setCleanupSqlStr(cleanupSQL);
		return result;
	}

	public SQLPartList getSQLPartList() {

		SQLPartList rtn = new SQLPartList();
		
		for(IExpressionItem item:expressionItems){
			if (item==null)
				throw new DataDiggerException(DataDiggerErrorCode.EXPR_PARSE_ITEM_LOST).setDetail("?="+this.expressionText);
			rtn.add(item.getSQLPart());
		}
		
		return rtn;
	}
	
//	private Set<AbstractFieldBO> getFields(){		
//		Set<AbstractFieldBO> rtn = new HashSet<AbstractFieldBO>();
//		fillFields(rtn);
//		return rtn;
//	}
//	
//	private void fillFields(Set<AbstractFieldBO> ret){
//		
//		for(IExpressionItem item:expressionItems){
//			if (item instanceof AbstractFieldBO) {
//				ret.add((AbstractFieldBO)item); 				
//			} 
//			else if (item instanceof Expression) {
//				Expression expression = (Expression)item;
//				expression.fillFields(ret);			
//			}			
//			else if (item instanceof ParamIgnoreAreaItem){
//				Expression expression = ((ParamIgnoreAreaItem)item).getExpression();
//				expression.fillFields(ret);
//			}
//		}
//	}
	
	/**
	 *ͨ���������ʽ���õ�����ֶκͲ��������������ֶβ�����ֻ����ʽ�������﷨����
	 *ͨ���ı�ֱ����д���ֶ��������޷���ȡ�ġ� 
	 */
//	public Set<AbstractFieldBO> getRelatedFields(){
//		
//		Set<AbstractFieldBO> fields = getFields();
//		return fields;
//	}
//
//	/**
//	 *ͨ���������ʽ���õ�����ֶκͲ����漰���ı����������ֶβ�����ֻ����ʽ�������﷨���С�
//	 *ͨ���ı�ֱ����д�ı��������޷���ȡ�ġ� 
//	 */
//	public Set<String> getRelatedTableIDs(){
//		Set<AbstractFieldBO> fields = getFields();
//		Set<String> ret = new HashSet<String>();
//		for(AbstractFieldBO field:fields){
//			ret.addAll(field.getTableIDs());
//		}		
//		return ret;		
//	}

	
	/**
	 * ɨ����ʽ����ȡ���������еĲ���ID  
	 * 
	 * ���������õĲ�����Ψһ��
	 * 
	 * @return
	 */
	public Set<String> getParameterIDs(){
		HashSet<String> rtn = new LinkedHashSet<String>();
		fillParamIDs(rtn);
		return rtn;
	} 
	
	/**
	 * 
	 * ���������õĲ����ǿ��ظ���
	 * 
	 * @return
	 */
	public List<String> getParameterIDsByList(){
		ArrayList<String> rtn = new ArrayList<String>();
		fillParamIDs(rtn);
		return rtn;
	}
	
	private void fillParamIDs(Collection<String> paramIDs){
		for(IExpressionItem item:expressionItems){
			if (item==null) 
				continue;
			if (item instanceof InnerParamProxy)
			{
				InnerParamProxy innerParamProxy = (InnerParamProxy)item;
				String tmpId = innerParamProxy.getParamId();
				boolean ignorable = innerParamProxy.isIgnorable();
				Parameter tmpParam = null; //MetaDataRuntimeContext.getInstance().searchParameter(tmpId);
				if (tmpParam == null)
					continue;

				/*�����ڲ��ϲ�����������£������ײ����������⣬
				 *���
				 *�ڲ��ϲ�������ʱ��ֻ��鱸ѡֵ���Ӳ���*/

//				if (tmpParam.checkCombinPara()) 
//				{
//					if (tmpParam.getDefaultType().equals(ParamExprType.SQL))
//					{
//						String defaultValue = tmpParam.getDefaultValue();
//						if (!StringUtil.isNullOrEmpty(defaultValue))
//						{
//							Expression exp = new Expression(defaultValue);
//							exp.fillParamIDs(paramIDs);
//						}
//					}
//				}
//				if (tmpParam.getStandByType().equals(ParamExprType.SQL))
//				{
//					String standByValue = tmpParam.getStandByValue();
//					if (!StringUtil.isNullOrEmpty(standByValue))
//					{
//						Expression exp = new Expression(tmpParam.getStandByValue());
//						exp.fillParamIDs(paramIDs);
//					}
//				}
//				if (!tmpParam.checkCombinPara()) {   //���ϲ��������
//					paramIDs.add(tmpId);
//					ignorableParamMap.get().put(tmpId, ignorable);
//				} else if (!paramIDs.contains(tmpId)) {//�ϲ�����������б���û��
//					paramIDs.add(tmpId);
//					ignorableParamMap.get().put(tmpId, ignorable);
//				} else {//����ϲ����б����������Ҫ�޸Ĳ����Ƿ���Ե�ֵ
//					boolean tmp_ignorable =ignorableParamMap.get().get(tmpId);
//					ignorableParamMap.get().put(tmpId, ignorable && tmp_ignorable);
//				}
		} 
//			else if (item instanceof Expression) {
//				Expression expression = (Expression)item;
//				expression.fillParamIDs(paramIDs);				
//			}
//			if (item instanceof Function){
//				((Function)item).getSkinExpression().fillParamIDs(paramIDs);				
//			}
//			else if (item instanceof BusinessAttributeBO){
//				BusinessAttributeBO attributeBO = (BusinessAttributeBO)item;
//				attributeBO.getExpression().fillParamIDs(paramIDs);
//			}
//			else if (item instanceof BusinessViewCalcFieldBO){
//				BusinessViewCalcFieldBO bizViewCalcField = (BusinessViewCalcFieldBO)item;
//				bizViewCalcField.getExpression().fillParamIDs(paramIDs);
//			}
//			else if (item instanceof CalcFieldBO){
//				CalcFieldBO calcFieldBO = (CalcFieldBO)item;
//				calcFieldBO.getExpression().fillParamIDs(paramIDs);
//			}
//			else if (item instanceof BusinessViewBO){
//				BusinessViewBO bizViewBO = (BusinessViewBO)item;
//				bizViewBO.getExpression().fillParamIDs(paramIDs);
//			}		
//			else if (item instanceof SubQueryItem){
//				SubQueryItem queryItem = (SubQueryItem)item;
//				queryItem.getQuery().getExpression().fillParamIDs(paramIDs);
//			}	
//			else if (item instanceof UserPropertyItem){
//				UserPropertyItem propertyItem = (UserPropertyItem)item;
//				UserProperty prop = FreeQueryDAOFactory.getUserPropertyDAO().getByName(propertyItem.getPropertyName());
//				//IUserProperty prop = FreeQueryModule.getInstance().getUserManagerModule().getUserPropertyByName(propertyItem.getPropertyName());
////				if(prop == null)
////					throw new DataDiggerException(ErrorCode.USER_PROPERTY_NOT_EXIST).setDetail(propertyItem.getPropertyName());
////				new Expression(prop.getExpression()).fillParamIDs(paramIDs);
//				
//				if(prop != null)
//					new Expression(prop.getExpression()).fillParamIDs(paramIDs);				
//			}	
//			else if (item instanceof ParamIgnoreAreaItem){
//				ParamIgnoreAreaItem piai = (ParamIgnoreAreaItem)item;
//				piai.getExpression().fillParamIDs(paramIDs);
//			}
			
		}
	}	
	
	public void setParametersValue(Map<String,ParamValue> parametersValue){
		if (parametersValue==null) return;
		
		this.parametersValue.putAll(parametersValue);
		for(IExpressionItem item:this.expressionItems){
//			if (item instanceof Function){
//				((Function)item).setParametersValue(this.parametersValue);
//			}
//			else if (item instanceof InnerParamProxy){
//				((InnerParamProxy)item).setParametersValue(this.parametersValue);
//			}
//			else if (item instanceof BusinessViewBO){
//				((BusinessViewBO)item).setParametersValue(this.parametersValue);
//			}
//			else 
				if (item instanceof Expression){
				((Expression)item).setParametersValue(this.parametersValue);
			}
			
//			else if (item instanceof BusinessAttributeBO){
//				BusinessAttributeBO attributeBO = (BusinessAttributeBO)item;
//				attributeBO.getExpression().setParametersValue(this.parametersValue);
//			}
//			else if (item instanceof BusinessViewCalcFieldBO){
//				BusinessViewCalcFieldBO bizViewCalcField = (BusinessViewCalcFieldBO)item;
//				bizViewCalcField.getExpression().setParametersValue(this.parametersValue);
//			}
//			else if (item instanceof CalcFieldBO){
//				CalcFieldBO calcFieldBO = (CalcFieldBO)item;
//				calcFieldBO.getExpression().setParametersValue(this.parametersValue);
//			}			
//			else if (item instanceof SubQueryItem){
//				SubQueryItem queryItem = (SubQueryItem)item;
//				queryItem.getQuery().getExpression().setParametersValue(this.parametersValue);
//			}	
//			else if (item instanceof UserPropertyItem){
//				UserPropertyItem propertyItem = (UserPropertyItem)item;
//				//IUserProperty prop = FreeQueryModule.getInstance().getUserManagerModule().getUserPropertyByName(propertyItem.getPropertyName());
//				UserProperty prop = FreeQueryDAOFactory.getUserPropertyDAO().getByName(propertyItem.getPropertyName());
//				new Expression(prop.getExpression()).setParametersValue(this.parametersValue);
//			}
//			else if (item instanceof ParamIgnoreAreaItem){
//				ParamIgnoreAreaItem piai = (ParamIgnoreAreaItem)item;
//				piai.getExpression().setParametersValue(this.parametersValue);
//			}
		}
	}
	
	/**
	 * ����ID������InnerParam�Ĵ�����
	 * @param paramID
	 * @return
	 */
	private InnerParamProxy createParameProxyById(String paramID){
		return new InnerParamProxy(paramID);
	}
	
	/**
	 * ����xml�ڵ�
	 * @param doc
	 * @return
	 */
//	public Element getXmlNode(Document doc){
//		Element el = doc.createElement(SQLQuery.EL_EXPRESSION);
//		el.setAttribute("TextValue",expressionText);
//		return el;
//	}
	
	/*
	 * �жϱ��ʽ�����Ƿ�Ϊ��
	 */
	public boolean isEmpty(){
		if(this.getExpressionText() == null || "".equals(this.getExpressionText().trim()))
			return true;
		else
			return false;
	}

	public boolean isMutilSql() {
		return outputSql >= 0;
	}
	
	public int getOutputSql() {
		return outputSql;
	}

	public void setOutputSql(int outputSql) {
		this.outputSql = outputSql;
	}

	
	public void setRefViewID(String refViewID)
	{
	
	    this.refViewID = refViewID;
	}

	
	public String getRefViewID()
	{
	
	    return refViewID;
	}
	
	/***
	 * ���� ���ʽ�����еĲ������ �������ID
	 */
//	public void changeParamToOutputParam(String refid, Map<String, Integer> paramIdInfos) {
//		if (refid == null)
//			refid = this.refViewID;
//		for (IExpressionItem paramItem : this.expressionItems) {
//			if (paramItem instanceof InnerParamProxy) {
//				InnerParamProxy temp = (InnerParamProxy) paramItem;
//				String id = temp.getParamId();
//				String paramname = id.substring(id.lastIndexOf(".") + 1);
//
//				Parameter tmpParam = MetaDataRuntimeContext.getInstance()
//						.searchParameter(id);
//				if (tmpParam == null || tmpParam.checkCombinPara())
//					continue;
//				if (paramIdInfos.containsKey(id)) {
//					int number = paramIdInfos.get(id);
//					temp.setOutputParamID(OutputParameter.makeOutputParamID(refid, paramname
//							+ number));
//					number++;
//					paramIdInfos.put(id, number);
//				} else {
//					temp.setOutputParamID(OutputParameter.makeOutputParamID(refid, paramname));
//					paramIdInfos.put(id, 0);
//				}
//			} else if (paramItem instanceof ParamIgnoreAreaItem) {
//				ParamIgnoreAreaItem item = (ParamIgnoreAreaItem) paramItem;
//				Expression expr = item.getExpression();
//				if (expr != null)
//					expr.changeParamToOutputParam(refid, paramIdInfos);
//			}
//		}
//	}

	private void processIgnorable(Node node) {
		processIgnorableIntern(node, 0, false);
	}

	private void processIgnorableIntern(Node node, int deep, boolean ignorable) {
		if ( ignorable && node instanceof ParamNode ) {
			((ParamNode)node).setIgnorable(true);
		}
		Node nextSibling = node.getNextSibling();
		if ( nextSibling != null ) {
			processIgnorableIntern(nextSibling, deep, ignorable);
		}
		Node firstChild = node.getFirstChild();
//		ignorable = ignorable || (node instanceof ParamIgnoreAreaNode ? true : false);
		if ( firstChild != null ) {
			processIgnorableIntern(firstChild, deep+1, ignorable);
		}
	}

	public static ThreadLocal<Map<String, Boolean>> ignorableParamMap = new ThreadLocal<Map<String, Boolean>>() {
	
		@Override
		protected Map<String, Boolean> initialValue() {
			return new HashMap<String, Boolean>();
		}
	
	};
}