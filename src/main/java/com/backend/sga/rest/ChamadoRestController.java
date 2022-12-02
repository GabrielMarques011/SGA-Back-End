package com.backend.sga.rest;

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

import com.backend.sga.annotation.User;
import com.backend.sga.annotation.Administrador;
import com.backend.sga.model.Chamado;
import com.backend.sga.model.Erro;
import com.backend.sga.model.Sucesso;
import com.backend.sga.model.TipoChamado;
import com.backend.sga.model.TipoStatus;
import com.backend.sga.repository.ChamadoRepository;

@RestController
@CrossOrigin
@RequestMapping("/api/chamado")
public class ChamadoRestController {

	@Autowired
	private ChamadoRepository repository;

	@User
	@Administrador
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarChamado(@RequestBody Chamado chamado, HttpServletRequest request) {
		if (chamado != null) {
			repository.save(chamado);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");

			Object[] filtro = new Object[2];
			filtro[0] = sucesso;
			filtro[1] = chamado.getId();

			ResponseEntity<Object> okpost = new ResponseEntity<Object>(filtro, HttpStatus.OK);
			return okpost;
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível criar um chamado", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Administrador
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Chamado> buscaChamados() {
		return repository.findAll();
	}

	@Administrador
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deletarChamado(@PathVariable("id") Long id, HttpServletRequest request) {
		Optional<Chamado> del = repository.findById(id);
		if (del.get().getId() == id) {
			repository.delete(del.get());
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível deletar o chamado", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Administrador
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> alterarChamado(@PathVariable("id") Long id, @RequestBody Chamado chamado,
			HttpServletRequest request) {
		if (id != chamado.getId()) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			repository.save(chamado);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		}
	}
	
	@Administrador
	@RequestMapping(value = "/alterarStatus/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> alterarStatus(@PathVariable("id") Long id, HttpServletRequest request) {

		Optional<Chamado> alterar = repository.findById(id);

		if (alterar.get().getId() == id) {
			alterar.get().setStatus(TipoStatus.FECHADO);
			repository.save(alterar.get());
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel alterar o status do chamado", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Administrador
	@RequestMapping(value = "/tipochamado")
	public TipoChamado[] entregaChamado() {
		return TipoChamado.values();
	}
}