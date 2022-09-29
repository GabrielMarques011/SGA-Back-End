package com.backend.sga.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Curso;
import com.backend.sga.model.TipoCurso;

@Repository
public interface CursoRepository extends PagingAndSortingRepository<Curso, Long>{

	//SELECT * FROM sga.curso WHERE sga.curso.tipo_curso = 0
	@Query("SELECT c FROM Curso c WHERE c.tipoCurso = :tipo_curso")
	public Iterable<Curso> buscaTipoCurso (@Param("tipo_curso") TipoCurso tipoCurso);

	
}
