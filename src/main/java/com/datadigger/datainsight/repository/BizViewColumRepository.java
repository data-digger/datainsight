package com.datadigger.datainsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.datadigger.datainsight.domain.BizViewColumn;;


public interface BizViewColumRepository extends CrudRepository<BizViewColumn, String> {
	
	List<BizViewColumn> findByBizViewId(String bizViewId);
	
	BizViewColumn findByBizViewIdAndColumnName(String bizViewId,String columnName);
	
	List<BizViewColumn> findByBizViewIdAndCategory(String bizViewId,int category);
	
	@Query(value="select * from bizview_columns where bizview_id=?1 and category in (0,1)",nativeQuery=true)  
	List<BizViewColumn> findColumnsNotAggregation(String bizViewId);
	
}