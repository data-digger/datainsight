package com.datadigger.datainsight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datadigger.datainsight.bean.GridData;
import com.datadigger.datainsight.bean.ParamGridData;
import com.datadigger.datainsight.domain.BizView;
import com.datadigger.datainsight.service.MetaDataService;

@RestController
public class BizViewController {
	 @Autowired
	private MetaDataService metaDataService;
    @RequestMapping("/bizview/list")
    public Iterable<BizView> getAllBizView() {     
    	return metaDataService.listAllBizView();
    }

    @RequestMapping("/bizview/new")
    public String createBizView(BizView bizView) {
    	    System.out.println("BizView is " + bizView.getName());
    		String bizViewId = metaDataService.createBizView(bizView).getId();
    		return bizViewId;
    }
    @RequestMapping("/bizview/preview")
//    public GridData perviewBizView(String bizViewId) { 	
//    	return metaDataService.getGridData(bizViewId);
//
//    }
    public ParamGridData perviewBizView(String bizViewId) { 	
    		return metaDataService.getParamGridData(bizViewId);

   }
}