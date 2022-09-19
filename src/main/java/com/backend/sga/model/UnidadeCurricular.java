package com.backend.sga.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class UnidadeCurricular {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String nome;
	
	@ManyToOne
	private Curso curso;
	
	//tratar com o grupo oq seria no @annotation
	@NotNull
	private double horas;
	
	@ManyToOne
	private Competencia competencia;
	
}
