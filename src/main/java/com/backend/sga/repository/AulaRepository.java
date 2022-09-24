package com.backend.sga.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Aula;

@Repository
public interface AulaRepository extends PagingAndSortingRepository<Aula, Long>{

	@Query("SELECT a FROM Aula a WHERE a.professor.id = :id AND a.dataInicio >= :data_inicio AND a.dataFinal <= :data_final")
	public List<Aula> buscaTempo (@Param("id") Long id, @Param("data_inicio") Calendar data_inicio, @Param("data_final") Calendar data_final);
	
	@Query("SELECT a FROM Aula a WHERE a.unidadeCurricular.id = :id")
	public List<Aula> findByAmbientesId(@Param("id") Long id);
	
}
