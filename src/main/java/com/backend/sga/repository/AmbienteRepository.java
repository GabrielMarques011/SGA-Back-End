package com.backend.sga.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Ambiente;

@Repository
public interface AmbienteRepository extends PagingAndSortingRepository<Ambiente, Long>{

	//select * from sga.ambiente;
	//public List<Ambiente> findAllAmbientes();
	
}
