package com.backend.sga.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Usuario {
	
	private String nome;
	
	@NotNull
	private String email;
	
	@NotNull
	private String senha;//fazer o hash
	
	@Id
	private String nif;
	
	private TipoUsuario tipoUsuario;
	
}
