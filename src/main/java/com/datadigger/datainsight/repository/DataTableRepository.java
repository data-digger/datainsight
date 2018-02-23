package com.datadigger.datainsight.repository;

import org.springframework.data.repository.CrudRepository;

import com.datadigger.datainsight.domain.DataTable;


public interface DataTableRepository extends CrudRepository<DataTable, String> {

	
}