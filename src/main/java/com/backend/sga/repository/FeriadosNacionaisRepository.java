package com.backend.sga.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.backend.sga.model.FeriadosNacionais;


@Repository
public interface FeriadosNacionaisRepository extends PagingAndSortingRepository<FeriadosNacionais, Long>{

	
	
	
	
}
