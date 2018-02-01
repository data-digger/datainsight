package com.datadigger.datainsight.repository;

import org.springframework.data.repository.CrudRepository;

import com.datadigger.datainsight.domain.Report;


public interface ReportRepository extends CrudRepository<Report, String> {

	
}