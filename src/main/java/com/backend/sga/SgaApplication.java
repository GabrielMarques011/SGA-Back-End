package com.backend.sga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.ws.config.annotation.EnableWs;

//estamos desabilitando os endpoints para que cada alteração não tenha que ficar fazendo login
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableFeignClients
public class SgaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SgaApplication.class, args);
	}
	
	
	//o annotation Bean serve para que consiga carregar possiveis alterações dentro do metodo

}
