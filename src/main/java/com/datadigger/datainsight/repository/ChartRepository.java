package com.datadigger.datainsight.repository;

import org.springframework.data.repository.CrudRepository;

import com.datadigger.datainsight.domain.Chart;


public interface ChartRepository extends CrudRepository<Chart, String> {

	
}