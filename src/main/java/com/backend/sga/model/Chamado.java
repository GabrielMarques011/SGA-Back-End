package com.backend.sga.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Chamado {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String descricao;
	
	@Column(columnDefinition = "LONGTEXT")
	private String foto;
	
	@OneToOne
	private Usuario usuario;
	
	@NotNull
	private TipoStatus status;
	
	@NotNull
	private TipoChamado tipo;

}
