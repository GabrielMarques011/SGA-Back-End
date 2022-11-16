package com.backend.sga.repository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Periodo;
import com.backend.sga.model.Professor;

@Repository
public interface ProfessorRepository extends PagingAndSortingRepository<Professor, Long>{

	//SELECT * FROM sga.professor WHERE sga.professor.nome LIKE '%r%'
	@Query("SELECT p FROM Professor p WHERE p.nome LIKE %:nome%")
	//Retorna uma lista onde aparece o relacionamento entre as 'letras' escritas com o banco
	public List<Professor> palavraChave(@Param("nome") String nome);
	
	//SELECT * FROM sga.professor order by sga.professor.nome asc;
	@Query("SELECT p FROM Professor p ORDER BY p.nome ASC")
	public List<Professor> orderProf();

	//SELECT p FROM Professor p INNER JOIN Competencia com ON com.professor.id = p.id INNER JOIN UnidadeCurricular uc ON com.unidade_curricular.id = uc.id INNER JOIN Curso c ON uc.curso.id = c.id WHERE c.nome = :nomeCr AND uc.nome = :nomeUc 
	@Query("SELECT p FROM Professor p INNER JOIN Competencia c ON c.professor.id = p.id INNER JOIN UnidadeCurricular uc ON c.unidadeCurricular.id = uc.id INNER JOIN Curso cu ON uc.curso.id = cu.id WHERE cu.nome = :nomeCurso AND uc.nome = :nomeUnidade")
	public List<Professor> listProfcuc(@Param("nomeCurso") String nomeCurso, @Param("nomeUnidade") String nomeUnidade);
	
	// SELECT a.professor_id FROM sga.aula as a where a.data = "2022-10-17" and a.periodo = 0;
	@Query("SELECT a.professor FROM Aula a WHERE a.data = :data AND a.periodo = :periodo")
	public List<Professor> buscaOcupado(@Param("data") Calendar data, @Param("periodo") Periodo periodo);
	
	//SELECT * FROM sga.professor AS p WHERE p.ativo = 1;
	@Query("SELECT p FROM Professor p WHERE p.ativo = :ativo")
	public List<Professor> buscaProfAtivo(@Param("ativo") boolean ativo);
	
	//SELECT * FROM sga.professor AS p WHERE p.id = 1 AND p.ativo = 1;
	@Query("SELECT p FROM Professor p WHERE p.id = :id AND p.ativo = :ativo")
	public Optional<Professor> buscaProfAtivoId(@Param("ativo") boolean ativo, @Param("id") Long id);
	
}
