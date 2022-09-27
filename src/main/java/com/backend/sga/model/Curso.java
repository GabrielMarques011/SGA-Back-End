package com.backend.sga.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.Data;


@Data
@Entity
public class Curso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String nome;
	
	//aqui estamos puxando a Enum do TipoDeCurso, para que apareça os selects depois
	//Colocar depois o metodo getCargaHoraria
	
	private TipoCurso tipoCurso;
	
	private boolean ativo;
	
	//aplicando um list, para que ele possa retornar juntamente com o valor do curso (Solicitado pelo Arthur)
	@OneToMany(mappedBy = "curso")
	private List<UnidadeCurricular> unidadeCurricular;
		
}
