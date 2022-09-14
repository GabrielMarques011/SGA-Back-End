package com.backend.sga.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;


@Data
@Entity
public class Curso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//@NotNull lembrar de importar
	private String nome;
	
	//aqui estamos puxando a Enum do TipoDeCurso, para que apareça os selects depois
	//Colocar depois o metodo getCargaHoraria
	@OneToOne
	private TipoCurso tipoCurso;
		
}
