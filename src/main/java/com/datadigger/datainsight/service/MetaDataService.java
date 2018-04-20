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
import java.util.HashSet;
import java.util.List;
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
	
	public Matcher matchRegEx (String sentence) {
		String regEx = "\\^.*?\\^";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(sentence);
		return matcher;
	}
	/*
	 * 获取带参数对象的查询器数据
	 */
    public ParamGridData getParamGridData(String bizViewId){
	    	BizView bizView = bizViewRespository.findOne(bizViewId);
	    	String dataSourceId = bizView.getDataSourceId();
	    	DataSource ds = dastaSourceRespository.findOne(dataSourceId);
	    	String defineJSON = bizView.getDefineJSON();
	    	List<String> paramList = new ArrayList<String>();	   
	    	List<Object> paramValue = new ArrayList<Object>();
	    	ParamGridData pd = new ParamGridData();
	    List<DefaultParameter> dpList = new ArrayList<DefaultParameter>();

	    Matcher matcher = matchRegEx(defineJSON);
	    // 查找字符串中匹配的正则表达式的字符/字符串
	    while(matcher.find()) { 
	    		String pStr = matcher.group();
	    		int len = pStr.length();
	    		String pId = pStr.substring(1, len-1);
	    		paramList.add(pId);
	   } 
	    String paramSql = matcher.replaceAll("?");   
	    
	    for(int i=0; i<paramList.size();i++) {
	    		DefaultParameter dp = new DefaultParameter();
	    		Parameter param = parameterRepository.findOne(paramList.get(i));
	    		String paramDefine = param.getDefineJSON();
	    		JSONObject o = (JSONObject) JSON.parse(paramDefine);
	    		String componetType = o.getString("componenttype");
	    		dp.setParamId(paramList.get(i));
	    		dp.setParamType(componetType);
	   		if(componetType.equals("date")) {  //日期控件默认值设置昨天
	   			DateParameter dateParam = new DateParameter();
	   			Date currentTime = new Date();
	   			String format = o.getString("formattype");
	   			SimpleDateFormat formatter = new SimpleDateFormat(format);
	   			Calendar cal  =  Calendar.getInstance();
	   			cal.setTime(currentTime);
	   			cal.add(Calendar.DATE,-1);
	   			String dateString = formatter.format(cal.getTime());
//	   			SimpleDateFormat formatter = new SimpleDateFormat(format);
//	   			String dateString = formatter.format(currentTime);
	   			dateParam.setDate(dateString);
	   			dateParam.setFormat(format);
	   			dp.setDefaultDate(dateParam);
	   			paramValue.add(dateString);
	   		} else if(componetType.equals("list")) {
	   			JSONObject defaultDefine = o.getJSONObject("defalutDefine");
	   			String dk = defaultDefine.getString("key");
	   			String dv = defaultDefine.getString("value");
	   			ListParameter lp = new ListParameter();
	   			lp.setKey(dk);
	   			lp.setValue(dv);
	   			dp.setDefaultListValue(lp);
	   			paramValue.add(dk);
	   		} else if(componetType.equals("Tree")) {
	   			
	   		}
	   		dpList.add(dp);
	    }
	      GridData data = SQLExecutor.execute(ds, paramSql, paramValue);
	      pd.setGridData(data);
	      pd.setDefaultParameters(dpList);
	      return pd;
	    	//return SQLExecutor.execute(ds, bizView.getDefineJSON());
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

    public ParamGridData getChartData(String chartID, String JSONParam) {
    	       Chart chart = chartRespository.findOne(chartID);
    	       String bizViewId = chart.getBizViewId();
    	       ParamGridData pd = new ParamGridData();
    	       if(StringUtil.isNullOrEmpty(JSONParam)) {
    	    	   		pd = getParamGridData(bizViewId);		 
    			} else {
    				pd = updateBizViewData(bizViewId,JSONParam);
    			}
    	       return pd;
    }
    
    public ParamGridData getTableData(String tableID,String JSONParam) {
	       DataTable table = dataTableRepository.findOne(tableID);
	       String bizViewId = table.getBizViewId();
	       ParamGridData pd = new ParamGridData();
	       if(StringUtil.isNullOrEmpty(JSONParam)) {
	    	   		pd = getParamGridData(bizViewId);		 
			} else {
				pd = updateBizViewData(bizViewId,JSONParam);
			}
	       return pd;
    }
    
    public ChartData getReptChart(String chartID,String portletID,String JSONparam) {
    		 Chart chart = getChart(chartID);
		 ChartData cd =  new ChartData(chart);
		 cd.setPortletID(portletID);
		 ParamGridData pd  = getChartData(chartID,JSONparam);
		 cd.setData(pd);
	     return cd;
    }
    public TableData getReptTable(String tableID,String portletID,String JSONparam) {
    	 	 DataTable dt = getTable(tableID);
    	 	 ParamGridData gd = getTableData(tableID,JSONparam);
		 TableData td = new TableData(dt);
		 td.setData(gd);
		 td.setPortletID(portletID);
	     return td;
    }

//	public ReportData getReportData(String reportID) {
//		 Report r = getReport(reportID);
//		 String defineJSON = r.getDefineJSON();
//		 ReportData rd = new ReportData(r);
//		 JSONObject o = (JSONObject) JSON.parse(defineJSON);
//		 JSONObject content = o.getJSONObject("content");
//		 JSONArray portlets = content.getJSONArray("portlets");
//		 List<ChartData> chartData = new ArrayList<ChartData>();
//		 List<TableData> tableData = new ArrayList<TableData>();
//		 for (int i = 0; i < portlets.size(); i++) {
//			 JSONObject portlet = portlets.getJSONObject(i);
//			 String portletID = portlet.getString("portletID");
//			 JSONArray tabs = portlet.getJSONArray("tabs");
//			 for(int j = 0; j < tabs.size(); j++) {
//				 JSONObject jobj = tabs.getJSONObject(j);
//				 String objtype = jobj.getString("objtype");
//				 String objid = jobj.getString("objid");
//				 if(objtype.equals("Table")) {
//					 TableData td = getReptTable(objid,portletID);
//					 tableData.add(td);
//				 } else {
//					 ChartData cd =  getReptChart(objid,portletID);
//					 chartData.add(cd);
//				 }
//				 
//			 }
//			 
//		 }
//		 rd.setChartData(chartData);
//		 rd.setTableData(tableData);
//		 return rd;
//	}
    public ReportData getReportData(String reportID) {
		 Report r = getReport(reportID);
		 String defineJSON = r.getDefineJSON();
		 ReportData rd = new ReportData(r);
		 JSONObject o = (JSONObject) JSON.parse(defineJSON);
		 JSONObject content = o.getJSONObject("content");
		 JSONArray portlets = content.getJSONArray("portlets");
		 List<ChartData> chartData = new ArrayList<ChartData>();
		 List<TableData> tableData = new ArrayList<TableData>();
		 Set<DefaultParameter> paramSet = new HashSet<DefaultParameter>();  
		 for (int i = 0; i < portlets.size(); i++) {
			 JSONObject portlet = portlets.getJSONObject(i);
			 String portletID = portlet.getString("portletID");
			 JSONArray tabs = portlet.getJSONArray("tabs");
			 for(int j = 0; j < tabs.size(); j++) {
				 List<DefaultParameter> dpList = new ArrayList<DefaultParameter>();
				 JSONObject jobj = tabs.getJSONObject(j);
				 String objtype = jobj.getString("objtype");
				 String objid = jobj.getString("objid");
				 if(objtype.equals("Table")) {
					 TableData td = getReptTable(objid,portletID,null);
					 tableData.add(td);
					 dpList = td.getData().getDefaultParameters();	//获取Table的参数对象
				 } else {
					 ChartData cd =  getReptChart(objid,portletID,null);
					 chartData.add(cd);
					 dpList = cd.getData().getDefaultParameters();	//获取Chart的参数对象
				 }
				 for(int k=0; k<dpList.size(); k++) {
					 paramSet.add(dpList.get(k));	//报表参数对象合并去重
				 }
			 }
			 
		 }
		 rd.setParameterSet(paramSet);
		 rd.setChartData(chartData);
		 rd.setTableData(tableData);
		 return rd;
	}
    
    public ReportData updateReportData(String reportID,String JSONparam) {
		 Report r = getReport(reportID);
		 String reportDefine = r.getDefineJSON();
		 ReportData rd = new ReportData(r);
		 JSONObject o = (JSONObject) JSON.parse(reportDefine);
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
					 TableData td = getReptTable(objid,portletID,JSONparam);
					 tableData.add(td);
				 } else {
					 ChartData cd =  getReptChart(objid,portletID,JSONparam);
					 chartData.add(cd);
				 }
			 }			 
		 }
		 rd.setChartData(chartData);
		 rd.setTableData(tableData);
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
	/*
	 * 获取参数候选值
	 */
	public ParameterValue getParameterValue(String paramId) {
		Parameter param = parameterRepository.findOne(paramId);
		String defineJSON = param.getDefineJSON();
		JSONObject o = (JSONObject) JSON.parse(defineJSON);
		String componetType = o.getString("componenttype");
		JSONObject standbyDefine = o.getJSONObject("standbyDefine");
		String valueSource = standbyDefine.getString("valueSource");
		ParameterValue pv = new ParameterValue();
		if(componetType.equals("list")) {  
			List<ListParameter> listValues = new ArrayList<ListParameter>();
   			if(valueSource.equals("static")) {
   				JSONArray valueList = standbyDefine.getJSONArray("values");
   				for(int i=0; i<valueList.size(); i++) {
   					ListParameter lp = new ListParameter();
   					JSONObject value = valueList.getJSONObject(i);
   					lp.setKey(value.getString("key"));
   					lp.setValue(value.getString("value"));
   					listValues.add(lp);
   				}
   				pv.setStandByList(listValues);
   			} else {
   				String dataSourceId = standbyDefine.getString("dataSourceID");
   		    		DataSource ds = dastaSourceRespository.findOne(dataSourceId);
   				JSONArray valueList = standbyDefine.getJSONArray("values");
   				String sql = valueList.getJSONObject(0).getString("value");
   				GridData gd = SQLExecutor.execute(ds, sql);
   				int rows = gd.getRowsCount();
   				for(int i=0; i<rows; i++) {
   					ListParameter lp = new ListParameter();
   					String key = gd.get(i, 0).getDisplayValue();
   					String value = gd.get(i, 1).getDisplayValue();
   					lp.setKey(key);
   					lp.setValue(value);
   					listValues.add(lp);
   				}
   				pv.setStandByList(listValues);
   			}
   		} else if(componetType.equals("Tree")) {

   		} 
		return pv;
		
	}
	/*
	 * 查询器获取参数更新后的数据
	 */
	public ParamGridData updateBizViewData(String bizViewId,String JSONparam) {
		JSONObject paramSelected = (JSONObject) JSON.parse(JSONparam);
		BizView bizView = bizViewRespository.findOne(bizViewId);
    		String dataSourceId = bizView.getDataSourceId();
    		DataSource ds = dastaSourceRespository.findOne(dataSourceId);
    		String sqlJSON = bizView.getDefineJSON();
    		List<Object> paramValues = new ArrayList<Object>();
    		ParamGridData pd = new ParamGridData();

    	    Matcher matcher = matchRegEx(sqlJSON);
    	    
    	    while(matcher.find()) { 
    	    		String pStr = matcher.group();
    	    		int len = pStr.length();
    	    		String pId = pStr.substring(1, len-1);
    	    		String pv = paramSelected.getString(pId);
    	    		paramValues.add(pv);
    	   } 
    	    String paramSql = matcher.replaceAll("?"); 
    	    GridData gd = SQLExecutor.execute(ds, paramSql, paramValues);
    	    pd.setGridData(gd);
		return pd;
	}
	
	/*
	 * 封装默认参数对象
	 */
	public DefaultParameter getDefaultParameter(String parameterId) {
		DefaultParameter dp = new DefaultParameter();
		Parameter param = parameterRepository.findOne(parameterId);
		String paramDefine = param.getDefineJSON();
		JSONObject o = (JSONObject) JSON.parse(paramDefine);
		String componetType = o.getString("componenttype");
		dp.setParamId(parameterId);
		dp.setParamType(componetType);
		if(componetType.equals("date")) {  //日期控件默认值设置为当日/当月/当年
			DateParameter dateParam = new DateParameter();
			Date currentTime = new Date();
			String format = o.getString("formattype");
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			String dateString = formatter.format(currentTime);
			dateParam.setDate(dateString);
			dateParam.setFormat(format);
			dp.setDefaultDate(dateParam);
		} else if(componetType.equals("list")) {
			JSONObject defaultDefine = o.getJSONObject("defalutDefine");
			String dk = defaultDefine.getString("key");
			String dv = defaultDefine.getString("value");
			ListParameter lp = new ListParameter();
			lp.setKey(dk);
			lp.setValue(dv);
			dp.setDefaultListValue(lp);
		} else if(componetType.equals("Tree")) {
			
		}
		return dp;
	}
	/*
	 * 解析获取查询器的参数对象
	 */
	public List<DefaultParameter> getParamets(String bizViewId){
		BizView bizView = bizViewRespository.findOne(bizViewId);
    		String sqlJSON = bizView.getDefineJSON();
    		List<DefaultParameter> dpList = new ArrayList<DefaultParameter>();
    	    Matcher matcher = matchRegEx(sqlJSON);
    	    while(matcher.find()) { 
    	    		String pStr = matcher.group();
    	    		int len = pStr.length();
    	    		String pId = pStr.substring(1, len-1);
    	    		dpList.add(getDefaultParameter(pId));
    	   } 
    	    return dpList;
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
		String bizViewSql = bv.getDefineJSON();
		List<BizViewColumn> columnList = bizViewColumRepository.findColumnsNotAggregation(bizViewId);
		StringBuffer sqlStatementBuffer = new StringBuffer("select ");
		for(int i=0; i<columnList.size();i++) {
			String name = columnList.get(i).getColumnName();
			String alias = columnList.get(i).getColumnAlias();
			String expression = columnList.get(i).getExpression();
			int category = columnList.get(i).getCategory();
			if(i==0) {
				if(category == 0) {
					sqlStatementBuffer.append(name);
					sqlStatementBuffer.append(" as ");
					sqlStatementBuffer.append(alias);		
				} else {
					sqlStatementBuffer.append(expression);
					sqlStatementBuffer.append(" as ");
					sqlStatementBuffer.append(alias);
				}
			} else {
				if(category == 0) {
					sqlStatementBuffer.append(", ");
					sqlStatementBuffer.append(name);
					sqlStatementBuffer.append(" as ");
					sqlStatementBuffer.append(alias);		
				} else {
					sqlStatementBuffer.append(", ");
					sqlStatementBuffer.append(expression);
					sqlStatementBuffer.append(" as ");
					sqlStatementBuffer.append(alias);
				}
			}
		}
		sqlStatementBuffer.append(" from (");
		sqlStatementBuffer.append(bizViewSql);
		sqlStatementBuffer.append(") t1");
		String sqlStament = sqlStatementBuffer.toString();
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
}