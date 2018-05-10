package com.datadigger.datainsight.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datadigger.datainsight.domain.Report;
import com.datadigger.datainsight.domain.ReportData;
import com.datadigger.datainsight.service.MetaDataService;

@RestController
public class ReportController {
	 @Autowired
	private MetaDataService metaDataService;
    @RequestMapping("/report/list")
    public Iterable<Report> getAllReport() {     
    	return metaDataService.listAllReport();
    }

    @RequestMapping("/report/save")
    public String saveReport(Report report) {
    	    System.out.println("Report is " + report.getName());
    		String reportId = metaDataService.saveReport(report).getId();
    		return reportId;
    }
    
    @RequestMapping("/report/getone")
    public Report getReport(String reportID) {
    	   
    		return metaDataService.getReport(reportID);
    }
    @RequestMapping("/report/preview")
    public ReportData initReportByJSON(String reportDefine) {
    	   
    	 return metaDataService.initReportByJSON(reportDefine);
    	 
    }
    @RequestMapping("/report/getdata")
    public ReportData initReportById(String reportID) {
    	   
    	 return metaDataService.initReportById(reportID);
    	 
    }
    @RequestMapping("/report/update")
    public ReportData updateReportData(String reportDefine) {
    	   
    	 return metaDataService.updateReportData(reportDefine);
    	 
    }
    @RequestMapping("/report/getStandBy")
    public List<String> getReportStandByValue(String relatedJSON) {
    	   
    	 return metaDataService.getReportStandByValue(relatedJSON);
    	 
    }
}