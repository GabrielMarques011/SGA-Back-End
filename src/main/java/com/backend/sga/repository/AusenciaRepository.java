package com.backend.sga.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Ausencia;

@Repository
public interface AusenciaRepository extends PagingAndSortingRepository<Ausencia, Long>{

	public List<Ausencia> findByProfessorId(Long id);
	
	//SELECT  a.data_inicio, a.data_final FROM sga.ausencia AS a WHERE a.professor_id = 1;
	@Query("SELECT a FROM Ausencia a WHERE a.professor.id = :id AND a.tipo = 0")
	public List<Ausencia> listaAusenciaDeProf(@Param("id") Long id);
	
	// SELECT * FROM sga.ausencia as a where "2023-02-16" between a.data_inicio and a.data_final
	@Query("SELECT a FROM Ausencia a WHERE :data BETWEEN a.dataInicio AND a.dataFinal")
	public List<Ausencia> buscaAusenciaData(@Param("data") Calendar data);
	
	@Query("SELECT a FROM Ausencia a WHERE a.dataInicio >= :dataInicio AND a.dataFinal <= :dataFinal AND a.professor.id = :id")
	public List<Ausencia> listaAusenciaDeProf(@Param("dataInicio") Calendar dataInicio, @Param("dataFinal") Calendar dataFinal, @Param("id") Long id );

	//AND a.dataInicio >= :dataInicio AND a.dataFinal <= :dataFinal
	@Query("SELECT a FROM Ausencia a WHERE a.professor.id = :id AND a.tipo = 0")
	public List<Ausencia> listaAusenciaDeUmProfessor(@Param("id") Long id);
	
}
