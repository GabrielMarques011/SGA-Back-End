package com.backend.sga.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.UnidadeCurricular;

@Repository
public interface UnidadeCurricularRepository extends PagingAndSortingRepository<UnidadeCurricular, Long>{

}
