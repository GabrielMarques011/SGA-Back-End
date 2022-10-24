package com.backend.sga.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.Data;


@Data
@Entity
public class Curso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String nome;
	
	private TipoCurso tipoCurso;
	
	private boolean ativo;
	
	//(mappedBy = "curso")
	@OneToMany
	private List<UnidadeCurricular> unidadeCurricular;
		
}
