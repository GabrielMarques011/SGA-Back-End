package com.backend.sga.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Usuario {
	
	private String nome;
	
	@NotNull
	@Column(unique = true)
	@Email
	private String email;
	
	@NotNull
	private String senha;//Ja fizemos a criptografia da senha
	
	@Id
	private String nif;
	
	private TipoUsuario tipoUsuario;
	
	//utilizando para setar o usuario como ativo ou inativo
	private Boolean ativo;
	
	@OneToMany
	private Chamado chamado;
	
}
