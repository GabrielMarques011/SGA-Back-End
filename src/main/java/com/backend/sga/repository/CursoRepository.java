package com.backend.sga.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Curso;
import com.backend.sga.model.TipoCurso;

@Repository
public interface CursoRepository extends PagingAndSortingRepository<Curso, Long>{

	@Query("SELECT c FROM Curso c order by c.nome")
	public List<Curso> findAllOrdeyBy();
	
	//SELECT * FROM sga.curso WHERE sga.curso.tipo_curso = 0
	@Query("SELECT c FROM Curso c WHERE c.tipo = :tipo")
	public Iterable<Curso> buscaTipoCurso (@Param("tipo") TipoCurso tipo);

	//SELECT * FROM sga.curso WHERE sga.curso.nome LIKE '%Ele%'
	@Query("SELECT c FROM Curso c WHERE c.nome LIKE %:nome%")
	public List<Curso> palavraChave(@Param("nome") String nome);
	
	//SELECT * FROM sga.curso INNER JOIN sga.unidade_curricular ON c.id = uc.id WHERE uc.nome = 'ai-900 Curso';
	@Query("SELECT c FROM Curso c INNER JOIN UnidadeCurricular uc ON c.id = uc.id WHERE uc.nome LIKE %:nome%")
	public List<Curso> buscaCurso (@Param("nome") String nome);
	
	
}
