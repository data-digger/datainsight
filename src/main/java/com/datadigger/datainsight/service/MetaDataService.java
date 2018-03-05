package com.datadigger.datainsight.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datadigger.datainsight.bean.GridData;
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
import com.datadigger.datainsight.query.SQLExecutor;
import com.datadigger.datainsight.repository.BizViewRepository;
import com.datadigger.datainsight.repository.ChartRepository;
import com.datadigger.datainsight.repository.DataSourceRepository;
import com.datadigger.datainsight.repository.ReportRepository;
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

	public DataSource createDataSource(DataSource dataSource) {
		dataSource.setId(DomainType.DS.getDomainIDPrefix() + dataSource.getName());
		dastaSourceRespository.save(dataSource);
        log.debug("Create DataSource -- "+ dataSource.getName());
		return dataSource;
	}
	
	public BizView createBizView(BizView bizView) {
		bizView.setId(DomainType.BZ.getDomainIDPrefix() + bizView.getName());
		bizViewRespository.save(bizView);
		log.debug("Create BizView -- "+ bizView.getName());
		return bizView;
	}

    public GridData getGridData(String bizViewId){
    	BizView bizView = bizViewRespository.findOne(bizViewId);
    	String dataSourceId = bizView.getDataSourceId();
    	DataSource ds = dastaSourceRespository.findOne(dataSourceId);
    	return SQLExecutor.execute(ds, bizView.getDefineJSON());
    }
    
    public Chart createChart(Chart chart) {
		chart.setId(DomainType.CR.getDomainIDPrefix() + chart.getName());
		chartRespository.save(chart);
		log.debug("Create Chart -- "+ chart.getName());
		return chart;
	}
    
    public DataTable createDataTable(DataTable dataTable) {
    	dataTable.setId(DomainType.DT.getDomainIDPrefix() + dataTable.getName());
    	dataTableRepository.save(dataTable);
		log.debug("Create dataTable -- "+ dataTable.getName());
		return dataTable;
	}
    
    public Report createReport(Report report) {
    	report.setId(DomainType.RP.getDomainIDPrefix() + report.getName());
    	reportRespository.save(report);
		log.debug("Create report -- "+ report.getName());
		return report;
	}
    
    public Parameter createParameter(Parameter parameter) {
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

    public GridData getChartData(String chartID) {
    	       Chart chart = chartRespository.findOne(chartID);
    	       GridData gd = getGridData(chart.getBizViewId());
    	       return gd;
    }
    
    public GridData getTableData(String tableID) {
	       DataTable table = dataTableRepository.findOne(tableID);
	       GridData gd = getGridData(table.getBizViewId());
	       return gd;
    }
    
    public ChartData getReptChart(String chartID,String portletID) {
    		 Chart chart = getChart(chartID);
		 GridData gd  =getChartData(chartID);
		 ChartData cd =  new ChartData(chart);
		 cd.setPortletID(portletID);
		 cd.setGridData(gd);
	     return cd;
    }
    public TableData getReptTable(String tableID,String portletID) {
    	 	 DataTable dt = getTable(tableID);
		 GridData gd = getTableData(tableID);
		 TableData td = new TableData(dt);
		 td.setGridData(gd);
		 td.setPortletID(portletID);
	     return td;
    }

	public ReportData getReportData(String reportID) {
		 Report r = getReport(reportID);
		 String defineJSON = r.getDefineJSON();
		 ReportData rd = new ReportData(r);
		 JSONObject o = (JSONObject) JSON.parse(defineJSON);
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
					 TableData td = getReptTable(objid,portletID);
					 tableData.add(td);
				 } else {
					 ChartData cd =  getReptChart(objid,portletID);
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
}