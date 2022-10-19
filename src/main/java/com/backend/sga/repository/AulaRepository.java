package com.backend.sga.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Ambiente;
import com.backend.sga.model.Aula;
import com.backend.sga.model.Periodo;

@Repository
public interface AulaRepository extends PagingAndSortingRepository<Aula, Long>{

	//@Query("SELECT a FROM Aula a WHERE a.professor.id = :id AND a.dataInicio >= :data_inicio AND a.dataFinal <= :data_final")
	//public List<Aula> buscaTempo (@Param("id") Long id, @Param("data_inicio") Calendar data_inicio, @Param("data_final") Calendar data_final);
	
	@Query("SELECT a FROM Aula a WHERE a.unidadeCurricular.id = :id")
	public List<Aula> findByAmbientesId(@Param("id") Long id);
	
	//SELECT * FROM sga.aula where sga.aula.data >= "2022-09-25"
	@Query("SELECT a FROM Aula a WHERE a.data >= :data")
	public List<Aula> diaSemanal(@Param("data") Calendar data);
	
	//SELECT * FROM sga.aula where sga.aula.data = "2022-10-26";
	@Query("SELECT a FROM Aula a WHERE a.data = :data AND a.periodo = :periodo and ambiente = :ambiente")
	public List<Aula> diaAula(@Param("data") Calendar data, @Param("periodo") Periodo periodo, @Param("ambiente") Ambiente ambiente);
	
	//SELECT * FROM sga.aula where sga.aula.periodo = 1 
	@Query("SELECT a from Aula a where a.periodo = :periodo and a.data = :data")
	public Iterable<Aula> retornaPeriodo (@Param("periodo") Periodo periodo, @Param("data") Calendar data);
	
	@Query("SELECT a FROM Aula a WHERE a.codTurma = :codTurma")
	public List<Aula> buscaCodTurma(@Param("codTurma") String codTurma);
}
