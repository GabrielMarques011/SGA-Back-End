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
import com.backend.sga.annotation.Administrador;
import com.backend.sga.annotation.Suporte;
import com.backend.sga.model.Erro;
import com.backend.sga.model.Sucesso;
import com.backend.sga.model.TokenJWT;
import com.backend.sga.model.Usuario;
import com.backend.sga.repository.UsuarioRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/usuario")
public class UsuarioRestController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public static final String SECRET = "$!$t3m4D3G3r3nç!4m3nt0D34vd!t0r!0";

	public static final String EMISSOR = "SGA";

	// passando o parametro do BCrypto dentro encoder para que salva a criptografia
	private PasswordEncoder encoder = new BCryptPasswordEncoder();

	@Suporte
	// metodo para criar o professor
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> criarUser(@RequestBody Usuario user, HttpServletRequest request) {
        System.out.println();
        if (usuarioRepository.findByNif(user.getNif()) != null) {
            Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Nif já cadastrado.", null);
            return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (usuarioRepository.findByEmail(user.getEmail()) != null) {
            Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Email já cadastrado.", null);
            return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (user != null && usuarioRepository.findByNif(user.getNif()) == null) {
            // pegando a senha e transformando em criptografia
            String crip = this.encoder.encode(user.getSenha());
            // mandando criptografada
            user.setSenha(crip);
            // deixando o usuario como ativo no banco de dados
            user.setAtivo(true);
            usuarioRepository.save(user);
            Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
            Object[] filtro = new Object[2];
            filtro[0] = sucesso;
            filtro[1] = user;// ajeitando o metodo depois que apliquei o id na model
            // setando o o filtro junto com o 'Status OK'
            ResponseEntity<Object> okpost = new ResponseEntity<Object>(filtro, HttpStatus.OK);
            return okpost;
        } else {
            Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar um usuário", null);
            return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@Suporte
	@RequestMapping(value = "/desativar/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> desativaUser(@PathVariable("id") Long id, HttpServletRequest request) {

		Optional<Usuario> inativar = usuarioRepository.findById(id);// setando o Ativo como false, para estar desativado

		if (inativar.get().getId() == id) {
			if (inativar.get().getAtivo() == false) {
				inativar.get().setAtivo(true);
			} else {
				inativar.get().setAtivo(false);
			}
			usuarioRepository.save(inativar.get());
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel desativar usuario", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Suporte
	// Buscando todos os dados no banco
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Usuario> listaUser(Usuario user) {
		return usuarioRepository.findAll();
	}

	@Suporte
	// Editar usuario não precisa de senha
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> atualizarUser(@PathVariable("id") Long id, @RequestBody Usuario user,
            HttpServletRequest request) {
        if (user.getId() != id) {
            Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
            return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            // vendo se o user é existente
            Usuario userRepository = usuarioRepository.findById(id).get();
            boolean nifIgual = user.getNif().equals(userRepository.getNif());
            boolean emailIgual = user.getEmail().equals(userRepository.getEmail());
            if (usuarioRepository.findByNif(user.getNif()) != null) {
                if (!nifIgual) {
                    Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Nif já cadastrado.", null);
                    return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            if (usuarioRepository.findByEmail(user.getEmail()) != null) {
                if (!emailIgual) {
                    Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Email já cadastrado.", null);
                    return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            user.setSenha(userRepository.getSenha());
            usuarioRepository.save(user);
            Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
            return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
        }
    }
	
	@Suporte
	@Administrador
	// Editar perfil que precisa da senha
    @RequestMapping(value = "/perfil/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> atualizarUserPerfil(@PathVariable("id") Long id, @RequestBody Usuario user,
            HttpServletRequest request) {
        if (user.getId() != id) {
            Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
            return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            // vendo se o user é existente
            Usuario userRepository = usuarioRepository.findById(id).get();
            boolean nifIgual = user.getNif().equals(userRepository.getNif());
            boolean emailIgual = user.getEmail().equals(userRepository.getEmail());
            if (user.getSenha() == null) {
                user.setSenha(userRepository.getSenha());
            } else {
                String crip = this.encoder.encode(user.getSenha());
                user.setSenha(crip);
            }
            if (usuarioRepository.findByNif(user.getNif()) != null) {
                if (!nifIgual) {
                    Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Nif já cadastrado.", null);
                    return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            if (usuarioRepository.findByEmail(user.getEmail()) != null) {
                if (!emailIgual) {
                    Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Email já cadastrado.", null);
                    return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            user.setAtivo(true);
            user.setTipo(userRepository.getTipo());
            usuarioRepository.save(user);
            Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
            return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
        }
    }

	// metodo feito para validar quando criamos o login
	// metodo feito para comparar se a senha bate com a do banco de dados
	public Boolean validarSenha(Usuario user, String senha) {
		// pegando a senha do banco de dados
		String senhaCrip = usuarioRepository.findById(user.getId()).get().getSenha();
		// pegando a senha do banco e comparando com a atual
		Boolean valid = encoder.matches(senha, senhaCrip);
		return valid;
	}

	@Suporte
	@Administrador
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(@RequestBody Usuario usuario, HttpServletRequest request) {

       Usuario user = usuarioRepository.findByNif(usuario.getNif());
        if(user != null) {
            Boolean validar = validarSenha(user, usuario.getSenha());
            if (validar == true) {

                   // cria uma variavel payload e insere os dados do usuario no payload
                    Map<String, Object> payload = new HashMap<String, Object>();
                    
                    payload.put("id", user.getId());
                    payload.put("nome", user.getNome());
                    payload.put("email", user.getEmail());
                    payload.put("senha", user.getSenha());
                    payload.put("nif", user.getNif());
                    payload.put("tipoUsuario", user.getTipo().toString());

                   // cria variavel para data de expiração
                    Calendar expiracao = Calendar.getInstance();

                   // adiciona expiração a variavel para expirar o token
                    expiracao.add(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH + 7);

                   // coloca assinatura do algoritmo no token
                    Algorithm algoritmo = Algorithm.HMAC512(SECRET);

                   // instancia a classe token
                    TokenJWT tokenJwt = new TokenJWT();

                    // adiciona os recursos no token
                    tokenJwt.setToken(JWT.create().withPayload(payload).withIssuer(EMISSOR)
                            .withExpiresAt(expiracao.getTime()).sign(algoritmo));

                   System.out.println(tokenJwt);

                   // envia o token
                    return ResponseEntity.ok(tokenJwt);
                }
            }
       return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
    }

}
