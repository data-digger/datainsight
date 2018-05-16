package com.datadigger.datainsight.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datadigger.datainsight.bean.DefaultParameter;
import com.datadigger.datainsight.bean.GridData;
import com.datadigger.datainsight.bean.JDBCTable;
import com.datadigger.datainsight.bean.CellData;
import com.datadigger.datainsight.bean.DateParameter;
import com.datadigger.datainsight.bean.ListParameter;
import com.datadigger.datainsight.bean.ParameterValue;
import com.datadigger.datainsight.bean.TreeNode;
import com.datadigger.datainsight.bean.ParamGridData;
import com.datadigger.datainsight.domain.ReportData;
import com.datadigger.datainsight.domain.BizView;
import com.datadigger.datainsight.domain.Chart;
import com.datadigger.datainsight.domain.ChartData;
import com.datadigger.datainsight.domain.DataSource;
import com.datadigger.datainsight.domain.DomainType;
import com.datadigger.datainsight.domain.Report;
import com.datadigger.datainsight.domain.DataTable;
import com.datadigger.datainsight.domain.TableData;
import com.datadigger.datainsight.exception.DataDiggerErrorCode;
import com.datadigger.datainsight.exception.DataDiggerException;
import com.datadigger.datainsight.domain.Parameter;
import com.datadigger.datainsight.domain.BizViewColumn;
import com.datadigger.datainsight.query.SQLExecutor;
import com.datadigger.datainsight.repository.BizViewRepository;
import com.datadigger.datainsight.repository.ChartRepository;
import com.datadigger.datainsight.repository.DataSourceRepository;
import com.datadigger.datainsight.repository.ReportRepository;
import com.datadigger.datainsight.util.ConnectionPool;
import com.datadigger.datainsight.util.StringUtil;

import com.datadigger.datainsight.repository.BizViewColumRepository;
import com.datadigger.datainsight.repository.DataTableRepository;
import com.datadigger.datainsight.repository.ParameterRepository;
@Service
public class MetaDataService  {
	private static final Logger log = Logger.getLogger(MetaDataService.class);
	@Autowired
	private DataSourceRepository dastaSourceRespository;
    @Autowired
    private BizViewRepository bizViewRespository;
    @Autowired
    private ChartRepository chartRespository;
    @Autowired
    private ReportRepository reportRespository;
    @Autowired
    private DataTableRepository dataTableRepository;
    @Autowired
    private ParameterRepository parameterRepository;
    @Autowired
    private BizViewColumRepository bizViewColumRepository;
	private MetaDataService() {
	}

	public Iterable<DataSource> listAllDataSource(){
		return dastaSourceRespository.findAll();
	}
	public Iterable<BizView> listAllBizView(){
		return bizViewRespository.findAll();
	}
	public Iterable<Chart> listAllChart(){
		return chartRespository.findAll();
	}
	public Iterable<Report> listAllReport(){
		return reportRespository.findAll();
	}
	public Iterable<DataTable> listAllDataTable(){
		return dataTableRepository.findAll();
	}
	public Iterable<Parameter> listAllParameter(){
		return parameterRepository.findAll();
	}

	public DataSource saveDataSource(DataSource dataSource) {
		dataSource.setId(DomainType.DS.getDomainIDPrefix() + dataSource.getName());
		dastaSourceRespository.save(dataSource);
        log.debug("Create DataSource -- "+ dataSource.getName());
		return dataSource;
	}
	/*
	 * 保存查询器对象
	 */
	public BizView saveBizView(String bizViewJSON,String columsJSON) {
		BizView bizView = new BizView();
		JSONObject o = (JSONObject) JSON.parse(bizViewJSON);
		bizView.setName(o.getString("name"));
		bizView.setAlias(o.getString("alias"));
		bizView.setDesc(o.getString("desc"));
		bizView.setDefineJSON(o.getString("defineJSON"));
		bizView.setDataSourceId(o.getString("dataSourceId"));
		bizView.setId(DomainType.BZ.getDomainIDPrefix() + bizView.getName());
		bizViewRespository.save(bizView);
		saveBizViewColumns(bizView.getId(), columsJSON);	//保存列详细信息
		log.debug("Create BizView -- "+ bizView.getName());
		return bizView;
	}
	public GridData getGridData(String bizViewId) {
		BizView bizView = bizViewRespository.findOne(bizViewId);
    		String dataSourceId = bizView.getDataSourceId();
    		DataSource ds = dastaSourceRespository.findOne(dataSourceId);
    		return SQLExecutor.execute(ds, bizView.getDefineJSON());
	}
	
    
    public Chart saveChart(Chart chart) {
		chart.setId(DomainType.CR.getDomainIDPrefix() + chart.getName());
		chartRespository.save(chart);
		log.debug("Create Chart -- "+ chart.getName());
		return chart;
	}
    
