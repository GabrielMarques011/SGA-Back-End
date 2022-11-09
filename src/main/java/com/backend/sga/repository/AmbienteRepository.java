package com.backend.sga.repository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Ambiente;
import com.backend.sga.model.Periodo;
import com.backend.sga.model.TipoAmbiente;

@Repository
public interface AmbienteRepository extends PagingAndSortingRepository<Ambiente, Long> {

	// select * from sga.ambiente;
	// public List<Ambiente> findAllAmbientes();

	// SELECT * FROM sga.ambiente WHERE sga.ambiente.nome LIKE '%15%'
	@Query("SELECT a FROM Ambiente a WHERE a.nome LIKE %:nome%")
	public List<Ambiente> palavraChave(@Param("nome") String nome);

	// SELECT * FROM sga.ambiente WHERE sga.ambiente.tipo_ambiente = 1
	@Query("SELECT a FROM Ambiente a WHERE a.tipo = :tipo")
	public Iterable<Ambiente> buscaAmbiente(@Param("tipo") TipoAmbiente tipoAmbiente);

	// SELECT * FROM sga.ambiente WHERE sga.ambiente.capacidade between 15 AND 30;
	@Query("SELECT a FROM Ambiente a WHERE a.capacidade BETWEEN :capacidadeMin AND :capacidadeMax")
	public Iterable<Ambiente> retornaCapacidade(@Param("capacidadeMin") int capacidadeMin,@Param("capacidadeMax") int capacidadeMax);

	// SELECT * FROM sga.ambiente WHERE sga.ambiente.tipo_ambiente = 2 AND sga.ambiente.capacidade BETWEEN 20 AND 30
	@Query("SELECT a FROM Ambiente a WHERE a.tipo = :tipo AND a.capacidade BETWEEN :capacidadeMin AND :capacidadeMax")
	public Iterable<Ambiente> retornaTipoCapacidade(@Param("tipo") TipoAmbiente tipoAmbiente,@Param("capacidadeMin") int capacidadeMin, @Param("capacidadeMax") int capacidadeMax);

	// SELECT a.* FROM sga.ambiente as a inner join sga.aula as au on a.id = au.ambiente_id where au.data >= "2022-10-24" AND au.data <= "2022-11-08" and au.periodo = 0 group by ID ;

	@Query("SELECT a FROM Ambiente a INNER JOIN Aula au ON a.id = au.ambiente.id WHERE au.data >= :datainicio AND au.data <= :dataFinal AND au.periodo = :periodo")
	public List<Ambiente> retornaOcupados(@Param("datainicio") Calendar dataInicio, @Param("dataFinal") Calendar dataFinal, @Param("periodo") Periodo periodo);

	@Query("SELECT a FROM Ambiente a INNER JOIN Aula au ON a.id = au.ambiente.id WHERE au.data = :datainicio AND au.periodo = :periodo")
	public List<Ambiente> retornaOcupadosDia(@Param("datainicio") String dataInicio, @Param("periodo") Periodo periodo);
	
	@Query("SELECT a FROM Ambiente a INNER JOIN Aula au ON a.id = au.ambiente.id WHERE au.data = :data GROUP BY a.id")
	public List<Ambiente> ocupadosPorData(@Param("data") Calendar dataInicio);
	
	//SELECT * FROM sga.ambiente order by sga.ambiente.nome asc;
	@Query("SELECT a FROM Ambiente a ORDER BY a.nome ASC")
	public List<Ambiente> orderAmbiente();
	
	@Query("SELECT a FROM Ambiente a INNER JOIN Aula au ON a.id = au.ambiente.id WHERE au.data = :datainicio AND au.periodo = :periodo")
	public List<Ambiente> retornaOcupadosDiaCalendar(@Param("datainicio") Calendar dataInicio, @Param("periodo") Periodo periodo);

}
