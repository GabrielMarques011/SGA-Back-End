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
import com.backend.sga.model.UnidadeCurricular;

@Repository
public interface ProfessorRepository extends PagingAndSortingRepository<Professor, Long>{

	//SELECT * FROM sga.professor WHERE sga.professor.nome LIKE '%r%'
	@Query("SELECT p FROM Professor p WHERE p.nome LIKE %:nome%")
	//Retorna uma lista onde aparece o relacionamento entre as 'letras' escritas com o banco
	public List<Professor> palavraChave(@Param("nome") String nome);
	
	// SELECT p.* FROM sga.professor as p inner join sga.competencia as c on p.id = c.professor_id inner join sga.aula as a on a.professor_id = p.id where c.unidade_curricular_id = 1 and a.periodo = 1 and a.data = "2022-10-05"
	
}
