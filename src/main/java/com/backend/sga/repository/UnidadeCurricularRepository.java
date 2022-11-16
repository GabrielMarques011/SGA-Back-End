package com.backend.sga.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.UnidadeCurricular;

@Repository
public interface UnidadeCurricularRepository extends PagingAndSortingRepository<UnidadeCurricular, Long>{

	//SELECT * FROM sga.unidade_curricular AS uc WHERE uc.nome LIKE "%DP%";
	@Query("SELECT uc FROM UnidadeCurricular uc WHERE uc.nome LIKE %:nome%")
	public List<UnidadeCurricular> autoComplete (@Param("nome") String nome);
	
	//SELECT uc.* FROM sga.unidade_curricular AS uc INNER JOIN sga.curso AS c ON c.id = uc.curso_id WHERE c.nome LIKE "%dp%";
	//@Query("SELECT uc FROM UnidadeCurricular uc INNER JOIN Curso c ON c.id = uc.curso_id WHERE c.nome LIKE %:nome%")
	//public List<UnidadeCurricular> buscaUnidade(@Param("nome") String nome);
	
	@Query("SELECT uc FROM UnidadeCurricular uc INNER JOIN Curso c ON c.id = uc.id WHERE c.nome LIKE %:nome%")
	public List<UnidadeCurricular> buscaUnidade (@Param("nome") String nome);
	
}
