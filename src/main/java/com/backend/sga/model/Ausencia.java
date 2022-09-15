package com.backend.sga.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Ausencia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//perguntar para o Matheus se irão utilizar essa pratica no React
	//@JsonProperty("start")
	@NotNull
	private Calendar dataInicio;
	
	//@JsonProperty("end")
	@NotNull
	private Calendar dataFinal;
	
	@ManyToOne
	private Professor professor;
	
	@NotNull
	private String tipo;
	
}
