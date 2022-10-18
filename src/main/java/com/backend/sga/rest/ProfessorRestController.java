package com.backend.sga.rest;

import java.util.List;

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
import com.backend.sga.model.Professor;
import com.backend.sga.model.RecebeProfComp;
import com.backend.sga.model.Sucesso;
import com.backend.sga.repository.ProfessorRepository;

//CrossOrigin serve para que o projeto receba JSON
@CrossOrigin
@RestController
@RequestMapping("/api/professor")
public class ProfessorRestController {

	@Autowired
	private ProfessorRepository professorRepository;
	
	
	//metodo para criar o professor
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarProf (@RequestBody Professor prof){
		if(prof != null) {
			//setando o professor como ativo no banco de dados
			//prof.setAtivo(true);
			//salvando professor no banco de dados
			professorRepository.save(prof);
			Competencia c = new Competencia();
			prof.getCompetencia().forEach((Competencia t)->{
				System.out.println(t);
				c.setUnidadeCurricular(t.getUnidadeCurricular());
				c.setNivel(t.getNivel());
				c.setProfessor(prof);
				
				//int i = (int) t.getId();
				//prof.getCompetencia().set(i, c);
			});
			
			professorRepository.save(prof);
			
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			
			//criando um vetor para que armazene dois dados para retornar no ResponseEntity
			Object[] filtro = new Object[2];
			filtro[0] = sucesso;
			filtro[1] = prof.getId();
			
			//setando o o filtro junto com o 'Status OK'
			ResponseEntity<Object> okpost = new ResponseEntity<Object>(filtro, HttpStatus.OK);
			
			System.out.println(prof);
			return okpost;
		}else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar o Professor", null);							
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/desativar/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> desativarProf(@PathVariable("id") Long id, Professor prof, HttpServletRequest request){

		//buscando ele pelo id para alterar
		prof = professorRepository.findById(id).get();
		//setando ela como inativo
		prof.setAtivo(false);
		//salvando no banco como inativo
		professorRepository.save(prof);
			
		Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
		return new ResponseEntity<Object>(sucesso, HttpStatus.OK);	
	}
	
	//Buscando todos os dados no banco
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Professor> listaDnl (){
		return professorRepository.findAll();
	}
	
	//metodo para alterar
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> atualizarProf(@PathVariable("id") Long id, @RequestBody Professor prof, HttpServletRequest request){
		if (prof.getId() != id) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}else {
			professorRepository.save(prof);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		}
	}
	
	//metodo para trazer os professores digitados (Pedido Kalebe)
	@RequestMapping(value = "/buscapalavra/{nome}", method = RequestMethod.GET)
	public Iterable<Professor> buscaPalavraChave (@PathVariable("nome") String nome){
		//retorna o like da query que eles irão escrever
		return professorRepository.palavraChave(nome);
	}
	
}
