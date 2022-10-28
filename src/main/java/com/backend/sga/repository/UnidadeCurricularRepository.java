package com.backend.sga.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Curso;
import com.backend.sga.model.UnidadeCurricular;

@Repository
public interface UnidadeCurricularRepository extends PagingAndSortingRepository<UnidadeCurricular, Long>{

	//@Query("SELECT un FROM UnidadeCurricular un INNER JOIN Curso c ON un.id = c.id WHERE un.nome = :nome")
	//public Iterable<Curso> buscaCurso (@Param("nome") String nome);
	
}
