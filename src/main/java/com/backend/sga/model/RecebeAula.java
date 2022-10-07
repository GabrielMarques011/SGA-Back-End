package com.backend.sga.model;

import java.util.Calendar;

import lombok.Data;

@Data
public class RecebeAula {

	private Curso curso;
	
	private UnidadeCurricular unidadeCurricular;
	
	private String codTurma;
	
	private Periodo periodo;
	
	private Calendar dataInicio;
	
	private boolean diaSemana[];

	private Ambiente ambiente;
	
	private Professor professor;
	
	private double cargaDiaria;
	
	private Calendar semanaldia[];
	
}
