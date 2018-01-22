package com.datadigger.datainsight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datadigger.datainsight.domain.DataSource;
import com.datadigger.datainsight.service.MetaDataService;

@RestController
public class DataSourceController {
	 @Autowired
	private MetaDataService metaDataService;
	 
    @RequestMapping("/datasource/list")
    public Iterable<DataSource> getAllDataSources() {     
    	return metaDataService.listAllDataSource();
    }
  
    @RequestMapping("/hello")
    public String index() {
        return "Hello World";
    }
    @RequestMapping("/datasource/new")
    public String createDataSource(DataSource dataSource) {
    	    System.out.println("DataSource is " + dataSource.getName());
    		String dsId = metaDataService.createDataSource(dataSource).getId();
    		return dsId;
    }
}