package com.backend.sga.rest;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.backend.sga.model.Erro;
import com.backend.sga.model.Sucesso;
import com.backend.sga.model.TokenJWT;
import com.backend.sga.model.Usuario;
import com.backend.sga.repository.UsuarioRepository;

//CrossOrigin serve para que o projeto receba JSON
@CrossOrigin
@RestController
@RequestMapping("/api/usuario")
public class UsuarioRestController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public static final String SECRET = "$!$t3m4D3G3r3nç!4m3nt0D34vd!t0r!0";
	
	public static final String EMISSOR = "SGA";
	
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
			
			Object[] filtro = new Object[2];
			filtro[0] = sucesso;
			filtro[1] = user.getNif();
			
			//setando o o filtro junto com o 'Status OK'
			ResponseEntity<Object> okpost = new ResponseEntity<Object>(filtro, HttpStatus.OK);
			
			return okpost;
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
	public ResponseEntity<Object> atualizarUser(@PathVariable("nif") String nif,@RequestBody Usuario user, HttpServletRequest request){
		if (nif == null) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "NIF inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
			//vendo se o user é existente
			usuarioRepository.findById(nif);
		
			//pegando a senha e transformando em criptografia
			String crip = this.encoder.encode(user.getSenha());
			
			//mandando criptografada
			//user.setSenha(crip);
			user.setSenha(crip);
		
			usuarioRepository.save(user);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
	}
	
	
	//metodo feito para validar quando criamos o login
	//metodo feito para comparar se a senha bate com a do banco de dados
	public Boolean validarSenha(Usuario user){
		//pegando a senha do banco de dados
		String senha = usuarioRepository.findById(user.getNif()).get().getSenha();
		//pegando a senha do banco e comparando com a atual
		Boolean valid = encoder.matches(user.getSenha(), senha);
		return valid;
	}
	
	
	/*public ResponseEntity<Object> senhaValida(@RequestBody Usuario user, HttpServletRequest request){
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
	}*/
	
	//metodo para login com tokien (teste)
	@RequestMapping(value = "/login", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> login(@RequestBody Usuario usuario, HttpServletRequest request){
		
		
		//ResponseEntity<Object> validar = senhaValida(usuario, request);
		//Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso ao Logar");
		//ResponseEntity<Object> ok =  ResponseEntity<Object>(sucesso, HttpStatus.OK);
		
		Boolean validar = validarSenha(usuario);
	
		
		if (validar = true) {
			// insere nif e senha dentro da variavel usuario
						usuario = usuarioRepository.findByNifAndSenha(usuario.getNif(), usuario.getSenha());
						
						// verifica se há usuario 
						if (usuario != null) {
							System.out.println(usuario.getNome());
							
							// cria uma variavel payload e insere os dados do usuario no payload
							Map<String, Object> payload = new HashMap<String, Object>();
							
							payload.put("nome", usuario.getNome());
							payload.put("email", usuario.getEmail());
							payload.put("senha", usuario.getSenha());
							payload.put("nif", usuario.getNif());
							payload.put("tipoUsuario", usuario.getTipoUsuario());
							payload.put("ativo", usuario.getAtivo());
							
							//cria variavel para data de expiração
							Calendar expiracao = Calendar.getInstance();
							
							//adiciona expiração a variavel para expirar o token
							expiracao.add(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH + 7);
							
							//coloca assinatura do algoritmo no token 
							Algorithm algoritmo = Algorithm.HMAC512(SECRET);
							
							//instancia a classe token
							TokenJWT tokenJwt = new TokenJWT();
							
							// adiciona os recursos no token
							tokenJwt.setToken(JWT.create()
									.withPayload(payload)
									.withIssuer(EMISSOR)
									.withExpiresAt(expiracao.getTime())
									.sign(algoritmo));
							
							System.out.println(tokenJwt);
							
							//envia o token
							return ResponseEntity.ok(tokenJwt);
						}
		}
		
		return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
	}
	
	
	
}
