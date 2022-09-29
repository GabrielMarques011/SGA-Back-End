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
import com.backend.sga.service.FeriadosNacionaisService;

import org.springframework.http.MediaType;

@RestController
@CrossOrigin
@RequestMapping("/api/feriados")
public class FeriadosNacionaisRestController {
	
	@Autowired
	private FeriadosNacionaisRepository repository;	
	
	@Autowired
	private FeriadosNacionaisService service;
		
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Object> salvarFeriados(){
		
		FeriadosNacionais feriados[] = service.consultaFeriados();
		
		if(feriados != null) {
			for(int i = 0; i < feriados.length; i++) {
				FeriadosNacionais feriado = new FeriadosNacionais();
				feriado.setDate(feriados[i].getDate());
				feriado.setName(feriados[i].getName());
				feriado.setType(feriados[i].getType());
				
				repository.save(feriado);
			}
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		}else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar os feriados nacionais", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	// método que retorna os feriados nacionais 
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<FeriadosNacionais> buscarFeriados(){
		return repository.findAll(); // retorna a lista de feriados nascionais
	}
	
}
