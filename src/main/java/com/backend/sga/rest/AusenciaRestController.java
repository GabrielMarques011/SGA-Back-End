package com.backend.sga.rest;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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

import com.backend.sga.model.Ambiente;
import com.backend.sga.model.Ausencia;
import com.backend.sga.model.Erro;
import com.backend.sga.model.Professor;
import com.backend.sga.model.Sucesso;
import com.backend.sga.repository.AusenciaRepository;

//CrossOrigin serve para que o projeto receba JSON
@CrossOrigin
@RestController
@RequestMapping("/api/ausencia")
public class AusenciaRestController {

	@Autowired
	private AusenciaRepository ausenciaRepository;
	
	//método para cadastrar uma ausencia
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarAusencia(@RequestBody Ausencia ausencia, HttpServletRequest request){
		if(ausencia != null) { // verificando se a ausencia não é nula
			ausenciaRepository.save(ausencia); // salvando a ausência no banco de dados
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso"); // moldando a mensagem de sucesso
			
			//criando um vetor para que armazene dois dados para retornar no ResponseEntity
			Object[] filtro = new Object[2];
			filtro[0] = sucesso;
			filtro[1] = ausencia.getId();
			
			//setando o o filtro junto com o 'Status OK'
			ResponseEntity<Object> okpost = new ResponseEntity<Object>(filtro, HttpStatus.OK);
			
			return okpost; // retornando a mensagem de sucesso
		}else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível cadstrar a ausência", null); // moldando a mensagem de erro
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR); // retornando a mensagem de erro
		}
	}
	
	// método que deleta a usencia pelo ID
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deletarAusencia(@PathVariable("id") Long id, Ausencia ausencia, HttpServletRequest request){
		if(ausencia.getId() == id) { // verifica se o id da ausencia é igual ao id selecionado
			ausenciaRepository.deleteById(id); // deleta a ausencia do banco de dados
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso"); // moldando a mensagem de sucesso
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK); // retornando a mesnagem de sucesso
		}else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível deletar a ausência", null); // moldando a mensagem de erro
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR); // retornando a mensagem de erro
		}
	}
	
	// método que lista as aus~encias
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Ausencia> listarAusencias(){
		return ausenciaRepository.findAll(); // retorna todos os itenas do banco de dados
	}
	
	//metodo para alterar
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> atualizarAusencia(@PathVariable("id") Long id, @RequestBody Ausencia ausencia, HttpServletRequest request){
		if (ausencia.getId() != id) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID invalido!", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}else {
			ausenciaRepository.save(ausencia);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/professor/{id}", method = RequestMethod.GET)
	public Object[] buscarAusenciaProfessor(@PathVariable("id") Long id) {
		List<Ausencia> ausencias = ausenciaRepository.findByProfessorId(id);
		Professor professor = ausencias.get(0).getProfessor();
		
		Object result[] = new Object[2];
		
		result[0] = professor;
		result[1] = ausencias;
		
		return result;
	}
	
}
