package com.backend.sga.model;

import java.util.List;

import javax.persistence.OneToMany;

import lombok.Data;

@Data
public class RecebeProfComp {

	private String nome;
	
	private String email;
	
	private int cargaSemanal;
	
	private String foto;
	
	//evitar de criar uma tabela associativa
	@OneToMany(mappedBy = "professor")
	private List<Competencia> competencia;
	
}
