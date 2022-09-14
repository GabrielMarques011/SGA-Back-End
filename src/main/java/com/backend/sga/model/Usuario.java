package com.backend.sga.model;

import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
public class Usuario {

	//@NotNull
	private String nome;
	
	//@NotNull
	private String email;
	
	//@NotNull
	private String senha;//fazer o hash
	
	//@NotNull
	private String nif;
	
	private TipoUsuario tipoUsuario;
	
}