    public DataTable saveDataTable(DataTable dataTable) {
    	dataTable.setId(DomainType.DT.getDomainIDPrefix() + dataTable.getName());
    	dataTableRepository.save(dataTable);
		log.debug("Create dataTable -- "+ dataTable.getName());
		return dataTable;
	}
    
    public Report saveReport(Report report) {
    	report.setId(DomainType.RP.getDomainIDPrefix() + report.getName());
    	reportRespository.save(report);
		log.debug("Create report -- "+ report.getName());
		return report;
	}
    
    public Parameter saveParameter(Parameter parameter) {
    	parameter.setId(DomainType.PA.getDomainIDPrefix() + parameter.getName());
    	parameterRepository.save(parameter);
		log.debug("Create parameter -- "+ parameter.getName());
		return parameter;
	}
    
    public Report getReport(String reportID) {
    	
	    	Report r = reportRespository.findOne(reportID);
	    	
	    	log.debug(r.toJSON());
	    	return r;
	}

    public ChartData getChartData(String chartID) {
    	       Chart chart = chartRespository.findOne(chartID);
    	       String bizViewId = chart.getBizViewId();
    	       String defineJSON = chart.getDefineJSON();
    	       JSONObject jsonObject = (JSONObject) JSON.parse(defineJSON);
    	       String filterJSON = jsonObject.getString("filters");
    	       GridData gd = previewChartData(bizViewId, filterJSON);
    	       ChartData cd = new ChartData(chart);
    	       cd.setData(gd);
    	       return cd;
    }
    
//    public ParamGridData getTableData(String tableID,String JSONParam) {
//	       DataTable table = dataTableRepository.findOne(tableID);
//	       String bizViewId = table.getBizViewId();
//	       ParamGridData pd = new ParamGridData();
//	       if(StringUtil.isNullOrEmpty(JSONParam)) {
//	    	   		pd = getParamGridData(bizViewId);		 
//			} else {
//				pd = updateBizViewData(bizViewId,JSONParam);
//			}
//	       return pd;
 //   }
//    public TableData getReptTable(String tableID,String portletID,String JSONparam) {
//    	 	 DataTable dt = getTable(tableID);
//    	 	 ParamGridData gd = getTableData(tableID,JSONparam);
//		 TableData td = new TableData(dt);
//		 td.setData(gd);
//		 td.setPortletID(portletID);
//	     return td;
//    }
    
    /* 根据defineJSON获取报表数据(不填充控件候选值列表)
     * globalFilter:[{name:名称
			alias:别名
			type:类型
			value:过滤值
			related:[{chartId: , field: , mark:}]}] 关联字段：图表ID，字段名，符号
     */
    public ReportData updateReportData(String reportDefine) {
//		 Report r = getReport(reportID);
//		 String defineJSON = r.getDefineJSON();
//    	 String defaultDefine = setDefaultDateInFilter(reportDefine);
		 ReportData rd = new ReportData();
		 JSONObject o = (JSONObject) JSON.parse(reportDefine);
		 JSONArray globalFilter = o.getJSONObject("header").getJSONArray("globalFilter");
		 JSONObject content = o.getJSONObject("content");
		 JSONArray portlets = content.getJSONArray("portlets");
		 List<ChartData> chartData = new ArrayList<ChartData>();
		 List<TableData> tableData = new ArrayList<TableData>();
		 for (int i = 0; i < portlets.size(); i++) {
			 JSONObject portlet = portlets.getJSONObject(i);
			 String portletID = portlet.getString("portletID");
			 JSONArray tabs = portlet.getJSONArray("tabs");
			 for(int j = 0; j < tabs.size(); j++) {
				 JSONObject jobj = tabs.getJSONObject(j);
				 String objtype = jobj.getString("objtype");
				 String objid = jobj.getString("objid");
				 if(objtype.equals("Table")) {
//					 TableData td = getReptTable(objid,portletID,null);
//					 tableData.add(td);
//					 dpList = td.getData().getDefaultParameters();	//获取Table的参数对象
				 } else {
					   Chart chart =  chartRespository.findOne(objid);
					   String bizViewId = chart.getBizViewId();
					   ChartData cd = new ChartData(chart);
					   cd.setPortletID(portletID);
					   JSONObject repChartDefine = combineFilters(chart,globalFilter);
					   String repChartFilter = repChartDefine.getString("filters");
					   GridData gd = previewChartData(bizViewId,repChartFilter);
					   cd.setData(gd);
					   chartData.add(cd);
				 }
			 }
			 
		 }
		 //Map<String,List<String>> standBy = getAllReportStandByValue(globalFilter);
		 rd.setChartData(chartData);
		 rd.setTableData(tableData);
		 //rd.setDefineJSON(defaultDefine);
		 return rd;
	}
    /* 根据报表ID获取报表数据
     * 
     */
    public ReportData initReportById(String reportID) {
		 Report r = getReport(reportID);
		 String defineJSON = r.getDefineJSON();
		 ReportData rd = initReportByJSON(defineJSON);
		 rd.setId(r.getId());
		 rd.setName(r.getName());
		 rd.setAlias(r.getAlias());
		 return rd;
    }
    /* 根据报表根据defineJSON获取报表数据(填充控件候选值列表)
     * 
     */
    public ReportData initReportByJSON(String defineJSON) {
		 String defaultDefine = setDefaultDateInFilter(defineJSON); //对报表的defineJSON添加时间默认值
		 JSONObject o = (JSONObject) JSON.parse(defaultDefine);
		 JSONArray globalFilter = o.getJSONObject("header").getJSONArray("globalFilter");
		 Map<String,List<String>> standBy = getAllReportStandByValue(globalFilter); 
		 ReportData rd = updateReportData(defaultDefine);
		 rd.setDefineJSON(defaultDefine);
		 rd.setStandbyValueMap(standBy);
		 return rd;
  }
    
