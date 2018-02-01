package com.datadigger.datainsight.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datadigger.datainsight.bean.GridData;
import com.datadigger.datainsight.domain.BizView;
import com.datadigger.datainsight.domain.Chart;
import com.datadigger.datainsight.domain.DataSource;
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
		dataSource.setId("DS." + dataSource.getName());
		dastaSourceRespository.save(dataSource);
        log.debug("Create DataSource -- "+ dataSource.getName());
		return dataSource;
	}
	
	public BizView createBizView(BizView bizView) {
		bizView.setId("BZ." + bizView.getName());
		bizViewRespository.save(bizView);
		log.debug("Create BizView -- "+ bizView.getName());
		return bizView;
	}

    public GridData previewBizView(String bizViewId){
    	BizView bizView = bizViewRespository.findOne(bizViewId);
    	String dataSourceId = bizView.getDataSourceId();
    	DataSource ds = dastaSourceRespository.findOne(dataSourceId);
    	return SQLExecutor.execute(ds, bizView.getDefineJSON());
    }
    
    public Chart createChart(Chart chart) {
		chart.setId("CR." + chart.getName());
		chartRespository.save(chart);
		log.debug("Create Chart -- "+ chart.getName());
		return chart;
	}
    
    public GridData previewChart(String chartId){
    	Chart chart = chartRespository.findOne(chartId);
    	String bizViewId = chart.getBizViewId();
    	BizView bizView = bizViewRespository.findOne(bizViewId);
    	String dataSourceId = bizView.getDataSourceId();
    	DataSource ds = dastaSourceRespository.findOne(dataSourceId);
    	return SQLExecutor.execute(ds, bizView.getDefineJSON());
    }
    
    public Report createReport(Report report) {
    	report.setId("RP." + report.getName());
    	reportRespository.save(report);
		log.debug("Create report -- "+ report.getName());
		return report;
	}
}