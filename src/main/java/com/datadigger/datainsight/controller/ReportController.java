package com.datadigger.datainsight.controller;

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
    @RequestMapping("/report/getdata")
    public ReportData getReportData(String reportID) {
    	   
    	 return metaDataService.getReportData(reportID);
    	 
    }
    @RequestMapping("/report/update")
    public ReportData updateReportData(String reportID,String JSONparam) {
    	   
    	 return metaDataService.updateReportData(reportID, JSONparam);
    	 
    }
}