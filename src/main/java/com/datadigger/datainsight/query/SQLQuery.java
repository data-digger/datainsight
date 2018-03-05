package com.datadigger.datainsight.query;

import java.io.Serializable;
import java.util.*;

import org.w3c.dom.*;


public class SQLQuery implements Serializable{
//	// �洢���õĿ��ӻ���ѯ
//	private List<BusinessViewBO> relatedBusinessViews = new ArrayList<BusinessViewBO>();
//
//	// ֻ�洢SQLQuery�ж���Ĺ�ϵ����ƴFROM��WHERE��ʱ����Ҫ��ȡ����Ϣ
//	// private List <TempRelation> tableRelationsDefinedInQuery = new
//	// ArrayList();
//	private BusinessViewTableLinksBO linksBO;
//
//	// �洢ֱ����ò�ѯ��ص��ֶΣ��κ����͵��ֶζ�����ʹ��getRelatedField��ȡ��ص��ֶ�
//	// private List <InnerField> relatedFields = new ArrayList();
//
//	// �洢һ�����ʽ����WHERE����һ�������ʽ��ʵ����ExpressionItem�ӿڵĶ��󹹳�
//	private Expression theWhere = new Expression("");
//
//	// ͬtheWhere����
//	private Expression theHaving = new Expression("");
//	
//	private boolean distinct = false;
//	protected String bizThemeId = "";
//	private static final String ATTR_BIZTHEME_ID = "bizthemeid";
//
//	// �洢Field,SelfDefinedField,BussinessViewField����
//	private List<AbstractFieldBO> theSelect = new ArrayList<AbstractFieldBO>();
//
//	
//
//	public SQLQuery(BusinessViewBO par) {
//		this.setPar(par);
//		this.linksBO = new BusinessViewTableLinksBO(this);
//		this.doc = XmlUtility.newDocument();
//	}
//
//	@Override
//	public String getId(){
//		if(this.id == null || "".equals(this.id.trim()))
//			this.id = "SQLQuery." + this.getPar().getId() + "." + UUIDGenerator.generate();
//		return this.id;
//	}
//
//	@Override
//	public QueryType getQueryType() {
//		return QueryType.SQL;
//	}	
//	private List<AbstractFieldBO> parseAbstractFieldList(NodeList nodeList) {
//		ArrayList<AbstractFieldBO> rtn = new ArrayList<AbstractFieldBO>();
//		int count = nodeList.getLength();
//		for (int i = 0; i < count; i++) {
//			Element tmpNode = (Element) nodeList.item(i);
//			FieldType tmpFieldType = FieldType.valueOf(tmpNode.getAttribute(ATTR_TYPE));
//			switch (tmpFieldType) {
//			case BASIC_FIELD:
//				rtn.add(new BasicFieldBO(tmpNode));
//				break;
//			case CALC_FIELD:
//				//�����쳣,��������ֶα�ɾ�����������Ĳ�ѯһ��Ҳ����������ȥ,ȱ��:�޷�֪ͨ�û������쳣
//				try{
//					rtn.add(new CalcFieldBO(tmpNode));
//				}catch(Exception e){
//				}
//				break;
//			case BUSINESS_ATTRIBUTE:
//				rtn.add(new BusinessAttributeBO(tmpNode));
//				break;
//			case BUSINESSVIEW_OUTPUT_FIELD:
//				rtn.add(new BusinessViewOutputFieldBO(tmpNode));
//				break;
//			case BUSINESSVIEW_CALC_FIELD:
//				String bizViewCalcFieldId = tmpNode.getAttribute(ATTR_ID);
//				rtn.add(MetaDataRuntimeContext.getInstance().searchAbstractFieldBO(bizViewCalcFieldId));
//				// rtn.add(new BusinessViewCalcFieldBO(tmpNode));
//				break;
//			}
//		}
//
//		return rtn;
//	}
//
//	public SQLQuery(Element queryNode, BusinessViewBO par) {
//		this(par);
//		this.id = queryNode.getAttribute(ATTR_ID);
//		QueryType queryType = QueryType.valueOf(queryNode.getAttribute(AbstractFieldBO.ATTR_TYPE));
//		this.queryRelation = QueryRelationType.valueOf(queryNode.getAttribute(ATTR_QUERY_RELATION));
//		String tmpDistinct = queryNode.getAttribute(ATTR_DISTINCT);
//		if(tmpDistinct == null || "".equals(tmpDistinct))
//			this.distinct = false;
//		else
//			this.distinct = Boolean.parseBoolean(tmpDistinct);
//		this.name = queryNode.getAttribute(ATTR_NAME);
//		this.alias = queryNode.getAttribute(ATTR_ALIAS);
//		this.desc = queryNode.getAttribute(ATTR_DESC);
//		this.setBizThemeId(queryNode.getAttribute(ATTR_BIZTHEME_ID));
//		
//		if(this.name == null || "".equals(this.name))
//			this.name = "query" + this.getPar().queryIndex;
//		
//		if(this.alias == null || "".equals(this.alias))
//			this.alias = "��ѯ" + this.getPar().queryIndex;		
//		
//		if (QueryType.SQL.equals(queryType)) {
//			NodeList tmpNodeList = null;
//
//			Element relationGraphNode = (Element) queryNode.getElementsByTagName(EL_RELATION_GRAPH)
//					.item(0);
//			this.linksBO = new BusinessViewTableLinksBO(relationGraphNode, this);
//
//			Element theSelectNode = (Element) queryNode.getElementsByTagName(EL_THE_SELECT).item(0);
//			tmpNodeList = theSelectNode.getElementsByTagName(EL_FIELD);
//			this.theSelect = parseAbstractFieldList(tmpNodeList);
//
//			Element theWhereNode = (Element) queryNode.getElementsByTagName(EL_THE_WHERE).item(0);
//			tmpNodeList = theWhereNode.getElementsByTagName(EL_EXPRESSION);
//			this.theWhere = new Expression((Element) tmpNodeList.item(0));
//			this.theWhere.changeParamToOutputParam(this.getPar().getId(), new HashMap<String, Integer>());
//
//			Element theHavingNode = (Element) queryNode.getElementsByTagName(EL_THE_HAVING).item(0);
//			tmpNodeList = theHavingNode.getElementsByTagName(EL_EXPRESSION);
//			this.theHaving = new Expression((Element) tmpNodeList.item(0));
//		} else if (QueryType.TEXT.equals(queryType)) {
//
//		}
//	}
//
//	public Element getXmlNode(Document doc) {
//		Element queryNode = doc.createElement(EL_QUERY);
//		queryNode.setAttribute(ATTR_ID, this.id);
//		queryNode.setAttribute(ATTR_TYPE, QueryType.SQL.toString());
//		queryNode.setAttribute(ATTR_NAME, this.name);
//		queryNode.setAttribute(ATTR_ALIAS, this.alias);
//		queryNode.setAttribute(ATTR_DESC, this.desc);
//		queryNode.setAttribute(ATTR_QUERY_RELATION, this.queryRelation.toString());
//		queryNode.setAttribute(ATTR_DISTINCT, String.valueOf(this.distinct));
//		queryNode.setAttribute(ATTR_BIZTHEME_ID, this.getBizThemeId());
//		int count = 0;
//
//		queryNode.appendChild(this.linksBO.getXmlNode(doc));
//
//		Element theSelectNode = doc.createElement(EL_THE_SELECT);
//		queryNode.appendChild(theSelectNode);
//		count = theSelect.size();
//		for (int i = 0; i < count; i++) {
//			AbstractFieldBO tmpField = this.theSelect.get(i);
//			theSelectNode.appendChild(tmpField.getXmlNode(doc));
//		}
//
//		Element theWhereNode = doc.createElement(EL_THE_WHERE);
//		queryNode.appendChild(theWhereNode);
//		theWhereNode.appendChild(theWhere.getXmlNode(doc));
//
//		Element theHavingNode = doc.createElement(EL_THE_HAVING);
//		queryNode.appendChild(theHavingNode);
//		theHavingNode.appendChild(theHaving.getXmlNode(doc));
//		return queryNode;
//	}
//
//	private HashSet<AbstractFieldBO> getRelatedFields() {
//		HashSet<AbstractFieldBO> rtn = new HashSet<AbstractFieldBO>();
//		for (AbstractFieldBO tmp : this.theSelect) {
//			rtn.addAll(tmp.getRelatedFields());
//		}
//
//		rtn.addAll(this.theWhere.getRelatedFields());
//
//		rtn.addAll(this.theHaving.getRelatedFields());
//		return rtn;
//	}
//
//	private Expression buildExpression(List<Expression> selectPart,
//			Expression fromPart, List<IExpressionItem> wherePart,
//			List<IExpressionItem> groupPart,
//			Expression havingPart) {
//		Expression rtn = new Expression("");
//		rtn.addExpressionItem(new TextExpressionItem("select "));
//		if(distinct)
//			rtn.addExpressionItem(new TextExpressionItem("distinct "));
//		int count = selectPart.size();
//		for (int i = 0; i < count; i++) {
//			Expression tmp = selectPart.get(i);
//			rtn.addAllExpressionItems(tmp.getExpressionItems());
//			
//			if (i == count - 1)
//				rtn.addExpressionItem(new TextExpressionItem(" "));
//			else
//				rtn.addExpressionItem(new TextExpressionItem(","));
//		}
//
//		List<IExpressionItem> fromItems = fromPart.getExpressionItems();
//		if(fromItems == null || fromItems.size() == 0) {
//			switch(getPar().getDataSource().getDriverType()) {
//				case ORACLE:
//				case ORACLE_OCI:
//					rtn.addExpressionItem(new TextExpressionItem(" from dual "));
//			}
//		} else {
//			rtn.addExpressionItem(new TextExpressionItem(" from "));
//			rtn.addAllExpressionItems(fromItems);
//		}
//
//		clearEmptyExpression(wherePart);
//		
//		if (wherePart != null && !wherePart.isEmpty()) {
//			rtn.addExpressionItem(new TextExpressionItem(" where "));
//			count = wherePart.size();
//			for (int i = 0; i < count; i++) {
//				IExpressionItem tmp = wherePart.get(i);
//				rtn.addExpressionItem(tmp);
//			
//				if (i == count - 1)
//					rtn.addExpressionItem(new TextExpressionItem(" "));
//				else
//					rtn.addExpressionItem(new TextExpressionItem(" and "));
//			}
//		}
//
//		if (groupPart.size() > 0 && groupPart.size() < selectPart.size()) {
//			rtn.addExpressionItem(new TextExpressionItem(" group by "));
//			count = groupPart.size();
//			for (int i = 0; i < count; i++) {
//				IExpressionItem tmp = groupPart.get(i);
//				rtn.addExpressionItem(tmp);
//				if (i == count - 1)
//					rtn.addExpressionItem(new TextExpressionItem(" "));
//				else
//					rtn.addExpressionItem(new TextExpressionItem(","));
//			}
//		}
//
//		if (havingPart != null && havingPart.isEmpty() == false) {
//			rtn.addExpressionItem(new TextExpressionItem(" having "));
//			for(IExpressionItem item : havingPart.getExpressionItems()) {
//				int index = theSelect.indexOf(item);
//				if(index >= 0) {
//					//��Having��ʹ����ȷ�ľۺϷ�ʽ�����ﶼʹ��ǰ���Count(A)����
//					rtn.addExpressionItem(theSelect.get(index).getSelectPartExpression(null, false));
//				} else
//					rtn.addExpressionItem(item);
//			}
//			//Having���ֵı��ʽ��Ҫ���ֶ��滻��ΪTempID
////			Expression havingExp = new Expression("");
////			for(IExpressionItem tmp:havingPart.getExpressionItems()){
////				if(tmp instanceof AbstractFieldBO){
////					String tmpId = UUIDGenerator.buildFieldTempID(((AbstractFieldBO)tmp).getId());
////					havingExp.addExpressionItem(new TextExpressionItem(tmpId));
////				}else
////					havingExp.addExpressionItem(tmp);
////			}
////			rtn.addExpressionItem(havingExp);
//		}
//		return rtn;
//	}
//
//	private void clearEmptyExpression(List<IExpressionItem> expList) {
//		for (Iterator<IExpressionItem> it = expList.iterator(); it.hasNext();) {
//			IExpressionItem item = it.next();
//			Expression exp = new Expression("");
//			exp.addExpressionItem(item);
////			if (exp.getSQLPart().getSqlStr().endsWith("") ||exp.isEmpty()){
//			if (exp.isEmpty()) {
//				it.remove();
//			}
//		}
//	}
//
//	public BusinessViewTableLinksBO getLinksBO() {
//		return this.linksBO;
//	}
//	
//	public Expression getExpression() {
//		ArrayList<Expression> selectPart = new ArrayList<Expression>();
//		for(int i = 0; i < theSelect.size(); i++){
//			AbstractFieldBO tmp = this.theSelect.get(i);
//			if(tmp instanceof BusinessViewCalcFieldBO && !((BusinessViewCalcFieldBO)tmp).isBuildSql())
//				continue;
//			if(tmp instanceof CalcFieldBO && !((CalcFieldBO)tmp).getIsBuildSql())
//				continue;
//			if(tmp instanceof BusinessAttributeBO && !((BusinessAttributeBO)tmp).getIsBuildSql())
//				continue;
//			if(queryRelation != QueryRelationType.UNION)
//				selectPart.add(tmp.getSelectPartExpression());
//			else {
//				SQLQuery mainQuery = ((SQLQuery)this.getPar().getMainQuery());
//				if(mainQuery.theSelect.size() <= i)
//					throw new BOFException(FreeQueryErrorCode.BUSINESSVIEW_UNION_FILED_NUM_DIFF);
//				String tempId = mainQuery.theSelect.get(i).getTempID();
//				selectPart.add(tmp.getSelectPartExpression(tempId, true));
//			}
//		}
//
//		ArrayList<IExpressionItem> groupByPart = new ArrayList<IExpressionItem>();
//		for (AbstractFieldBO tmpField : this.theSelect) {
//			if (tmpField instanceof ITakePartInGroup){ //����ǿ��ӻ���ѯ�����ֶΡ������ֶΡ�������ҵ������
//				TakePartInGroup inGroup = ((ITakePartInGroup)tmpField).getIsInGroup();
//				switch(inGroup){
//					case AUTO:{
//						if (tmpField.getAggregateMethod().equals(AggregateMethod.NULL))
//							groupByPart.add(tmpField);
//						break;
//					}
//					case NO:
//						break;
//					case YES:
//						groupByPart.add(tmpField);
//				}
//			}						
//			else if (tmpField.getAggregateMethod().equals(AggregateMethod.NULL))
//				groupByPart.add(tmpField);
//		}
//
//		TableLinksGraphBO graph = getGraph();
//		List<AbstractTable> tables = changeID2AbstractTable(graph.getAllRelatedTableID());
//		//List<AbstractTable> tables = changeID2AbstractTable(graph.getDirectRelatedTableIDs());
//
//		Expression fromPart = this.buildFromPartExp(graph.getMiniSpanTrees());
//
//		ArrayList<IExpressionItem> wherePart = new ArrayList<IExpressionItem>();
//		
//		//��Where���ֵĲ�������ת��
////		String value = ConfigService.getInstance().getConfigValue(SysParams.COMBINPARAM_DEFAULT);
////		if ("false".equals(value)) {
//			this.theWhere.changeParamToOutputParam(this.getPar().getId(), new HashMap<String, Integer>());
////		}
//		
//		wherePart.add(this.theWhere);
//		
//		List<AbstractTable> tmpTables = new ArrayList<AbstractTable>();
//		int priority = 0;
//		for (AbstractTable tmpTable : tables){			
//			// ��ȡ���ȼ�
//			int tmpPriority = RowPermissionService.getInstance()
//					.getPriorityByTableId(tmpTable.getId());
//			if (tmpPriority > priority) {
//				priority = tmpPriority;
//				tmpTables.clear();
//				tmpTables.add(tmpTable);
//			} else if (tmpPriority == priority)
//				tmpTables.add(tmpTable);						
//		}
//		
//		for (AbstractTable tmpTable : tmpTables) {
//			if (!RowPermissionService.getInstance().isApplyRangeEmpty(
//					tmpTable.getId())
//					&& RowPermissionService.getInstance()
//							.checkCurrentUserInApplyRange(tmpTable.getId())) {
//				Expression tmp = tmpTable.getRowPermissionExp();
//				if (tmp != null)
//					wherePart.add(tmp);
//			} else if (RowPermissionService.getInstance().isApplyRangeEmpty(
//					tmpTable.getId())) {
//				Expression tmp = tmpTable.getRowPermissionExp();
//				if (tmp != null)
//					wherePart.add(tmp);
//			}
//		}
//
//		return this.buildExpression(selectPart, fromPart, wherePart, groupByPart,
//				this.theHaving);
//	}
//
//	/*
//	 * ��ȡ������صı�
//	 * ���ı�Select���ֻ������򲿷ֵ��ֶε�ʱ����Ҫ���������������ȡ������صı�Ȼ����չ�ϵͼ�еı��޸Ĺ�ϵͼ
//	 */	
//	private List<AbstractTable> changeID2AbstractTable(Set<String> tablesIDs){
//		HashSet<String> rtnIDs = new HashSet<String>();
//		rtnIDs.addAll(tablesIDs);
//		List<AbstractTable> rtn = new ArrayList<AbstractTable>();
//		for(String ID:rtnIDs){
//			AbstractTable tmpInnerTable = MetaDataRuntimeContext.getInstance().searchInnerTable(ID);
//			if(tmpInnerTable != null)
//				rtn.add(tmpInnerTable);
//		}
//		
//		return rtn;
//
//	}
//	
//	
//	private List<AbstractLinkBO> changeITableLink2AbstractLinkBO(List<ITableLink> links){
//		List<ITableLink> tmpLinks = links;
//		List<AbstractLinkBO> rtn = new ArrayList<AbstractLinkBO>();
//		for(ITableLink tmpLink:tmpLinks){
//			if(tmpLink instanceof BusinessViewLinkBO){
//				rtn.add((BusinessViewLinkBO)tmpLink);
//			}
//			else {
//				rtn.add(new TableLinkBO((TableLink)tmpLink));
//			}
//		}
//		return rtn;
//	}
//	
//	
//	private Expression buildFromPartExp(List<MiniSpanTree> treeList){
//		Expression rtn = new Expression("");
//		int count = treeList.size();
//		for(int i = 0;i < count; i++){
//			MiniSpanTree tree = treeList.get(i);
//			rtn.addAllExpressionItems(this.buildTableTreeExp2(tree.getRoot(), tree).getExpressionItems());
//			if(i == count -1)
//				rtn.addExpressionItem(new TextExpressionItem(" "));
//			else	
//				rtn.addExpressionItem(new TextExpressionItem(","));
//		}
//		return rtn;
//	}
//
//	private AbstractTable getTableByNode(GraphNode node){
//		return MetaDataRuntimeContext.getInstance().searchInnerTable(node.getKey());
//	}
//
//	private AbstractLinkBO getRelationByNodes(GraphNode from, GraphNode to, MiniSpanTree tree){
//		return (AbstractLinkBO)tree.getLink(from, to).getLinkInfo();
//	}
//
//	private Expression buildTableTreeExp(GraphNode curRoot, MiniSpanTree tree){
//		if(curRoot == null)
//			throw new BOFException(FreeQueryErrorCode.SQL_QUERY_GRAPH_NODE_NULL);
//		Expression rtn = new Expression("");
//		List<GraphNode> children = tree.getChildren(curRoot);
//		if(children == null || children.size() == 0){
//			rtn.addExpressionItem(getTableByNode(curRoot));	
//		}else{			
//			rtn.addExpressionItem(new TextExpressionItem("("));
//			rtn.addExpressionItem(getTableByNode(curRoot));			
//			for(GraphNode tmpNode : children){
//				AbstractLinkBO tmpLink = getRelationByNodes(curRoot, tmpNode, tree);
//				TableLinkType linkType = tmpLink.getLinkType();
//				switch(linkType) {
//					case LeftJoin:
//						if(tmpLink.getSrcTable().getId().equals(curRoot.getKey()))
//							rtn.addExpressionItem(new TextExpressionItem(" left outer join "));
//						else
//							rtn.addExpressionItem(new TextExpressionItem(" right outer join "));
//						break;
//					case RightJoin:
//						if(tmpLink.getSrcTable().getId().equals(curRoot.getKey()))
//							rtn.addExpressionItem(new TextExpressionItem(" right outer join "));
//						else
//							rtn.addExpressionItem(new TextExpressionItem(" left outer join "));
//						break;
//					case FullJoin:
//						rtn.addExpressionItem(new TextExpressionItem(" full outer join "));
//						break;
//					case InnerJoin:
//						rtn.addExpressionItem(new TextExpressionItem(" inner join "));
//						break;
//				}
//				rtn.addExpressionItem(buildTableTreeExp(tmpNode, tree));
//				rtn.addExpressionItem(new TextExpressionItem(" on "));
//				rtn.addExpressionItem(tmpLink.getLinkConditionExp());
//			}
//			rtn.addExpressionItem(new TextExpressionItem(")"));
//		}
//		return rtn;
//	}
//
//	/**
//	 * ƴװ���нṹ��Join
//	 * @param curRoot
//	 * @param tree
//	 * @return
//	 */
//	private Expression buildTableTreeExp2(GraphNode rootNode, MiniSpanTree tree){
//		Expression rtn = new Expression("");
//		List<GraphNode> list = new ArrayList<GraphNode>();
//		MiniSpanTree.makePreorderList(tree, rootNode, list);
//		
//		final int count = list.size();
//		GraphNode node = list.get(0);
//		rtn.addExpressionItem(getTableByNode(node));
//		for ( int i = 1; i < count; i++ ) {
//			GraphNode tmpNode = list.get(i);
//			GraphNode parentNode = tree.getParent(tmpNode);
//			AbstractLinkBO tmpLink = getRelationByNodes(parentNode, tmpNode, tree);
//			TableLinkType linkType = tmpLink.getLinkType();
//			switch(linkType) {
//				case LeftJoin:
//					if(tmpLink.getSrcTable().getId().equals(parentNode.getKey()))
//						rtn.addExpressionItem(new TextExpressionItem(" left outer join "));
//					else
//						rtn.addExpressionItem(new TextExpressionItem(" right outer join "));
//					break;
//				case RightJoin:
//					if(tmpLink.getSrcTable().getId().equals(parentNode.getKey()))
//						rtn.addExpressionItem(new TextExpressionItem(" right outer join "));
//					else
//						rtn.addExpressionItem(new TextExpressionItem(" left outer join "));
//					break;
//				case FullJoin:
//					rtn.addExpressionItem(new TextExpressionItem(" full outer join "));
//					break;
//				case InnerJoin:
//					rtn.addExpressionItem(new TextExpressionItem(" inner join "));
//					break;
//			}
//			rtn.addExpressionItem(getTableByNode(tmpNode));
//			rtn.addExpressionItem(new TextExpressionItem(" on "));
//			rtn.addExpressionItem(tmpLink.getLinkConditionExp());
//		}
//		return rtn;
//	}
//	
//	private AbstractFieldBO searchAbstractFieldInList(List<AbstractFieldBO> fieldList, String fieldID) {
//		for (AbstractFieldBO tmp : fieldList) {
//			if (fieldID.equals(tmp.getId()))
//				return tmp;
//		}
//		return null;
//	}
//
//	public IField createSelectField(String fieldID) {
//		AbstractFieldBO tmp = searchAbstractFieldInList(this.theSelect, fieldID);
//		if (tmp != null) {
//			throw new BOFException(FreeQueryErrorCode.BUSINESSVIEW_RUNTIME_FIELD_EXSIST);
//		}
//		AbstractFieldBO rtn = MetaDataRuntimeContext.getInstance().searchAbstractFieldBO(fieldID);
//		if (rtn instanceof BusinessViewOutputFieldBO) {
//			BusinessViewOutputFieldBO bizViewOutputFieldBO = (BusinessViewOutputFieldBO)rtn;
//			BusinessViewBO bizViewBO = MetaDataRuntimeContext.getInstance().searchBusinessView(bizViewOutputFieldBO.getBusinessViewId());
//			if ( bizViewBO.isReachable(getPar().getId()) ) {
//				throw new BOFException(FreeQueryErrorCode.BUSINESSVIEW_REFERENCE_RECURSIVELY);
//			}
//		}
//		isSelectFieldsChanged = true;
//		this.theSelect.add(rtn);
//		return rtn;
//	}
//
//	public void addSelectField(AbstractFieldBO field){
//		isSelectFieldsChanged = true;
//		this.theSelect.add(field);
//	}
//	
//	public void removeSelectField(String fieldID) {
//		isSelectFieldsChanged = true;
//		AbstractFieldBO tmp = searchAbstractFieldInList(this.theSelect, fieldID);
//		if (tmp != null)
//			this.theSelect.remove(tmp);
//	}
//	
//	public void removeAllSelectField(){
//		isSelectFieldsChanged = true;
//		this.theSelect.clear();
//	}
//
//	public void setSelectFieldAggregateMethod(String fieldID, AggregateMethod aggregateMethod) {
//		AbstractFieldBO tmp = searchAbstractFieldInList(this.theSelect, fieldID);
//		if (tmp != null)
//			tmp.setAggregateMethod(aggregateMethod);
//	}
//
//	public void setSelectFieldInfo(String fieldID,String alias,AggregateMethod aggregateMethod,String format){
//		AbstractFieldBO tmp = searchAbstractFieldInList(this.theSelect, fieldID);
//		if (tmp != null){
//			tmp.setAggregateMethod(aggregateMethod);
//			tmp.setAlias(alias);
//			tmp.setDataFormat(format);
//			isSelectFieldsChanged = true;
//		}
//	}
//	
//	public void setWherePartExpressionText(String expText) {
//		this.theWhere = new Expression(expText);
//		for (IExpressionItem exprItem : this.theWhere.getExpressionItems()) {
//			if ( exprItem instanceof BusinessViewOutputFieldBO ) {
//				BusinessViewOutputFieldBO bizViewOutputFieldBO = (BusinessViewOutputFieldBO)exprItem;
//				BusinessViewBO bizViewBO = MetaDataRuntimeContext.getInstance().searchBusinessView(bizViewOutputFieldBO.getBusinessViewId());
//				if ( bizViewBO.isReachable(getPar().getId()) ) {
//					throw new BOFException(FreeQueryErrorCode.BUSINESSVIEW_REFERENCE_RECURSIVELY);
//				}
//			}
//		}
//	}
//
//	public void setHavingPartExpressionText(String expText) {
//		this.theHaving = new Expression(expText);
//		for (IExpressionItem exprItem : this.theHaving.getExpressionItems()) {
//			if ( exprItem instanceof BusinessViewOutputFieldBO ) {
//				BusinessViewOutputFieldBO bizViewOutputFieldBO = (BusinessViewOutputFieldBO)exprItem;
//				BusinessViewBO bizViewBO = MetaDataRuntimeContext.getInstance().searchBusinessView(bizViewOutputFieldBO.getBusinessViewId());
//				if ( bizViewBO.isReachable(getPar().getId()) ) {
//					throw new BOFException(FreeQueryErrorCode.BUSINESSVIEW_REFERENCE_RECURSIVELY);
//				}
//			}
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	public List<AbstractFieldBO> getSelectFields() {
//		isSelectFieldsChanged = false;
//		return Collections.unmodifiableList(this.theSelect);
//	}
//
//	public AggregateMethod getSelectFieldAggregateMethod(String fieldID) {
//		AbstractFieldBO tmp = searchAbstractFieldInList(this.theSelect, fieldID);
//		if (tmp != null)
//			return tmp.getAggregateMethod();
//		return null;
//	}
//
//	public String getWherePartExpressionText() {
//		return this.theWhere.getExpressionText();
//	}
//
//	public String getHavingPartExpressionText() {
//		return this.theHaving.getExpressionText();
//	}
//	
//	public String getSelectPartExpressionText() {
//		List<AbstractFieldBO> fields = getSelectFields();
//		Expression expression = new Expression("");
//		for (AbstractFieldBO field : fields) {
//			expression.addExpressionItem(field);
//		}
//		return expression.getExpressionText();
//	}
//
//	public List<NodeData> getWhereExprNodeSequence() {
//		ExpressionService es = ExpressionService.getInstance();
//		return es.getExprNodeSequence(getWherePartExpressionText());
//	}
//
//	public List<NodeData> getHavingExprNodeSequence() {
//		ExpressionService es = ExpressionService.getInstance();
//		return es.getExprNodeSequence(getHavingPartExpressionText());
//	}
//
//	public List<NodeData> getSelectExprNodeSequence() {
//		ExpressionService es = ExpressionService.getInstance();
//		return es.getExprNodeSequence(getSelectPartExpressionText());
//	}
//
//	/**
//	 * ���ϵͼ��صķ���	
//	 * list[0] = addedTablesIDList
//	 * list[1] = deletedTablesIDList
//	 */
//	public List refreshRelationGraph() {
//		return this.linksBO.execute(this.getRelatedFields());
//	}
//
//	public void updateSelectField(String fieldID, String alias, String desc, String dataFormat) {
//		// TODO Auto-generated method stub
//		isSelectFieldsChanged = true;
//		AbstractFieldBO tmp = this.searchAbstractFieldInList(this.theSelect, fieldID);
//		tmp.setAlias(alias);
//		tmp.setDesc(desc);
//		tmp.setDataFormat(dataFormat);
//	}
//
//	public AbstractFieldBO getSelectField(String fieldID) {
//		AbstractFieldBO tmp = this.searchAbstractFieldInList(this.theSelect, fieldID);
//		return tmp;
//	}
//
//	@Override
//	public Set<String> getParameterIDList() {
//		throw new UnsupportedOperationException();
//	}
//
//	public void addRelatedBusinessViews(BusinessViewBO viewBO) {
//		relatedBusinessViews.add(viewBO);
//	}
//	
//	public void removeRelatedBusinessViews(BusinessViewBO viewBO) {
//		relatedBusinessViews.remove(viewBO);
//	}
//
//
//	public boolean isDistinct() {
//		return distinct;
//	}
//
//	public void setDistinct(boolean isDistinct) {
//		this.distinct = isDistinct;
//	}
//	
//	public TableLinksGraphBO getGraph() {
//		TableLinksGraphBO graph = new TableLinksGraphBO();		
//		graph.setAllRelations(changeITableLink2AbstractLinkBO(this.linksBO.getAllLinks()));
//		graph.setRelatedField(this.getRelatedFields());
//		graph.execute();
//		return graph;
//	}
//	
//	public boolean isReachable(String bizViewId) {
//		TableLinksGraphBO graph = getGraph();
//		List<AbstractTable> abstractTableList = changeID2AbstractTable(graph.getAllRelatedTableID());
//		for ( AbstractTable at : abstractTableList ) {
//			if ( at instanceof BusinessViewBO ) {
//				BusinessViewBO bizViewBO = (BusinessViewBO)at;
//				if (bizViewBO.isReachable(bizViewId)) {
//					return true;
//				} else {
//					continue;
//				}
//			}
//		}
//		return false;
//	}
//
//	public String getBizThemeId() {
//		return bizThemeId;
//	}
//
//	public void setBizThemeId(String bizThemeId) {
//		this.bizThemeId = bizThemeId;
//	}
	
}
