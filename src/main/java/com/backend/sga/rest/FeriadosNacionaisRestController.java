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
import com.google.common.util.concurrent.Service;

import org.springframework.http.MediaType;

@RestController
@CrossOrigin
@RequestMapping("/api/feriados")
public class FeriadosNacionaisRestController {
	
	@Autowired
	private FeriadosNacionaisRepository repository;	
			
	// método que retorna os feriados nacionais 
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<FeriadosNacionais> buscarFeriados(){
		return repository.findAll(); // retorna a lista de feriados nascionais
	}
	
}
