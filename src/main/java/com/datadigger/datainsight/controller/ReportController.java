package com.datadigger.datainsight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datadigger.datainsight.domain.Report;
import com.datadigger.datainsight.service.MetaDataService;

@RestController
public class ReportController {
	 @Autowired
	private MetaDataService metaDataService;
    @RequestMapping("/report/list")
    public Iterable<Report> getAllReport() {     
    	return metaDataService.listAllReport();
    }

    @RequestMapping("/report/new")
    public String createReport(Report report) {
    	    System.out.println("Report is " + report.getName());
    		String reportId = metaDataService.createReport(report).getId();
    		return reportId;
    }
    
    @RequestMapping("/report/getone")
    public Report getReport(String reportID) {
    	   
    		return metaDataService.getReport(reportID);
    }
}