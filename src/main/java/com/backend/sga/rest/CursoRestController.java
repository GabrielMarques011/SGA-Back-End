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

import com.backend.sga.model.Curso;
import com.backend.sga.model.Erro;
import com.backend.sga.model.Sucesso;
import com.backend.sga.repository.CursoRepository;

//CrossOrigin serve para que o projeto receba JSON
@CrossOrigin
@RestController
@RequestMapping("/api/curso")
public class CursoRestController {

	@Autowired
	private CursoRepository cursoRepository;
	
		//metodo para criar o curso
		@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Object> criarCurso (@RequestBody Curso curso, HttpServletRequest request){
				if(curso != null) {
					cursoRepository.save(curso);
					Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
					return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
				}else {
					Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar o curso", null);
					return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
		
		//metodo para excluir por 'ID'
		@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
		public ResponseEntity<Object> excluirCurso (@PathVariable("id") Long id, Curso curso,HttpServletRequest request){// Verificando se o id do 'ambiente' é igual ao do passado
			if(curso.getId() == id) {
				cursoRepository.delete(curso);
				Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
				return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
			}else {
				Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel excluir um curso", null);
				return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		//Buscando todos os dados no banco
		@RequestMapping(value = "", method = RequestMethod.GET)
		public Iterable<Curso> listaCurso (Curso curso){
			return cursoRepository.findAll();
		}
	
}
