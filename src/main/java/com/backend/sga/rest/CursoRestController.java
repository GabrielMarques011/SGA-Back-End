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
import com.backend.sga.model.TipoCurso;
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
		public ResponseEntity<Object> criarCurso (@RequestBody Curso curso, HttpServletRequest request, Long id){
				if(curso != null) {
					curso.setAtivo(true); // setando o ativo como true (padrão)
					cursoRepository.save(curso); // salvando o curso
					
					//trazendo a classe 'sucesso e aplicando a mensagem criada
					Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
					
					//criando um vetor para que armazene dois dados para retornar no ResponseEntity
					Object[] filtro = new Object[2];
					filtro[0] = sucesso;
					filtro[1] = curso.getId();
					
					//setando o o filtro junto com o 'Status OK'
					ResponseEntity<Object> okpost = new ResponseEntity<Object>(filtro, HttpStatus.OK);
					return okpost;
				}else {
					Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar o curso", null);
					return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
		
		//metodo para excluir por 'ID'
		@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
		public ResponseEntity<Object> desativarCurso (@PathVariable("id") Long id, Curso curso,HttpServletRequest request){// Verificando se o id do 'ambiente' é igual ao do passado
			if(curso.getId() == id) {
				curso.setAtivo(false); // setando o ativo como false, para estar desativado
				cursoRepository.save(curso); // salvando o curso
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
		
		//metodo para alterar
		@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Object> atualizarCurso(@PathVariable("id") Long id, @RequestBody Curso curso, HttpServletRequest request){
			if (curso.getId() != id) {
				Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
				return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
			}else {
				cursoRepository.save(curso);
				Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
				return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
			}
		}
		
		//Feito o metodo para retornar a ENUM para o Front
		@RequestMapping(value = "/tipocurso", method = RequestMethod.GET)
		public TipoCurso[] busca (){
			return TipoCurso.values();
		}
				
				
		//Feito o metodo para retornar quais tipos de Cursos forem aplicados
		@RequestMapping(value = "/buscacurso/{tipo_curso}", method = RequestMethod.GET)
		public Iterable<Curso> buscaTipoCurso(@PathVariable("tipo_curso") TipoCurso tCurso){
			//Fazendo uma Query
			return cursoRepository.buscaTipoCurso(tCurso);
		}
	
}
