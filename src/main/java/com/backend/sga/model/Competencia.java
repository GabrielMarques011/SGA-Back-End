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
public class Competencia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//esse annotation evita de dar loop
	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne
	private Professor professor;
	
	@OneToOne
	private UnidadeCurricular unidadeCurricular;
	
	@NotNull
	private int nivel;
}
