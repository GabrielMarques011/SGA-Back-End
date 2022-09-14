package com.backend.sga.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class DiaNaoLetivo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//@NotNull
	private Calendar dataInicio;
	
	//@NotNull
	private Calendar dataFinal;
	
	//@NotNull
	private String nome;
	
	private TipoDeDia tipoDeDia;
	
}
