package com.backend.sga.repository;

import javax.validation.constraints.NotNull;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.backend.sga.model.Usuario;

@Repository
public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, String>{

	public Usuario findByNifAndSenha(String nif, @NotNull String senha);
	
}
