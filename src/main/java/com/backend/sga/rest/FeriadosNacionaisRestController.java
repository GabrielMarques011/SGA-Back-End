package com.backend.sga.rest;

import java.sql.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.backend.sga.model.Erro;
import com.backend.sga.model.FeriadosNacionais;
import com.backend.sga.model.Sucesso;
import com.backend.sga.repository.FeriadosNacionaisRepository;
import org.springframework.http.MediaType;

@RestController
@CrossOrigin
@RequestMapping("/api/feriados")
public class FeriadosNacionaisRestController {
	
	private static final String FeriadosNacionais = null;
	@Autowired
	private FeriadosNacionaisRepository repository;	
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Object[] buscaFeriadosAPI(){
		int ano = LocalDate.now().getYear();
		
		String url = "https://brasilapi.com.br/api/feriados/v1/" + ano;
		
		RestTemplate restTemplate = new RestTemplate();
		
		Object[] feriados = restTemplate.getForObject(url, Object[].class);
				
		return feriados;
	}
	
	@RequestMapping(value = "/salvar", method = RequestMethod.POST)
	public ResponseEntity<Object> salvaFeriadosAPI(){
		int ano = LocalDate.now().getYear();
		
		String url = "https://brasilapi.com.br/api/feriados/v1/" + ano;
		
		RestTemplate restTemplate = new RestTemplate();
		
		Object[] feriados = restTemplate.getForObject(url, Object[].class);
		
		if(feriados != null) {
			for(int i = 0; i < feriados.length; i++) {
				FeriadosNacionais feriado = new FeriadosNacionais();
				feriado.setDados(feriados[i].toString());
				repository.save(feriado);
				System.out.println("passou no salvar");
			}
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso"); // moldando a mensagem de sucesso
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK); // retornando a mensagem de sucesso
		}else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "não foi possível salvar os feriados", null); // moldando a mensagem de erro
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR); // retornando a mensagem de erro
		}	
		
	}
	
	
	
	
	
	
}
