package com.backend.sga.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Ambiente;

@Repository
public interface AmbienteRepository extends PagingAndSortingRepository<Ambiente, Long>{

}
