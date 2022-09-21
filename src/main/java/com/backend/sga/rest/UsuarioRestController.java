package com.backend.sga.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.backend.sga.model.Erro;
import com.backend.sga.model.Sucesso;
import com.backend.sga.model.Usuario;
import com.backend.sga.repository.UsuarioRepository;

//CrossOrigin serve para que o projeto receba JSON
@CrossOrigin
@RestController
@RequestMapping("/api/usuario")
public class UsuarioRestController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	//passando o parametro do BCrypto dentro encoder para que salva a criptografia
	private PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	//metodo para criar o professor
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarUser (@RequestBody Usuario user, HttpServletRequest request){
		if(user != null) {
			
			//pegando a senha e transformando em criptografia
			String crip = this.encoder.encode(user.getSenha());
			
			//mandando criptografada
			user.setSenha(crip);
			
			//deixando o usuario como ativo no banco de dados
			user.setAtivo(true);
			
			usuarioRepository.save(user);
			
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		}else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar um Usuario", null);							
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/desativar/{nif}", method = RequestMethod.PUT)
	public ResponseEntity<Object> desativarUsuario(@PathVariable("nif") String nif, Usuario user, HttpServletRequest request){

		//buscando ele pelo id para alterar
		user = usuarioRepository.findById(nif).get();
		//setando ela como inativo
		user.setAtivo(false);
		//salvando no banco como inativo
		usuarioRepository.save(user);
			
		Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
		return new ResponseEntity<Object>(sucesso, HttpStatus.OK);	
	}
	
	//Buscando todos os dados no banco
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Usuario> listaUser (Usuario user){
		return usuarioRepository.findAll();
	}
	
	//metodo para alterar
	@RequestMapping(value = "/{nif}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> atualizarUser(@PathVariable("nif") String nif, @RequestBody Usuario user, HttpServletRequest request){
		if (!user.getNif().equals(nif)) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "NIF inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
			//pegando a senha e transformando em criptografia
			String crip = this.encoder.encode(user.getSenha());
			
			//mandando criptografada
			user.setSenha(crip);
		
			usuarioRepository.save(user);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
	}
	
	
	//metodo feito para validar quando criamos o login
	//metodo feito para comparar se a senha bate com a do banco de dados
	public Boolean validarSenha (Usuario user){
		//pegando a senha do banco de dados
		String senha = usuarioRepository.findById(user.getNif()).get().getSenha();
		//pegando a senha do banco e comparando com a atual
		Boolean valid = encoder.matches(user.getSenha(), senha);
		return valid;
	}
	
	//metodo para verificar se o login do Usuario esta correto
	@RequestMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> login (@RequestBody Usuario user, HttpServletRequest request){
		//trazendo valores do valid do 'validarSenha'
		Boolean valid = validarSenha(user);
		//verificando se a senha foi digitada corretamente
		if (!valid) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Login ou Senha incorreta", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//mensagem de OK, caso esteja tudo certo
		Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso ao Logar");
		return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
	}
	
}
