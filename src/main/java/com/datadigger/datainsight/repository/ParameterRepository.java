package com.datadigger.datainsight.repository;

import org.springframework.data.repository.CrudRepository;

import com.datadigger.datainsight.domain.Parameter;


public interface ParameterRepository extends CrudRepository<Parameter, String> {

	
}