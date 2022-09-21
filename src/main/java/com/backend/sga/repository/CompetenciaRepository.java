package com.backend.sga.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Competencia;

@Repository
public interface CompetenciaRepository extends PagingAndSortingRepository<Competencia, Long>{
	
	@Query("SELECT c FROM Competencia c where c.nivel = :nivel")
	public List<Competencia> buscarNivel (@Param("nivel") int nivel);
}
