package com.backend.sga.model;

import java.util.Calendar;

import lombok.Data;

@Data
public class RecebeBuscaAmbiente {
	private Ambiente ambiente;
	
	private Periodo periodo;
	
	private Calendar dataInicio;
	
	private Calendar dataFinal;
	
	private boolean diasSemana[];
}
