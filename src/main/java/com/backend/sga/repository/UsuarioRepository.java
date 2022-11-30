package com.backend.sga.repository;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Usuario;

@Repository
public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long>{

	public Usuario findByIdAndSenha(Long id, @NotNull String senha);
	
	public Usuario findByNif(String nif);
	
	public Usuario findByEmail(String email);
	
}
