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
import com.datadigger.datainsight.query.SQLExecutor;
import com.datadigger.datainsight.repository.BizViewRepository;
import com.datadigger.datainsight.repository.ChartRepository;
import com.datadigger.datainsight.repository.DataSourceRepository;
import com.datadigger.datainsight.repository.ReportRepository;
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
    
    public Report createReport(Report report) {
    	report.setId(DomainType.RP.getDomainIDPrefix() + report.getName());
    	reportRespository.save(report);
		log.debug("Create report -- "+ report.getName());
		return report;
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
    
    	
    
	public ReportData getReportData(String reportID) {
		 Report r = getReport(reportID);
		 String defineJSON = r.getDefineJSON();
		 ReportData rd = new ReportData(r);
		 JSONObject o = (JSONObject) JSON.parse(defineJSON);
		 JSONObject content = o.getJSONObject("content");
		 JSONArray portlets = content.getJSONArray("portlets");
		 List<ChartData> data = new ArrayList<ChartData>();
		 for (int i = 0; i < portlets.size(); i++) {
			 JSONObject portlet = portlets.getJSONObject(i);
			 String portletID = portlet.getString("portletID");
			 JSONArray tabs = portlet.getJSONArray("tabs");
			 for(int j = 0; j < tabs.size(); j++) {
				 JSONObject jchart = tabs.getJSONObject(j);
				 String chartId = jchart.getString("objid");
				 Chart chart = getChart(chartId);
				 GridData gd  =getChartData(chartId);
				 ChartData cd =  new ChartData(chart);
				 cd.setPortletID(portletID);
				 cd.setGridData(gd);
				 data.add(cd);
			 }
			 
		 }
		 rd.setData(data);
		return rd;
	}

	public Chart getChart(String chartID) {
		Chart chart = chartRespository.findOne(chartID);
		
		return chart;
	}
}