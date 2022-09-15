package com.backend.sga.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Professor;

@Repository
public interface ProfessorRepository extends PagingAndSortingRepository<Professor, Long>{

}
