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

import com.backend.sga.annotation.Administrador;
import com.backend.sga.annotation.Suporte;
import com.backend.sga.model.Competencia;
import com.backend.sga.model.Erro;
import com.backend.sga.model.Sucesso;
import com.backend.sga.repository.CompetenciaRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/competencia")
public class CompetenciaRestController {

	@Autowired
	private CompetenciaRepository competenciaRepository;

	@Administrador
	@Suporte
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Object> criarCompetencia(@RequestBody Competencia competencia, HttpServletRequest request) {
		if (competencia != null) {
			competenciaRepository.save(competencia);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");

			Object[] filtro = new Object[2];
			filtro[0] = sucesso;
			filtro[1] = competencia.getId();

			ResponseEntity<Object> okpost = new ResponseEntity<Object>(filtro, HttpStatus.OK);
			return okpost;
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível cadstrar a competência", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Administrador
	@Suporte
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deletarCompetencia(@PathVariable("id") Long id, Competencia competencia,
			HttpServletRequest request) {
		if (competencia.getId() == id) {
			competenciaRepository.deleteById(id);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível cadstrar a ausência", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Administrador
	@Suporte
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Competencia> listarCompetencias() {
		return competenciaRepository.findAll();
	}

	@Administrador
	@Suporte
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> atualizarCompetencia(@PathVariable("id") Long id,
			@RequestBody Competencia competencia, HttpServletRequest request) {
		if (competencia.getId() != id) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			competenciaRepository.save(competencia);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		}
	}

	@Administrador
	@Suporte
	@RequestMapping(value = "/nivel/{nivel}", method = RequestMethod.GET)
	public Iterable<Competencia> buscaNivel(@PathVariable("nivel") int nivel) {
		return competenciaRepository.buscarNivel(nivel);
	}
}
