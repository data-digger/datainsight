package com.datadigger.datainsight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datadigger.datainsight.bean.GridData;
import com.datadigger.datainsight.domain.Parameter;
import com.datadigger.datainsight.service.MetaDataService;

@RestController
public class ParameterController {
	 @Autowired
	private MetaDataService metaDataService;
    @RequestMapping("/parameter/list")
    public Iterable<Parameter> getAllParameter() {     
    	return metaDataService.listAllParameter();
    }

    @RequestMapping("/parameter/new")
    public String createParameter(Parameter parameter) {
    	    System.out.println("Parameter is " + parameter.getName());
    		String parameterId = metaDataService.createParameter(parameter).getId();
    		return parameterId;
    }
}