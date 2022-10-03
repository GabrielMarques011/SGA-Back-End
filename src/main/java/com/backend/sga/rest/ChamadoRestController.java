package com.backend.sga.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.backend.sga.model.Chamado;
import com.backend.sga.model.Erro;
import com.backend.sga.model.Sucesso;
import com.backend.sga.model.tipoChamado;
import com.backend.sga.repository.ChamadoRepository;

@RestController
@CrossOrigin
@RequestMapping("/api/chamado")
public class ChamadoRestController {
	
	@Autowired
	private ChamadoRepository repository;
	
	// método que cria uma novo chamado
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarChamado (@RequestBody Chamado chamado, HttpServletRequest request){
		if(chamado != null) {  // verifica se o chamado não é nulo
			repository.save(chamado); // salva o chamado no banco de dados
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso"); // molda a mensagem de sucesso
			
			//criando um vetor para que armazene dois dados para retornar no ResponseEntity
			Object[] filtro = new Object[2];
			filtro[0] = sucesso;
			filtro[1] = chamado.getId();
			
			//setando o o filtro junto com o 'Status OK'
			ResponseEntity<Object> okpost = new ResponseEntity<Object>(filtro, HttpStatus.OK);
			
			return okpost; //retorna a mensagem de sucesso
		}else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível criar um chamado", null); // molda a mensagem de erro
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR); // retorna a mensagem de erro
		}
	}
	
	// método que busca todos os chamados
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Chamado> buscaChamados(){ 
		return repository.findAll(); // retorna a lista de todos os chamados
	}
	
	// método que deleta o chamado pelo id
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deletarChamado (@PathVariable("id") Long id, @RequestBody Chamado chamado, HttpServletRequest request){
		if(id == chamado.getId()) { // verifica se o id indicado é igual ao id do chamado
			repository.deleteById(id); // deleta o chamado do banco de dados
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso"); // molda a mensagem de sucesso
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK); // retorna a mensagem de sucesso
		}else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível deletar o chamado", null); // molda a mensagem de erro
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR); // retorna a mensagem de erro
		}
	}
	
	// método para alterar o chamado
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> alterarChamado(@PathVariable("id") Long id, @RequestBody Chamado chamado, HttpServletRequest request){
		if(id != chamado.getId()) { // verifica se o id passado é diferente do id do chamado
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null); // molda a mensagem de erro
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR); // retorna a mensagem de erro
		}else {
			repository.save(chamado); // salva o chamado no banco de dados
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso"); // molda a mensagem de sucesso
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK); // retorna a mensagem de sucesso
		}
	}
	
	
	@RequestMapping(value = "/tipochamado")
	public tipoChamado[] entregaChamado(){
		return tipoChamado.values();
	}
	
}
