package com.backend.sga.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
@Entity
public class UnidadeCurricular {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String nome;
	
	//esse annotation evita de dar loop
	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToOne
	private Curso curso;
	
	//tratar com o grupo oq seria no @annotation
	@NotNull
	private double horas;
	
}