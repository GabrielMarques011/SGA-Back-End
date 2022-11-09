package com.backend.sga.model;

import java.util.List;

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
public class Professor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String nome;
	
	@NotNull
	@Email
	//@Column(unique = true)
	private String email;//fazer a criptografia
	
	@NotNull
	private int cargaSemanal;
	
	private Boolean ativo;
	
	//evitar de criar uma tabela associativa
	@OneToMany(mappedBy = "professor")
	private List<Competencia> competencia;
	
	@Column(columnDefinition = "LONGTEXT")
	private String foto;
	
}