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
import com.backend.sga.annotation.Privado;
import com.backend.sga.annotation.Publico;
import com.backend.sga.rest.UsuarioRestController;



@Component
public class AppInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		// VARIAVEL PARA OBTER A URI
		String uri = request.getRequestURI();
		
	
		
		// SE FOR PAGINA DE ERRO LIBERA
		if (uri.startsWith("/erro")) {
			return true;
		}
		
		// VERIFICA SE O HANDLER É UM HANDLER METHOD, O QUE INDICA QUE ELE ESTÁ CHAMANDO UM METODO EM ALGUM CONTROLLER
		if (handler instanceof HandlerMethod) {
			
			// CASTING DE OBJECT PARA HANDLER METHOD
			HandlerMethod metodo = (HandlerMethod) handler;
			
			// SE FOR PAGINAS DE API LIBERA
			if (uri.startsWith("/api")) {
				
				String token = null;
				
				// VERIFICA SE É UM METODO  USUARIO E FOR DIFERENTE DE NULO
				if (metodo.getMethodAnnotation(Privado.class) != null) {
					
					try {
						
						// SE O METODO FOR USUARIO OU ADM RECUPERA O TOKEN
						token = request.getHeader("Authorization");
							
						// BUSCANDO O ALGORITMO NO USUARIO E ADM
						Algorithm algoritmo = Algorithm.HMAC512(UsuarioRestController.SECRET);
						// OBJ PARA VERIFICAR O TOKEN
						JWTVerifier verifier = JWT.require(algoritmo).withIssuer(UsuarioRestController.EMISSOR).build();
						// DECODIFICA O TOKEN
						DecodedJWT jwt = verifier.verify(token);
						// RECUPERA OS DADOS DO PLAYLOAD (CLAIMS SÃO VALORES QUE VEM NO PLAYLOAD)
						Map<String, Claim> claims = jwt.getClaims();
						
						request.setAttribute("id",claims.get("id"));
						
						return true;			
						
					} catch (Exception e) {
						
						e.printStackTrace();
						if (token == null) {
							response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
						}else {
							response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
						}			
						return false;
					}
					
				}
				
			}  else {
				
				// VERFICA SE  ELE É PUBLICO
				if (metodo.getMethodAnnotation(Publico.class) != null) {					
					return true;
				}		
				return false;			
			}
			
		}		
		return true;
	}
}
