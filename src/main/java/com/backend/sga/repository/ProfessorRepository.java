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
	
	//SELECT p.* FROM sga.professor AS p INNER JOIN sga.aula AS a ON a.professor_id = p.id INNER JOIN sga.unidade_curricular AS uc ON a.unidade_curricular_id = uc.id INNER JOIN sga.curso AS c ON c.id = uc.curso_id WHERE c.nome = 'java' AND uc.nome = 'pacote word';
	/*@Query("SELECT p FROM Professor p INNER JOIN Aula a "
			+ "ON a.professor.id = p.id "
			+ "INNER JOIN UnidadeCurricular uc "
			+ "ON a.unidadeCurricular.id = uc.id "
			+ "INNER JOIN Curso c ON uc.curso.id = c.id "
			+ "WHERE c.nome = :nome AND uc.nome = :nome")
	public List<Professor> listaProf(@Param("nome") String nome);*/
	
//	SELECT p.* FROM sga.professor as p inner join sga.competencia as c on p.id = c.professor_id 
//	inner join sga.unidade_curricular as un on c.unidade_curricular_id = un.id
//	inner join sga.curso as cur on un.curso_id = cur.id
//	where cur.nome like "%excel%" or un.nome like "%excel%"
	@Query("SELECT p FROM Professor p INNER JOIN Competencia c ON p.id = c.professor.id INNER JOIN UnidadeCurricular un ON c.unidadeCurricular.id = un.id WHERE un.nome LIKE '%:value%'")
	public List<Professor> buscaUnidade(@Param("value") String value);

}
