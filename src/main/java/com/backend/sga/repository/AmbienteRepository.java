package com.backend.sga.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Ambiente;
import com.backend.sga.model.Periodo;
import com.backend.sga.model.TipoAmbiente;

@Repository
public interface AmbienteRepository extends PagingAndSortingRepository<Ambiente, Long> {

	@Query("SELECT a FROM Ambiente a WHERE a.ativo = 1")
	public List<Ambiente> findAllAtivo();
	
	//SELECT * FROM sga.ambiente order by sga.ambiente.nome;
	@Query("SELECT a FROM Ambiente a order by a.nome")
	public List<Ambiente> findAllOrderBy();
	
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
	public Iterable<Ambiente> retornaTipoCapacidade(@Param("tipo") TipoAmbiente tipo,@Param("capacidadeMin") int capacidadeMin, @Param("capacidadeMax") int capacidadeMax);

	@Query("SELECT a FROM Ambiente a INNER JOIN Aula au ON a.id = au.ambiente.id WHERE au.data >= :datainicio AND au.data <= :dataFinal AND au.periodo = :periodo")
	public List<Ambiente> retornaOcupadoEmIntervaloDeDatas(@Param("datainicio") Calendar dataInicio, @Param("dataFinal") Calendar dataFinal, @Param("periodo") Periodo periodo);

	@Query("SELECT a FROM Ambiente a INNER JOIN Aula au ON a.id = au.ambiente.id WHERE au.data = :data GROUP BY a.id")
	public List<Ambiente> ocupadosPorData(@Param("data") Calendar dataInicio);
	
	@Query("SELECT a FROM Ambiente a INNER JOIN Aula au ON a.id = au.ambiente.id WHERE au.data >= :dataInicio AND au.data <= :dataFinal GROUP BY a.id")
	public List<Ambiente> ocupadosPorDatas(@Param("dataInicio") Calendar dataInicio, @Param("dataFinal") Calendar dataFinal);
	
	@Query("SELECT a FROM Ambiente a INNER JOIN Aula au ON a.id = au.ambiente.id WHERE au.data = :data AND au.periodo = :periodo")
	public List<Ambiente> retornaOcupadosPorDia(@Param("data") Calendar data, @Param("periodo") Periodo periodo);
	
	//SELECT * FROM sga.ambiente order by sga.ambiente.nome asc;
	@Query("SELECT a FROM Ambiente a ORDER BY a.nome ASC")
	public List<Ambiente> orderAmbiente();
	
	
	
	public List<Ambiente> findAllByOrderById();

}
