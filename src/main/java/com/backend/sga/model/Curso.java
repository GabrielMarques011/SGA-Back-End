package com.backend.sga.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;


@Data
@Entity
public class Curso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String nome;
	
	private TipoCurso tipo;
	
	private boolean ativo;
	
	//@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany(mappedBy = "curso")
	private List<UnidadeCurricular> unidadeCurricular;
		
}
