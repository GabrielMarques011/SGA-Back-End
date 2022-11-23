package com.backend.sga.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Calendar data;
	
	@OneToOne
	private UnidadeCurricular unidadeCurricular;
	
	//aplicado
	@OneToOne
	private Curso curso;
	
	@NotNull
	private String codTurma;
	
	private Periodo periodo;
	
	private int partitionKey;
	
}
