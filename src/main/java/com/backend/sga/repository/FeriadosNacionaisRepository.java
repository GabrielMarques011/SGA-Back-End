package com.backend.sga.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.backend.sga.model.FeriadosNacionais;


@Repository
public interface FeriadosNacionaisRepository extends PagingAndSortingRepository<FeriadosNacionais, Long>{

	//SELECT * FROM sga.feriados_nacionais where date = "2022-01-01";
	@Query("SELECT f FROM FeriadosNacionais f WHERE f.date = :date")
	public List<FeriadosNacionais> buscaData (@Param("date") String date);
	
	@Query("SELECT f FROM FeriadosNacionais f WHERE f.date >= :inicio AND f.date <= :final")
	public List<FeriadosNacionais> buscaAno(@Param("inicio") String inicio, @Param("final") String fim);
	
	
}
