package com.backend.sga.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class RecebeMobile {

	private Ambiente ambiente;
	
	private List<Aula> aulas = new ArrayList<Aula>();
	
	public void addAula(Aula a) {
		aulas.add(a);
	}
	
}
