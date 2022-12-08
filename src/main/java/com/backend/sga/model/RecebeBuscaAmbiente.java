package com.backend.sga.model;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class RecebeBuscaAmbiente {

	private Ambiente ambiente;

	private Periodo periodo;

	// @JsonFormat(pattern = "dd/MM/yyyy")
	private Calendar dataInicio;

	// @JsonFormat(pattern = "dd/MM/yyyy")
	private Calendar dataFinal;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Calendar dtInicio;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Calendar dtFinal;

	private boolean diasSemana[];

	private Professor professor;

	private UnidadeCurricular unidadeCurricular;

}
