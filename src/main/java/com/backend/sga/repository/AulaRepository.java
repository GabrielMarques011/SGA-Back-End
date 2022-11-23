package com.backend.sga.repository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.checkerframework.checker.units.qual.A;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Ambiente;
import com.backend.sga.model.Aula;
import com.backend.sga.model.Periodo;
import com.backend.sga.model.Professor;
import com.backend.sga.model.TipoCurso;
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
	
	//VER COM OS MLK DO BACK
	//SELECT * FROM sga.aula where sga.aula.data = "2022-10-26";
	@Query("SELECT a FROM Aula a WHERE a.data = :data AND a.periodo = :periodo and ambiente = :ambiente")
	public List<Aula> diaAula(@Param("data") Calendar data, @Param("periodo") Periodo periodo, @Param("ambiente") Ambiente ambiente);
	
	//SELECT * FROM sga.aula where sga.aula.periodo = 1 
	@Query("SELECT a from Aula a where a.periodo = :periodo and a.data = :data")
	public Optional<Aula> findByPeriodoEData (@Param("periodo") Periodo periodo, @Param("data") Calendar data);
	
	//SELECT * FROM sga.aula where sga.aula.periodo = 1 
	@Query("SELECT a FROM Aula a WHERE a.periodo = :periodo AND a.data >= :dataInicio AND a.data <= :dataFinal ")
	public List<Aula> findByPeriodoEDataIF (@Param("periodo") Periodo periodo, @Param("dataInicio") Calendar dataInicio, @Param("dataFinal") Calendar dataFinal);
	
	@Query("SELECT a FROM Aula a WHERE a.codTurma = :codTurma")
	public List<Aula> buscaCodTurma(@Param("codTurma") String codTurma);
	
	//SELECT * FROM sga.aula AS a WHERE a.cod_turma = "02" AND a.data >= "2022-10-31" AND a.data <= "2022-11-09";
	@Query("SELECT a FROM Aula a WHERE a.partitionKey = :partitionKey AND a.data >= :dataInicio AND a.data <= :dataFinal")
	public List<Aula> buscaDatasEKey (@Param("partitionKey") int partitionKey, @Param("dataInicio") Calendar dataInicio,@Param("dataFinal") Calendar dataFinal);
	
	@Query("SELECT a FROM Aula a WHERE a.professor = :prof AND a.data = :data AND periodo = :periodo")
	public List<Aula> buscaProf (@Param("prof") Professor prof, @Param("data") Calendar data, @Param("periodo") Periodo perido);
	
	@Query("SELECT a FROM Aula a WHERE a.data = :data AND a.periodo = :periodo AND a.ambiente = :ambiente")
	public Optional<Aula> ocupadoPorDataPeriodo(@Param("data") Calendar dataInicio, @Param("periodo") Periodo periodo, @Param("ambiente") Ambiente ambiente);
	
	@Query("SELECT a FROM Aula a WHERE a.data = :data AND a.periodo = :periodo AND a.professor = :professor")
	public Optional<Aula> ocupadoPorDataPeriodoProf(@Param("data") Calendar dataInicio, @Param("periodo") Periodo periodo, @Param("professor") Professor professor);
	
	//SELECT * FROM sga.aula AS a INNER JOIN sga.professor AS p ON a.professor_id = p.id INNER JOIN sga.ambiente AS am ON a.ambiente_id = am.id INNER JOIN sga.unidade_curricular AS uc ON a.unidade_curricular_id = uc.id WHERE a.cod_turma LIKE '%2%'OR a.carga_diaria LIKE '%2%' OR p.nome LIKE "%2%" OR am.nome LIKE "%2%" OR uc.nome LIKE "%2%";
	@Query("SELECT a FROM Aula a INNER JOIN Professor p ON a.professor.id = p.id INNER JOIN Ambiente am ON a.ambiente.id = am.id INNER JOIN UnidadeCurricular uc ON a.unidadeCurricular.id = uc.id WHERE a.codTurma LIKE %:value% OR a.cargaDiaria LIKE %:value% OR p.nome LIKE %:value% OR am.nome LIKE %:value% OR uc.nome LIKE %:value%")
	public List<Aula> filtroAula (@Param("value") String value);
	
	@Query("SELECT a FROM Aula a WHERE a.data = :data")
	public List<Aula> buscaData(@Param("data") Calendar data);
	
	@Query("SELECT a FROM Aula a WHERE a.data = :data AND a.periodo = :periodo AND a.professor = :professor")
	public Optional<Aula> ocupadoProfessor(@Param("data") Calendar dataInicio, @Param("periodo") Periodo periodo, @Param("professor") Professor professor);
	
	//SELECT * FROM sga.professor AS p INNER JOIN sga.aula AS a ON p.id = a.professor_id WHERE a.unidade_curricular_id = 1 AND a.periodo = 0 AND a.data = "2022-12-06";
	@Query("SELECT p FROM Professor p INNER JOIN Aula a ON p.id = a.professor.id WHERE a.unidadeCurricular.id = :id AND a.periodo = :periodo AND a.data = :data")
	public Optional<Professor> disponibilidade(@Param("id") Long id, @Param("periodo") Periodo periodo, @Param("data") Calendar data);
	
	// SELECT a.periodo, count(*) as conta FROM sga.aula as a where a.data >= "2022-10-01" and a.data <= "2022-10-31" group by a.periodo;
	@Query("SELECT a.periodo FROM Aula a WHERE a.data >= :comeco AND a.data <= :fim GROUP BY a.periodo ORDER BY a.periodo ASC")
	public List<Periodo> comparacaoMes(@Param("comeco") Calendar comeco, @Param("fim") Calendar fim);
	
	@Query("SELECT COUNT(*) FROM Aula a WHERE a.data >= :comeco AND a.data <= :fim GROUP BY a.periodo ORDER BY a.periodo ASC")
	public List<Integer> valorMes(@Param("comeco") Calendar comeco, @Param("fim") Calendar fim);

	@Query("SELECT a FROM Aula a WHERE a.periodo = :periodo")
	public List<Aula> listaPorPeriodo(@Param("periodo") Periodo periodo);
	
	//SELECT a FROM Professor p INNER JOIN Aula a ON a.professor.id = p.id WHERE a.professor.id = :idProf AND a.data >= :data
	@Query("SELECT a FROM Aula a WHERE a.professor.id = :idProf AND a.data >= :data")
	public List<Aula> retornaAulasProf (@Param("idProf") Long idProf, @Param("data") Calendar data);
	
	@Query("SELECT a FROM Aula a INNER JOIN Curso c ON a.curso.id = c.id WHERE a.professor.id = :idProf AND c.tipo = :tipo")
	public List<Aula> retornaAulaProfTipo(@Param("idProf") Long id, @Param("tipo") TipoCurso tipo);
	
	@Query("SELECT a.data FROM Aula a INNER JOIN Curso c ON a.curso.id = c.id WHERE a.professor.id = :idProf AND c.tipo = :tipo")
	public List<Calendar> retornaAulaProfTipoData(@Param("idProf") Long id, @Param("tipo") TipoCurso tipo);
	
	// SELECT * FROM sga.aula AS a WHERE a.professor_id = 1 and a.data >= "2022-11-23" and a.data <= "2022-12-08" ;
	@Query("SELECT a FROM Aula a WHERE a.professor.id = :id AND a.data >= :dataInicio AND a.data <= :dataFinal")
	public List<Aula> buscaTempo(@Param("id") Long id, @Param("dataInicio")Calendar datainicio, @Param("dataFinal") Calendar datafinal);
	
	@Query("SELECT a FROM Aula a WHERE a.data >= :comeco AND a.data <= :final")
	public List<Aula> buscaEntreDatas(@Param("comeco") Calendar inicio, @Param("final") Calendar fim);
	
	@Query("SELECT a FROM Aula a WHERE a.partitionKey = :partitionKey")
	public List<Aula> findByPartitionKey(@Param("partitionKey") int partitionKey);
	
	

}
