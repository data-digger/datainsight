package com.datadigger.datainsight.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.datadigger.datainsight.domain.BizViewColumn;;


public interface BizViewColumRepository extends CrudRepository<BizViewColumn, String> {
	List<BizViewColumn> findByBizViewId(String bizViewId);
	
}