	public Chart getChart(String chartID) {
		Chart chart = chartRespository.findOne(chartID);
		
		return chart;
	}
	public DataTable getTable(String tableID) {
		DataTable table = dataTableRepository.findOne(tableID);	
		return table;
	}
	
	public List<TreeNode> getFields(Connection conn, String schema, String tableName) throws SQLException{
		List<TreeNode> fieldList = new ArrayList<TreeNode>(); 
		ResultSet rs = conn.getMetaData().getColumns(conn.getCatalog(), schema, tableName, "%");
		while ( rs.next()){
			TreeNode treeNode = new TreeNode();
			treeNode.setTitle(rs.getString("COLUMN_NAME"));
			fieldList.add(treeNode);
			
		}
		return fieldList;
	}
	public List<TreeNode> getTables(String dsId) {
		DataSource ds = dastaSourceRespository.findOne(dsId);
		Connection conn = null;
		PreparedStatement prep = null;
		String dbType = ds.getDriverType();
		String charset = ds.getDbCharset();
	    List<String> schemas = new ArrayList<String>();
	    String schema = null;
	    List<TreeNode> result = new ArrayList<TreeNode>();
		try {
			conn = ConnectionPool.getInstance().getConnection(ds);
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet schemasRS = meta.getSchemas();
			while (schemasRS.next()) {
				schemas.add(schemasRS.getString("TABLE_SCHEM"));
			}
			if(schemas.isEmpty()){
				schema = null;
			}else{
				schema = schemas.get(0);
			}
			String[] types = new String[] {"TABLE","VIEW", "MATERIALIZED QUERY TABLE", "SYNONYM", "ALIAS"};
			ResultSet rs = meta.getTables(conn.getCatalog(), schema, "%", types);
			ResultSetMetaData rsMeta = rs.getMetaData();
			boolean hasRemarks = false;
			
			for (int i = 1; i <= rsMeta.getColumnCount(); i++)
				if (rsMeta.getColumnName(i).equalsIgnoreCase("REMARKS"))
					hasRemarks = true;
			
			while (rs.next()) {
				String type = rs.getString("TABLE_TYPE");
				
				if (type != null && type.trim().toUpperCase().equals("TABLE")){
					TreeNode treeNode = new TreeNode();
					String tableName = rs.getString("TABLE_NAME");
					treeNode.setTitle(tableName);
					treeNode.setChildren(getFields(conn,schema,tableName));
					result.add(treeNode);
				}
				
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	//获取查询器数据的总记录数
	public int getTotalCount (DataSource ds,String sqlStament) {
		StringBuffer countBuffer = new StringBuffer("select count(*) from (");
		countBuffer.append(sqlStament);
		countBuffer.append(") t");
		String countSql =  countBuffer.toString() ;
		GridData rs = SQLExecutor.execute(ds, countSql);
		return rs.get(0, 0).getIntValue();
	}
	//指定每页记录数和当前页进行分页查询
	public GridData getPageDate(DataSource ds,String sqlStament,int pageSize,int currentPage) {
		int totalCount = getTotalCount(ds,sqlStament);
		int totalPage = (int)Math.ceil(totalCount/(pageSize*1.0));	        
        if (currentPage < 1){
        		currentPage = 1;
        }        
        if (currentPage > totalPage){
        		currentPage = totalPage;
        }  
      //由(pages-1)*limit算出当前页面第一条记录，由limit查询limit条记录。则得出当前页面的记录
        //String pageSql = "select * from ("+sqlStament+") t limit "+ (currentPage - 1) * pageSize + "," + pageSize;
        StringBuffer pageBuffer = new StringBuffer("select * from (");
        pageBuffer.append(sqlStament);
        pageBuffer.append(") t limit ");
        pageBuffer.append((currentPage - 1) * pageSize);
        pageBuffer.append(",");
        pageBuffer.append(pageSize);
        String pageSql = pageBuffer.toString();
        GridData gd = SQLExecutor.execute(ds, pageSql);
        gd.setTotalRowsCount(totalCount);
        return gd;
	}
	/*
	 * 根据sql语句预览查询器数据
	 */
	public GridData getBizViewData(String dateSourceId,String sqlStament,String pageSize) {
    		DataSource ds = dastaSourceRespository.findOne(dateSourceId);
    		int intPageSize = Integer.parseInt(pageSize);
    		return getPageDate(ds,sqlStament,intPageSize,1);
	}
	/*
	 * 根据查询器id预览查询器数据(包括原始字段和计算字段数据)
	 */
	public GridData getBizViewData(String bizViewId,String pageSize) {
		BizView bv = bizViewRespository.findOne(bizViewId);
		String dateSourceId = bv.getDataSourceId();
//		String bizViewSql = bv.getDefineJSON();
//		List<BizViewColumn> columnList = bizViewColumRepository.findColumnsNotAggregation(bizViewId);
//		StringBuffer sqlStatementBuffer = new StringBuffer("select ");
//		for(int i=0; i<columnList.size();i++) {
//			String name = columnList.get(i).getColumnName();
//			String alias = columnList.get(i).getColumnAlias();
//			String expression = columnList.get(i).getExpression();
//			int category = columnList.get(i).getCategory();
//			if(i==0) {
//				if(category == 0) {
//					sqlStatementBuffer.append(name);
//					sqlStatementBuffer.append(" as ");
//					sqlStatementBuffer.append(alias);		
//				} else {
//					sqlStatementBuffer.append(expression);
//					sqlStatementBuffer.append(" as ");
//					sqlStatementBuffer.append(alias);
//				}
//			} else {
//				if(category == 0) {
//					sqlStatementBuffer.append(", ");
//					sqlStatementBuffer.append(name);
//					sqlStatementBuffer.append(" as ");
//					sqlStatementBuffer.append(alias);		
//				} else {
//					sqlStatementBuffer.append(", ");
//					sqlStatementBuffer.append(expression);
//					sqlStatementBuffer.append(" as ");
//					sqlStatementBuffer.append(alias);
//				}
//			}
//		}
//		sqlStatementBuffer.append(" from (");
//		sqlStatementBuffer.append(bizViewSql);
//		sqlStatementBuffer.append(") t1");
		String sqlStament = getBizViewStatment(bizViewId);
		GridData gd = getBizViewData(dateSourceId,sqlStament,pageSize);
		return gd;
	}
	/*
	 * JSON对象转成BizViewColum
	 * isUpdate:JSON对象里是否包含bizViewId
	 */
	public BizViewColumn jsonObjectToBizViewColumn(JSONObject bcObject, boolean isUpdate) {
		BizViewColumn bc = new BizViewColumn();
		bc.setColumnName(bcObject.getString("columnName"));
		bc.setColumnAlias(bcObject.getString("columnAlias"));
		bc.setColumnType(bcObject.getString("columnType"));
		bc.setCountDistinct(bcObject.getIntValue("countDistinct"));
		bc.setFilterable(bcObject.getIntValue("filterable"));
		bc.setGroupby(bcObject.getIntValue("groupby"));
		bc.setMax(bcObject.getIntValue("max"));
		bc.setMin(bcObject.getIntValue("min"));
		bc.setSum(bcObject.getIntValue("sum"));
		bc.setCategory(bcObject.getIntValue("category"));
		if(bc.getCategory() != 0) {
			bc.setExpression(bcObject.getString("expression"));
		}
		if(isUpdate) {
			bc.setBizViewId(bcObject.getString("bizViewId"));
			bc.setId(bcObject.getString("id"));
		}
		return bc;
	}
	/*
	 * 保存查询器列信息
	 */
	public void saveBizViewColumns(String bizViewId, String columnsJSON) {
		JSONArray bcList = JSONArray.parseArray(columnsJSON);	
		List<BizViewColumn> bizViewColumns = new ArrayList<BizViewColumn>();
		for(int i=0;i<bcList.size();i++) {
			JSONObject bcObject = bcList.getJSONObject(i);
			BizViewColumn bc = jsonObjectToBizViewColumn(bcObject,false);
			bc.setBizViewId(bizViewId);			
			String id = bc.getBizViewId()+"_"+bc.getColumnName();
			bc.setId(id);
			//log.debug("Create BizViewColumn -- "+ id);
			bizViewColumns.add(bc);
		}
		bizViewColumRepository.save(bizViewColumns);
	}
	
	/*
	 * 根据bizViewID获取查询器的所有字段信息
	 */
	public List<BizViewColumn> getBizViewColumns(String bizViewId) {
		List<BizViewColumn> bcList = bizViewColumRepository.findByBizViewId(bizViewId);
		return bcList;
	}
	/*
	 * 预览计算字段数据
	 */
	public GridData getCalculatedFieldType(String expression,String bizViewSql,String dataSourceId) {
		StringBuffer sqlBuffer = new StringBuffer("select ");
		sqlBuffer.append(expression);
		sqlBuffer.append(" from (");
		sqlBuffer.append(bizViewSql);
		sqlBuffer.append(") t");		
		String sqlStatement = sqlBuffer.toString();
		String pageSize = "5";
		GridData gd = getBizViewData(dataSourceId,sqlStatement,pageSize);
		return gd;
	}
	/*
	 * 删除查询器字段
	 */
	public void deleteBizViewColumn(String columnsJSON) {
		JSONArray bcList = JSONArray.parseArray(columnsJSON);	
		List<BizViewColumn> bizViewColumns = new ArrayList<BizViewColumn>();
		for(int i=0;i<bcList.size();i++) {
			JSONObject bcObject = bcList.getJSONObject(i);
			BizViewColumn bc = jsonObjectToBizViewColumn(bcObject,true);
			bizViewColumns.add(bc);
		}
		bizViewColumRepository.delete(bizViewColumns);
	}
	/*
	 * 预览图表数据
	 */
	public GridData previewChartData(String bizViewId, String filterJSON) {
		BizView bv = bizViewRespository.findOne(bizViewId);
		String dateSourceId = bv.getDataSourceId();
		DataSource ds = dastaSourceRespository.findOne(dateSourceId);
		String sqlStatement = getChartSql(bizViewId,filterJSON);
		GridData gd = SQLExecutor.execute(ds, sqlStatement);	
		return gd;
	}
	/*
	 * 获取图表SQL
	 * filterJSON:{
	 *  value:[columnName],(必须字段)
	 *  groupby:columnName/null(卡式图表不需要groupby字段，其余图表必须),
	 *  isgroupy:true/false(是否以groupby字段进行分组)
	 *  orderby:{field:字段名,type:ASC/DESC},
	 *  limit:10,
	 *  where:[{field:columnName,mark:'=',value:10}]
	 * }
	 */
	public String getChartSql(String bizViewId, String filterJSON) {
		
		JSONObject filterObject = (JSONObject) JSON.parse(filterJSON);
		String groupby = filterObject.getString("groupby");
		boolean isGroupBy = filterObject.getBooleanValue("isgroupby");
		int limit = filterObject.getIntValue("limit");
	
		JSONArray valueObjectList = filterObject.getJSONArray("value");
		JSONArray whereObjectList = filterObject.getJSONArray("where");
		
		JSONObject orderbyObject = filterObject.getJSONObject("orderby");
		String orderbyField = orderbyObject.getString("field");
		String orderbyType = orderbyObject.getString("type");
		
		String bq = getBizViewStatment(bizViewId);		
		
		Map<String,String> whereMap = analyzeContentFilter(whereObjectList,bizViewId);
		String whereClause = whereMap.get("whereClause");
		String havingClause = whereMap.get("havingClause");
		String selectClaus = getSelectClause(groupby,valueObjectList,bizViewId);
		
		String finalClause = "("+bq;
		if (!whereClause.isEmpty()) {
			finalClause = finalClause + " where" + whereClause;
		}
		finalClause = finalClause+") t";
		
		
		finalClause = selectClaus + finalClause;
		
		if(isGroupBy==true && !StringUtil.isNullOrEmpty(groupby)) {
			finalClause = finalClause+ " group by "+groupby;
		}
				
		if (!havingClause.isEmpty()) {
			finalClause = finalClause + " having" + havingClause;
		}
		if(!orderbyField.isEmpty()) {
			finalClause = finalClause +" order by "+orderbyField+" "+orderbyType;
		}
		if(limit > 200) {
			limit = 200;
		}
		finalClause = finalClause+" limit "+limit;
		return finalClause;
		
	}
	/*
	 * 解析内容过滤语句
	 * 解析content内容，生成where子句和having子句
	 * 当content字段为原始字段或者计算字段时，组装字段名+符号(mark)+值(value)放入where子句
	 * 当content字段聚合函数时组装字段名+符号(mark)+值(value)放入having子句
	 * 
	 */
	public Map<String,String> analyzeContentFilter(JSONArray whereObjectList,String bizViewId){
		Map<String,String> re = new HashMap<String,String>(2);
		String whereClause="";
		String havingClause="";
		List<JSONObject> whereList = new ArrayList<JSONObject> ();
		List<JSONObject> havingList = new ArrayList<JSONObject> ();
		for(int i=0; i<whereObjectList.size(); i++) {
			JSONObject whereObject = whereObjectList.getJSONObject(i);
			String whereField = whereObject.getString("field");
			if(!StringUtil.isNullOrEmpty(whereField)) {
				BizViewColumn bc = bizViewColumRepository.findByBizViewIdAndColumnName(bizViewId, whereField);
				int category = bc.getCategory();
				if(category == 0 || category == 1) {
					whereList.add(whereObject);
				} else {
					havingList.add(whereObject);
				}
			}			
		}
		for(int j=0; j<whereList.size(); j++) {
			JSONObject where = whereList.get(j);
			String name = where.getString("field");
			String mark = where.getString("mark");
			String value = formatValue(where,mark);
			if(!name.isEmpty()) {
				if(j==0) {
					whereClause = whereClause + " "+name+" "+mark+" "+value;
				} else {
					whereClause = whereClause + " and "+name+" "+mark+" "+value;
				}
			}	
		}
		re.put("whereClause", whereClause);
		for(int k=0; k<havingList.size(); k++) {
			JSONObject having = havingList.get(k);
			String name = having.getString("field");
			String mark = having.getString("mark");
			String value = formatValue(having,mark);
			if(!name.isEmpty()) {
				if(k==0) {
					havingClause = havingClause + " "+name+" "+mark+" "+value;
				} else {
					havingClause = havingClause + " and "+name+" "+mark+" "+value;
				}
			}	
		}
		re.put("havingClause", havingClause);
		return re;
	}
	
	/*
	 * 格式化过滤值 IN [1,2,3] => IN ('1','2','3')
	 * 			  其他 1 => '1'
	 *
	 */
	public String formatValue(JSONObject where,String mark) {
		String value = "";
		try {
			if(mark.equals("IN") || mark.equals("NOT IN")) {
				JSONArray inValues = where.getJSONArray("value");
				StringBuffer inBuffer = new StringBuffer("(");
				for(int k=0; k<inValues.size();k++) {
					String inValue = inValues.getString(k);
					if(k == 0) {
						inBuffer.append(formatValue(inValue));
					} else {
						inBuffer.append(",");
						inBuffer.append(formatValue(inValue));
					}
				}
				inBuffer.append(")");
				value = inBuffer.toString();
			} else {
				StringBuffer inBuffer = new StringBuffer();
				inBuffer.append(formatValue(where.getString("value")));
				value = inBuffer.toString();
			}
			return value;
		}catch(Exception e) {
			throw new DataDiggerException(DataDiggerErrorCode.UNKOWN_ERROR,e).setDetail("where:" + where.toString() + ";mark:" + mark);
		}
	}
	/*
	 * 格式化过滤值 1 => '1'
	 * 
	 *
	 */
	public String formatValue(String value) {
		StringBuffer valueBuffer = new StringBuffer("'");
		valueBuffer.append(value);
		valueBuffer.append("'");
		return valueBuffer.toString();
	}
	/*
	 * 根据查询器id组装原始字段和计算字段sql
	 */
	public String getBizViewStatment(String bizViewId) {
		BizView bv = bizViewRespository.findOne(bizViewId);
		String bizViewSql = bv.getDefineJSON();
		List<BizViewColumn> columnList = bizViewColumRepository.findColumnsNotAggregation(bizViewId);
		StringBuffer sqlStatementBuffer = new StringBuffer("select ");
		for(int i=0; i<columnList.size();i++) {
			String name = columnList.get(i).getColumnName();
			String expression = columnList.get(i).getExpression();
			int category = columnList.get(i).getCategory();
			if(i==0) {
				if(category == 0) {
					sqlStatementBuffer.append(name);
					sqlStatementBuffer.append(" as ");
					sqlStatementBuffer.append(name);		
				} else {
					sqlStatementBuffer.append(expression);
					sqlStatementBuffer.append(" as ");
					sqlStatementBuffer.append(name);
				}
			} else {
				if(category == 0) {
					sqlStatementBuffer.append(", ");
					sqlStatementBuffer.append(name);
					sqlStatementBuffer.append(" as ");
					sqlStatementBuffer.append(name);		
				} else {
					sqlStatementBuffer.append(", ");
					sqlStatementBuffer.append(expression);
					sqlStatementBuffer.append(" as ");
					sqlStatementBuffer.append(name);
				}
			}
		}
		sqlStatementBuffer.append(" from (");
		sqlStatementBuffer.append(bizViewSql);
		sqlStatementBuffer.append(") t1");
		String sqlStament = sqlStatementBuffer.toString();
		return sqlStament;
	}
	/*
	 * 根据groupby字段和value字段组装select语句
	 * 
	 */
	public String getSelectClause(String groupby,JSONArray valueObjectList,String bizViewId) {
		BizViewColumn groupbyColumn = null;
		List<BizViewColumn> valueList = new ArrayList<BizViewColumn>(valueObjectList.size());
		if(!StringUtil.isNullOrEmpty(groupby)) {
			groupbyColumn = bizViewColumRepository.findByBizViewIdAndColumnName(bizViewId, groupby);
		}		
		for(int i=0; i<valueObjectList.size(); i++) {
			String valueName = valueObjectList.getString(i);
			if(!StringUtil.isNullOrEmpty(valueName)) {
				BizViewColumn value = bizViewColumRepository.findByBizViewIdAndColumnName(bizViewId, valueName);;
				valueList.add(value);
			}
		}
		return analyzeSelectClause(groupbyColumn,valueList);
	}
	public String analyzeSelectClause(BizViewColumn groupby, List<BizViewColumn> valueList) {
		String selectClause = "select ";
		if(groupby!=null) {
			if(groupby.getCategory() == 0 || groupby.getCategory() == 1) {
				selectClause = selectClause + groupby.getColumnName() + ",";
			} else {
				selectClause = selectClause+groupby.getExpression()+" as "+groupby.getColumnName()+ ",";
			}	
		}
		for(int i=0; i<valueList.size(); i++) {
			BizViewColumn value = valueList.get(i);
			if(i==0) {
				if(value.getCategory() == 0) {
					selectClause = selectClause+value.getColumnName()+" as "+value.getColumnName();
				} else {
					selectClause = selectClause+value.getExpression()+" as "+value.getColumnName();
				}	
			} else {
				if(value.getCategory() == 0) {
					selectClause = selectClause+", "+value.getColumnName()+" as "+value.getColumnName();
				} else {
					selectClause = selectClause+", "+value.getExpression()+" as "+value.getColumnName();
				}	
			}
		}
		selectClause = selectClause+" from ";
		return selectClause;
	}
	
	/* 
     * 获取过滤列表候选值(最大候选数为200)
     */ 
	public List<String> getStandByValue(String bizViewId,String columnName){
		List<String> result = new ArrayList<String>();
		BizView bv = bizViewRespository.findOne(bizViewId);
		String bq = getBizViewStatment(bizViewId);
		String dateSourceId = bv.getDataSourceId();
		DataSource ds = dastaSourceRespository.findOne(dateSourceId);
		StringBuffer sqlBuffer = new StringBuffer("select distinct ");
		sqlBuffer.append(columnName);
		sqlBuffer.append(" from (");
		sqlBuffer.append(bq);
		sqlBuffer.append(") t limit 200");
		String sql = sqlBuffer.toString();
		GridData gd = SQLExecutor.executeAllString(ds, sql);	
		List<List<CellData>> cellDataList = gd.getData();
		for(int i=0; i<cellDataList.size(); i++) {
			result.add(cellDataList.get(i).get(0).getStringValue());
		}
		return result;		
	}
	
	/* 
     * 获取报表全局过滤（单选和多选）的候选值列表(查看报表时需要获取全部单选和多选控件的候选值列表)
     */ 
	public Map<String,List<String>> getAllReportStandByValue(JSONArray gfList){
		Map<String,List<String>> result = new HashMap<String,List<String>>();
		for(int i=0; i<gfList.size(); i++) {
			JSONObject o = gfList.getJSONObject(i);
			String otype = o.getString("type");
			if (otype.equals("singleSelect") || otype.equals("multiSelect")) {	//控件类型为单选或者多选时要获取该字段的候选值列表供用户选择
				JSONArray relatedList = o.getJSONArray("related");
				List<String> standby = getReportStandByValue(relatedList);
				JSONObject related = relatedList.getJSONObject(0);  
				String field = related.getString("field");
				result.put(field, standby);
			}
		}
		return result;
	}
	public List<String> getReportStandByValue(JSONArray relatedList){
		JSONObject related = relatedList.getJSONObject(0);  //以第一个关联字段为准获取该字段的候选值列表
		String chartId = related.getString("chartId");
		String field = related.getString("field");
		Chart chart = chartRespository.findOne(chartId);
		String bizViewId = chart.getBizViewId();
		List<String> standby =  getStandByValue(bizViewId,field);
		return standby;
	}
	public List<String> getReportStandByValue(String relatedJSON){
		JSONArray relatedList = JSON.parseArray(relatedJSON);
		List<String> standby =  getReportStandByValue(relatedList);
		return standby;
	}
	/*合并全局过滤条件和图表私有过滤条件
     * globalFilter:[{name:名称
			alias:别名
			type:类型
			value:过滤值
			related:[{chartId: , field: , mark:}]}] 关联字段：图表ID，字段名，符号
     */
	public JSONObject combineFilters(Chart chart, JSONArray globalFilter) {
		String chartDefine = chart.getDefineJSON();
		if(globalFilter.size() == 0) {
			return (JSONObject) JSON.parse(chartDefine);
		} else {
			String chartId = chart.getId();
			JSONObject chartDefineObject = (JSONObject) JSON.parse(chartDefine);
			JSONObject chartFilter = chartDefineObject.getJSONObject("filters");
			JSONArray whereList = chartFilter.getJSONArray("where");
			for(int i=0; i<globalFilter.size(); i++) {
				JSONObject gfo = globalFilter.getJSONObject(i);
				String gValue = gfo.getString("value");
				JSONArray relatedFields = gfo.getJSONArray("related");
				for(int j=0; j<relatedFields.size(); j++) {
					JSONObject relatedField = relatedFields.getJSONObject(j);
					String rid = relatedField.getString("chartId");
					if(rid.equals(chartId)) {
						String rField = relatedField.getString("field");
						String rMark = relatedField.getString("mark");
						for(int k=0; k<whereList.size(); k++) {
							JSONObject whereObject = whereList.getJSONObject(k);
							String wField = whereObject.getString("field");
							String wMark = whereObject.getString("mark");
							if(wField.equals(rField) && wMark.equals(rMark)) {
								whereObject.put("value", gValue);
							}
						}
					}
				}
			}
			//String reportChartDefine = chartDefineObject.toJSONString();
			return chartDefineObject;
		}
	}
	
	/*
	 * 获取默认日期值
	 * 日类型：2018-05-07
	 * 月类型：2018-05
	 * 自定义类型：前端用户自定义
	 */
	public String getDefaultDate(String dateType,int forward,String format,String value) {
			Date currentTime = new Date();
			String dayFormat = "yyyy-MM-dd";
			String monFormat = "yyyy-MM";
			//String userFormat = format;
			Calendar cal  =  Calendar.getInstance();
			cal.setTime(currentTime);
			String dateString;
			if(dateType.equals("DateByDay")) {
				SimpleDateFormat formatter = new SimpleDateFormat(dayFormat);
				cal.add(Calendar.DATE,-forward);
				dateString = formatter.format(cal.getTime());
			} else if(dateType.equals("DateByMonth"))  {
				SimpleDateFormat formatter = new SimpleDateFormat(monFormat);
				cal.add(Calendar.MONTH,-forward);
				dateString = formatter.format(cal.getTime());
			} else {
				//SimpleDateFormat formatter = new SimpleDateFormat(userFormat);
				//cal.add(Calendar.MONTH,-forward);
				dateString = value;
			}
			return dateString;
	}
	
	/*在初始化报表时，时间控件填入默认值
     * globalFilter:[{name:名称
			alias:别名
			type:类型
			value:过滤值
			related:[{chartId: , field: , mark:}]}] 关联字段：图表ID，字段名，符号
     */
	public String setDefaultDateInFilter(String reportDefine) {
		JSONObject rdo = (JSONObject) JSON.parse(reportDefine);
		JSONArray gfList = rdo.getJSONObject("header").getJSONArray("globalFilter");
		for(int i=0; i<gfList.size(); i++) {
			JSONObject o = gfList.getJSONObject(i);
			String otype = o.getString("type");
			String value = o.getString("value");
			if(!StringUtil.isNullOrEmpty(otype)) {
				if (otype.equals("DateByDay") || otype.equals("DateByMonth")) {
					String defaultDate = getDefaultDate(otype,1,null,value); 
					o.put("value", defaultDate);
				} else if(otype.equals("DateByUser")) {
					String format = o.getString("value");
					String defaultDate = getDefaultDate(otype,0,format,value); 
					o.put("value", defaultDate);
				}
			}
			
		}
		return rdo.toJSONString();
	}
}