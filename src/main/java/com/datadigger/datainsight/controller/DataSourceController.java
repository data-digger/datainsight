package com.datadigger.datainsight.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datadigger.datainsight.bean.TreeNode;
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
    @RequestMapping("/datasource/save")
    public String saveDataSource(DataSource dataSource) {
    	    System.out.println("DataSource is " + dataSource.getName());
    		String dsId = metaDataService.saveDataSource(dataSource).getId();
    		return dsId;
    }
    
    @RequestMapping("/datasource/gettables")
    public List<TreeNode> getTables(String dsId) {
    		return metaDataService.getTables(dsId);
    }
}