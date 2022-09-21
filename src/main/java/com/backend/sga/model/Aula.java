package com.backend.sga.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
public class Aula {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	private Professor professor;
	
	@OneToOne
	private Ambiente ambiente;
	
	@NotNull
	private double cargaDiaria;
	
	@NotNull
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Calendar dataInicio;
	
	@NotNull
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Calendar dataFinal;
	
	@OneToOne
	private UnidadeCurricular unidadeCurricular;
	
	@NotNull
	private String codTurma;
	
	private Periodo periodo;
	
}
