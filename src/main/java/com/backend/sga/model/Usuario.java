package com.backend.sga.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String nome;
	
	@NotNull
	@Column(unique = true)
	@Email
	private String email;//fazer a criptografia
	
	@NotNull
	private String senha;
	
	@NotNull
	private String nif;
	
	private TipoUsuario tipoUsuario;
	
	private Boolean ativo;
	
}
