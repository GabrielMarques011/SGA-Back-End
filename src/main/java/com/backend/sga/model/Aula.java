package com.backend.sga.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

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
	
	//@NotNull
	private double cargaDiaria;
	
	//@NotNull
	private Calendar dataInicio;
	
	//@NotNull
	private Calendar dataFinal;
	
	@OneToOne
	private UnidadeCurricular unidadeCurricular;
	
	//@NotNull
	private String codTurma;
	
	private Periodo periodo;
	
}
