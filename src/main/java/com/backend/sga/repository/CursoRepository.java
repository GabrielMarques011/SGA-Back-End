package com.backend.sga.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Curso;

@Repository
public interface CursoRepository extends PagingAndSortingRepository<Curso, Long>{

}
