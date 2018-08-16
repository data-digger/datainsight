package com.datadigger.datainsight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datadigger.datainsight.bean.GridData;
import com.datadigger.datainsight.bean.ParamGridData;
import com.datadigger.datainsight.domain.DataTable;
import com.datadigger.datainsight.service.MetaDataService;

@RestController
public class DataTableController {
	 @Autowired
	private MetaDataService metaDataService;
    @RequestMapping("/table/list")
    public Iterable<DataTable> getAllDataTable() {     
    	return metaDataService.listAllDataTable();
    }

    @RequestMapping("/table/save")
    public String saveDataTable(DataTable dataTable) {
    	    System.out.println("Table is " + dataTable.getName());
    		String tableId = metaDataService.saveDataTable(dataTable).getId();
    		return tableId;
    }
}