package com.backend.sga.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class UnidadeCurricular {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//@NotNull lembrar de importar
	private String nome;
	
	@ManyToOne
	private Curso curso;
	
	//tratar com o grupo oq seria no @annotation
	//@NotNull
	private double horas;
	
}
