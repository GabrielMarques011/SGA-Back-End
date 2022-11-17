package com.backend.sga.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.DiaNaoLetivo;

@Repository
public interface DiaNaoLetivoRepository extends PagingAndSortingRepository<DiaNaoLetivo, Long>{

	// busca o dia não letivo pela data
	@Query("SELECT dnl from DiaNaoLetivo dnl where dnl.data = :data ")
	public List<DiaNaoLetivo> buscaDNL(@Param("data") Calendar dataInicio );
	
	@Query("SELECT dnl FROM DiaNaoLetivo dnl WHERE dnl.data >= :dataInicio AND dnl.data <= :dataFinal")
	public List<DiaNaoLetivo> buscaAno(@Param("dataInicio") Calendar datainicio, @Param("dataFinal") Calendar datafinal);
	
}
	