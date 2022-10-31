package com.backend.sga.repository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Ambiente;
import com.backend.sga.model.Aula;
import com.backend.sga.model.Periodo;
import com.backend.sga.model.Professor;
import com.backend.sga.model.UnidadeCurricular;

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
	public Optional<Aula> retornaPeriodo (@Param("periodo") Periodo periodo, @Param("data") Calendar data);
	
	@Query("SELECT a FROM Aula a WHERE a.codTurma = :codTurma")
	public List<Aula> buscaCodTurma(@Param("codTurma") String codTurma);
	
	//SELECT * FROM sga.aula AS a WHERE a.cod_turma = "02" AND a.data >= "2022-10-31" AND a.data <= "2022-11-09";
	@Query("SELECT a FROM Aula a WHERE a.codTurma = :cod_turma AND a.data >= :dataInicio AND a.data <= :dataFinal")
	public List<Aula> buscaDatasECod (@Param("cod_turma") String cod_turma, @Param("dataInicio") Calendar dataInicio,@Param("dataFinal") Calendar dataFinal);
	
	@Query("SELECT a FROM Aula a WHERE a.professor = :prof AND a.data = :data AND periodo = :periodo")
	public List<Aula> buscaProf (@Param("prof") Professor prof, @Param("data") Calendar data, @Param("periodo") Periodo perido);
	
	@Query("SELECT a FROM Aula a WHERE a.data = :data AND a.periodo = :periodo AND a.ambiente = :ambiente")
	public Optional<Aula> ocupadoPorDataPeriodo(@Param("data") Calendar dataInicio, @Param("periodo") Periodo periodo, @Param("ambiente") Ambiente ambiente);
	
	//SELECT * FROM sga.aula AS a INNER JOIN sga.professor AS p ON a.professor_id = p.id INNER JOIN sga.ambiente AS am ON a.ambiente_id = am.id INNER JOIN sga.unidade_curricular AS uc ON a.unidade_curricular_id = uc.id WHERE a.cod_turma LIKE '%2%'OR a.carga_diaria LIKE '%2%' OR p.nome LIKE "%2%" OR am.nome LIKE "%2%" OR uc.nome LIKE "%2%";
	@Query("SELECT a FROM Aula a INNER JOIN Professor p ON a.professor.id = p.id INNER JOIN Ambiente am ON a.ambiente.id = am.id INNER JOIN UnidadeCurricular uc ON a.unidadeCurricular.id = uc.id WHERE a.codTurma LIKE %:value% OR a.cargaDiaria LIKE %:value% OR p.nome LIKE %:value% OR am.nome LIKE %:value% OR uc.nome LIKE %:value%")
	public List<Aula> filtroAula (@Param("value") String value);
	
	@Query("SELECT a FROM Aula a WHERE a.data = :data")
	public List<Aula> buscaData(@Param("data") Calendar data);
	
	@Query("SELECT a FROM Aula a WHERE a.data = :data AND a.periodo = :periodo AND a.professor = :professor")
	public Optional<Aula> ocupadoProfessor(@Param("data") Calendar dataInicio, @Param("periodo") Periodo periodo, @Param("professor") Professor professor);
	
	@Query("SELECT p FROM Professor p INNER JOIN Competencia c ON c.professor.id = p.id INNER JOIN Aula a ON a.professor.id = p.id where c.unidadeCurricular = :unidade AND a.periodo = :periodo AND a.data = :data")
	public Optional<Professor> disponibilidade(@Param("unidade") UnidadeCurricular unidade, @Param("periodo") Periodo periodo, @Param("data") Calendar data);
}
