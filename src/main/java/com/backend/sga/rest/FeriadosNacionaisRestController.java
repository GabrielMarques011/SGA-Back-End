package com.backend.sga.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.backend.sga.annotation.Administrador;
import com.backend.sga.annotation.Suporte;
import com.backend.sga.model.Erro;
import com.backend.sga.model.FeriadosNacionais;
import com.backend.sga.model.Sucesso;
import com.backend.sga.repository.FeriadosNacionaisRepository;

@RestController
@CrossOrigin
@RequestMapping("/api/feriados")
public class FeriadosNacionaisRestController {
	
	@Autowired
	private FeriadosNacionaisRepository repository;	
			
	@RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<FeriadosNacionais> buscarFeriados(){
        
        Iterable<FeriadosNacionais> f = repository.findAll();

        int i = 0;
        for (FeriadosNacionais feriadosNacionais : f) {
            if(feriadosNacionais != null) {
                i++;
            }
        }
        if(i == 0) {
            return repository.findAll();
        } else {
            return repository.findAll();
        }
    }
	
	@Suporte
	@Administrador
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> excluir(@PathVariable("id") Long id){
		
		Optional<FeriadosNacionais> feriado = repository.findById(id);
		
		if (feriado.isEmpty()) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			repository.deleteById(id);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		}
	}
	
	@Suporte
	@Administrador
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> alterar(@PathVariable("id") long id, @RequestBody FeriadosNacionais feriado){
		
		Optional<FeriadosNacionais> feriadoDb = repository.findById(id);
		
		if(!feriadoDb.isEmpty()) {
			try {
				
				feriadoDb.get().setDate(feriado.getDate());
				feriadoDb.get().setName(feriado.getName());
				
				repository.save(feriadoDb.get());
				Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
				return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
			} catch (Exception e) {
				Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível alterar o feriado!", null);
				return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
