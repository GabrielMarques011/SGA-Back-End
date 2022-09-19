package com.backend.sga.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Ambiente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String nome;
	
	@NotNull
	private int capacidade;
	
	private TipoAmbiente tipoAmbiente;
	
	@NotNull
	private String cep;
	
	@NotNull
	private String complemento;
	
}
