package com.backend.sga.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Ambiente;
import com.backend.sga.model.TipoAmbiente;

@Repository
public interface AmbienteRepository extends PagingAndSortingRepository<Ambiente, Long>{

	//select * from sga.ambiente;
	//public List<Ambiente> findAllAmbientes();
	
	//SELECT * FROM sga.ambiente WHERE sga.ambiente.nome LIKE '%15%'
	@Query("SELECT a FROM Ambiente a WHERE a.nome LIKE %:nome%")
	public List<Ambiente> palavraChave(@Param("nome") String nome);
	
	//SELECT * FROM sga.ambiente WHERE sga.ambiente.tipo_ambiente = 1
	@Query("SELECT a FROM Ambiente a WHERE a.tipoAmbiente = :tipo_ambiente")
	public Iterable<Ambiente> buscaAmbiente (@Param("tipo_ambiente") TipoAmbiente tipoAmbiente);
	
	//SELECT * FROM sga.ambiente WHERE sga.ambiente.capacidade between 15 AND 30;
	@Query("SELECT a FROM Ambiente a WHERE a.capacidade BETWEEN :capacidadeMin AND :capacidadeMax")
	public Iterable<Ambiente> retornaCapacidade (@Param("capacidadeMin") int capacidadeMin, @Param("capacidadeMax") int capacidadeMax);
	
	//SELECT * FROM sga.ambiente WHERE sga.ambiente.tipo_ambiente = 2 AND sga.ambiente.capacidade BETWEEN 20 AND 30
	@Query("SELECT a FROM Ambiente a WHERE a.tipoAmbiente = :tipo_ambiente AND a.capacidade BETWEEN :capacidadeMin AND :capacidadeMax")
	public Iterable<Ambiente> retornaTipoCapacidade (@Param("tipo_ambiente") TipoAmbiente tipoAmbiente, @Param("capacidadeMin") int capacidadeMin, @Param("capacidadeMax") int capacidadeMax);
	
}
