package com.datadigger.datainsight.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datadigger.datainsight.bean.GridData;
import com.datadigger.datainsight.domain.BizViewColumn;
import com.datadigger.datainsight.service.MetaDataService;

@RestController
public class BizViewColumnController {
	 @Autowired
	private MetaDataService metaDataService;
    @RequestMapping("/bizview/column/list")
    public List<BizViewColumn> getBizViewColumns(String bizviewId) {     
    		return metaDataService.getBizViewColumns(bizviewId);
    }

    @RequestMapping("/bizview/column/save")
    public void saveBizViewColumns(List<BizViewColumn> bizViewColumns) {
    		metaDataService.saveBizViewColumns(bizViewColumns);
    }
}