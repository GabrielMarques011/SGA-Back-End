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

import com.backend.sga.model.Erro;
import com.backend.sga.model.Sucesso;
import com.backend.sga.model.TipoAmbiente;
import com.backend.sga.model.UnidadeCurricular;
import com.backend.sga.repository.UnidadeCurricularRepository;

//CrossOrigin serve para que o projeto receba JSON
@CrossOrigin
@RestController
@RequestMapping("/api/unidade")
public class UnidadeCurricularRestController {

	@Autowired
	private UnidadeCurricularRepository curricularRepository;
	
	//metodo para criar o professor
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarUnidade (@RequestBody UnidadeCurricular curricular, HttpServletRequest request){
		if(curricular != null) {
			curricularRepository.save(curricular);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			
			//criando um vetor para que armazene dois dados para retornar no ResponseEntity
			Object[] filtro = new Object[2];
			filtro[0] = sucesso;
			filtro[1] = curricular.getId();
			
			//setando o o filtro junto com o 'Status OK'
			ResponseEntity<Object> okpost = new ResponseEntity<Object>(filtro, HttpStatus.OK);
			
			return okpost;
		}else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar uma Unidade Curricular", null);							
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//metodo para excluir por 'ID'
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> excluirUnidade (@PathVariable("id") Long id, UnidadeCurricular curricular,HttpServletRequest request){// Verificando se o id do 'curricular' é igual ao do passado
		if(curricular.getId() == id) {
			curricularRepository.delete(curricular);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		}else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel excluir a unidade curricular", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Buscando todos os dados no banco
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<UnidadeCurricular> listaDnl (UnidadeCurricular curricular){
		return curricularRepository.findAll();
	}
	
	//metodo para alterar
		@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Object> atualizarUnidadetand(@PathVariable("id") Long id, @RequestBody UnidadeCurricular curricular, HttpServletRequest request){
			if (curricular.getId() != id) {
				Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "NIF inválido", null);
				return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
			}else {
				curricularRepository.save(curricular);
				Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
				return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
			}
		}
		
		//Feito o metodo para retornar a ENUM para o Front
		@RequestMapping(value = "/tipoambiente", method = RequestMethod.GET)
		public TipoAmbiente[] buscaAmbiente (){
			return TipoAmbiente.values();
		}
	
}
