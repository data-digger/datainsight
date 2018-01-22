package com.datadigger.datainsight.repository;

import org.springframework.data.repository.CrudRepository;

import com.datadigger.datainsight.domain.DataSource;

public interface DataSourceRepository extends CrudRepository<DataSource, String> {

	
}