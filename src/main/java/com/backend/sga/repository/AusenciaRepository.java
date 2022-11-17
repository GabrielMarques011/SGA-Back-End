package com.backend.sga.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Ausencia;

@Repository
public interface AusenciaRepository extends PagingAndSortingRepository<Ausencia, Long>{

	public List<Ausencia> findByProfessorId(Long id);
	
	//SELECT  a.data_inicio, a.data_final FROM sga.ausencia AS a WHERE a.professor_id = 1;
	@Query("SELECT a FROM Ausencia a WHERE a.professor.id = :id AND a.tipo = 0")
	public List<Ausencia> listaAusenciaDeProf(@Param("id") Long id);
}
