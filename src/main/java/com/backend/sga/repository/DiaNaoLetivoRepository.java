package com.backend.sga.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.backend.sga.model.DiaNaoLetivo;

public interface DiaNaoLetivoRepository extends PagingAndSortingRepository<DiaNaoLetivo, Long>{

	//SELECT * FROM sga.dia_nao_letivo where data_inicio >= "2022-12-01" AND data_final <= "2023-01-05"
	@Query("SELECT dnl FROM DiaNaoLetivo dnl WHERE dnl.data_inicio >= :data_inicio")
	public List<DiaNaoLetivo> buscaDataNaoLetiva (@Param("data_inicio") Calendar data_inicio, @Param("data_final") Calendar data_final);
	
}
