package com.datadigger.datainsight.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.datadigger.datainsight.bean.GridData;
import com.datadigger.datainsight.domain.BizViewColumn;
import com.datadigger.datainsight.service.MetaDataService;

@RestController
public class BizViewColumnController {
	 @Autowired
	private MetaDataService metaDataService;
	 /*
	  * 获取bizView的所有字段（原始字段+计算字段+聚合函数）
	  */
    @RequestMapping("/bizview/column/list")
    public List<BizViewColumn> getBizViewColumns(String bizviewId) {     
    		return metaDataService.getBizViewColumns(bizviewId);
    }

    @RequestMapping("/bizview/column/save")
    public void saveBizViewColumns(String bizViewId,String columsJSON) {
    		metaDataService.saveBizViewColumns(bizViewId,columsJSON);
    }
    
    @RequestMapping("/bizview/column/calculatedfield/preview")
    public GridData getCalculatedFieldData(String expression,String bizViewSql,String dataSourceId ) {     
    		return metaDataService.getCalculatedFieldType(expression,bizViewSql,dataSourceId);
    }
    
    @RequestMapping("/bizview/column/delete")
    public void deleteBizViewColumn(BizViewColumn bizViewColumn) {     
    		 metaDataService.deleteBizViewColumn(bizViewColumn);
    }
}