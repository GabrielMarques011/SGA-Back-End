package com.backend.sga.rest;

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

import com.backend.sga.model.Competencia;
import com.backend.sga.model.Erro;
import com.backend.sga.model.Sucesso;
import com.backend.sga.repository.CompetenciaRepository;

//CrossOrigin serve para que o projeto receba JSON
@CrossOrigin
@RestController
@RequestMapping("/api/competencia")
public class CompetenciaRestController {

	@Autowired
	private CompetenciaRepository competenciaRepository;
	
	// método para cadastrar uma ausencia
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Object> criarCompetencia(@RequestBody Competencia competencia, HttpServletRequest request){
		if(competencia != null) { // verifica se a competência não é nula
			competenciaRepository.save(competencia); // salva a competência no banco de dados
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso"); // moldando a mensagem de sucesso
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK); // retornando a mensagem de sucesso
		}else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível cadstrar a competência", null); // moldando a mensagem de erro
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR); // retornando a mensagem de erro
		}
	}
	
	// método para deletar a competência pelo ID
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deletarCompetencia(@PathVariable("id") Long id, Competencia competencia, HttpServletRequest request){
		if(competencia.getId() == id) { // verificando se o id da competência é igual ao da selecionada
			competenciaRepository.deleteById(id); // deleta a copetência do banco de dados
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso"); // moldando a mensagem de sucesso
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK); // retornando a mensagem de sucesso
		}else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível cadstrar a ausência", null); // moldando a mensagem de erro
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR); // retornando a mensagem de erro
		}
	}
	
	// método para listar todas as competências
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Competencia> listarCompetencias(){
		return competenciaRepository.findAll(); // retorna a lista de competências
	}
	
	//metodo para alterar
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> atualizarCompetencia(@PathVariable("id") Long id, @RequestBody Competencia competencia, HttpServletRequest request){
		if (competencia.getId() != id) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}else {
			competenciaRepository.save(competencia);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/nivel/{nivel}", method = RequestMethod.GET)
	public Iterable<Competencia> buscaNivel(@PathVariable("nivel") int nivel){
		
		return competenciaRepository.buscarNivel(nivel);
	}
}
