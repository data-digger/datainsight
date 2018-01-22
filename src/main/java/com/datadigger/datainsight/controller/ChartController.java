package com.datadigger.datainsight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datadigger.datainsight.bean.GridData;
import com.datadigger.datainsight.domain.Chart;
import com.datadigger.datainsight.service.MetaDataService;

@RestController
public class ChartController {
	 @Autowired
	private MetaDataService metaDataService;
    @RequestMapping("/chart/list")
    public Iterable<Chart> getAllChart() {     
    	return metaDataService.listAllChart();
    }

    @RequestMapping("/chart/new")
    public String createChart( Chart chart) {
    	    System.out.println("Chart is " + chart.getName());
    		String chartId = metaDataService.createChart(chart).getId();
    		return chartId;
    }
    @RequestMapping("/chart/preview")
    public GridData perviewChart(String chartId) { 	
    	return metaDataService.previewChart(chartId);

    }
}