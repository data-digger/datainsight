package com.datadigger.datainsight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datadigger.datainsight.bean.GridData;
import com.datadigger.datainsight.bean.ParamGridData;
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
    
    @RequestMapping("/chart/getone")
    public Chart getChart( String chartID) {
    	    System.out.println("Chart is " + chartID);
    		return metaDataService.getChart(chartID);
    }
    
    @RequestMapping("/chart/preview")
    public ParamGridData perviewChart(String chartId,String JSONParam) { 	
    	return metaDataService.getChartData(chartId,JSONParam);

    }
    
    @RequestMapping("/chart/getdata")
    public ParamGridData getChartData(String chartId,String JSONParam) { 	
    	return metaDataService.getChartData(chartId,JSONParam);

    }
}