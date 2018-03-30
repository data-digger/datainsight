package com.datadigger.datainsight.controller;

import java.util.Map;

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

    @RequestMapping("/bizview/save")
    public String saveBizView(BizView bizView) {
    	    System.out.println("BizView is " + bizView.getName());
    		String bizViewId = metaDataService.saveBizView(bizView).getId();
    		return bizViewId;
    }
    @RequestMapping("/bizview/preview")
//    public GridData perviewBizView(String bizViewId) { 	
//    	return metaDataService.getGridData(bizViewId);
//
//    }
    public GridData perviewBizView(String dateSourceId,String sqlStament,String pageSize) { 
    		return metaDataService.getBizViewData(dateSourceId,sqlStament,pageSize);
   }
    @RequestMapping("/bizview/update")
    public ParamGridData updateBizView(String bizViewId,String JSONParam) {
    		return metaDataService.updateBizViewData(bizViewId,JSONParam);
    }
}