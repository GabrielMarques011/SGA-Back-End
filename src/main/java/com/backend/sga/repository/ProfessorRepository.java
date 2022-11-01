package com.backend.sga.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

	//SELECT p.* FROM sga.professor AS p INNER JOIN sga.competencia AS c ON c.professor_id = p.id INNER JOIN sga.unidade_curricular AS uc ON c.unidade_curricular_id = uc.id INNER JOIN sga.curso AS cu ON uc.curso_id = cu.id WHERE cu.nome = 'PowerBI Avançado' AND uc.nome = 'PowerBI' ORDER BY p.id;
	@Query("SELECT p FROM Professor p INNER JOIN Competencia c ON c.professor.id = p.id INNER JOIN UnidadeCurricular uc ON c.unidadeCurricular.id = uc.id INNER JOIN Curso cu ON uc.curso.id = cu.id WHERE cu.nome = :nomeCr AND uc.nome = :nomeUc ORDER BY p.id")
	public List<Professor> listProfcuc(@Param("nomeCr") String nomeCr, @Param("nomeUc") String nomeUc);
	
}
