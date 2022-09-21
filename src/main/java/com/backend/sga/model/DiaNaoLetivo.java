package com.backend.sga.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
public class DiaNaoLetivo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Calendar dataInicio;
	
	@NotNull
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Calendar dataFinal;
	
	@NotNull
	private String nome;
	
	private TipoDeDia tipoDeDia;
	
}