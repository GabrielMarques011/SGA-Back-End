package com.backend.sga.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.Data;

@Data
public class RecebeAula {

	private Curso curso;
	
	private UnidadeCurricular unidadeCurricular;
	
	private String codTurma;
	
	private Periodo periodo;
	
	private Calendar dataInicio;
	
	private Calendar dataFinal;
	
	private boolean diaSemana[];

	private Ambiente ambiente;
	
	private Professor professor;
	
	private double cargaDiaria;
	
	private List<Professor> profList;
	
	private int partitionKey;
	
	public ArrayList<Integer> verificarDiasSemana(boolean dias[]) {
		
		ArrayList<Integer> diasSemana = new ArrayList<Integer>();
		
		for (int i = 0; i < dias.length; i++) {
			
			//pegando os dias que são na semana
			if (dias[i]) {
				
				if (i == 0) {
					diasSemana.add(Calendar.SUNDAY);
				}else if(i == 1) {
					diasSemana.add(Calendar.MONDAY);
				}else if(i == 2) {
					diasSemana.add(Calendar.TUESDAY);
				}else if(i == 3) {
					diasSemana.add(Calendar.WEDNESDAY);
				}else if(i == 4) {
					diasSemana.add(Calendar.THURSDAY);
				}else if(i == 5) {
					diasSemana.add(Calendar.FRIDAY);
				}else {
					diasSemana.add(Calendar.SATURDAY);
				}
				
			}
			
		}
		
		return diasSemana;
	}
	
	
	
}
