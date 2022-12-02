package com.backend.sga.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.backend.sga.annotation.User;
import com.backend.sga.annotation.Administrador;
import com.backend.sga.model.TipoUsuario;
import com.backend.sga.rest.UsuarioRestController;

@Component
public class AppInterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// variável para obter a URI
		String uri = request.getRequestURI();
		
		if(uri.startsWith("/erro")) {
			return true;
		}
		
		// verifica se está chamando um método de algum restController
		if(handler instanceof HandlerMethod) {
			
			// fazendo um casting do Object para HandlerMethod
			HandlerMethod metodo = (HandlerMethod) handler;
			
			// se for páginas de api libera
			if(uri.startsWith("/api")) {
				
				// variável para o token
				String token = null;
				
				token = request.getHeader("Autorization");
				
				// buscando o algoritmo do algoritmo do Usuario
				Algorithm algoritmo = Algorithm.HMAC512(UsuarioRestController.SECRET);
				
				// Obj para verificar o token
				JWTVerifier verifier = JWT.require(algoritmo).withIssuer(UsuarioRestController.EMISSOR).build();
				
				// decodifica o token
				DecodedJWT jwt = verifier.verify(token);
				
				// recupera os dados do payload (Claims os valores que vem no payload)
				Map<String, Claim> claims = jwt.getClaims();
				
				TipoUsuario tipo = TipoUsuario.values()[Integer.parseInt(claims.get("tipoUsuario").toString())];
				
				// se tiver tentando acessar um método com o anotation @Administrador
				if(metodo.getMethodAnnotation(User.class) != null) { 
					if(tipo == TipoUsuario.ADMINISTRADOR) {
						response.setStatus(HttpStatus.OK.value());
						return true;
					} else {
						response.sendError(HttpStatus.UNAUTHORIZED.value(), "Acesso negado");
						return false;
					}
					// se tiver tentando acessar um método com o anotation @Suporte
				} else if(metodo.getMethodAnnotation(Administrador.class) != null) {
					if(tipo == TipoUsuario.ADMINISTRADOR) {
						response.setStatus(HttpStatus.OK.value());
						return true;
					}else {
						response.sendError(HttpStatus.UNAUTHORIZED.value(), "Acesso negado");
					}
				}
				
			}
		}
		
		return true;
	}
}
