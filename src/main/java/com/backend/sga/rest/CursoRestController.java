package com.backend.sga.rest;

import java.util.Iterator;
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

import com.backend.sga.model.Curso;
import com.backend.sga.model.Erro;
import com.backend.sga.model.Sucesso;
import com.backend.sga.model.TipoCurso;
import com.backend.sga.model.UnidadeCurricular;
import com.backend.sga.repository.CursoRepository;
import com.backend.sga.repository.UnidadeCurricularRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/curso")
public class CursoRestController {

	@Autowired
	private CursoRepository cursoRepository;

	@Autowired
	private UnidadeCurricularRepository curricularRepository;

	// metodo para criar o curso
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarCurso(@RequestBody Curso curso, HttpServletRequest request, Long id) {
		if (curso != null) {
			curso.setAtivo(true);
			
			for (int i = 0; i < curso.getUnidadeCurricular().size(); i++) {
				UnidadeCurricular unidade = new UnidadeCurricular();
				unidade.setNome(curso.getUnidadeCurricular().get(i).getNome());
				unidade.setHoras(curso.getUnidadeCurricular().get(i).getHoras());
				curricularRepository.save(unidade);
				curso.getUnidadeCurricular().get(i).setId(unidade.getId());
			}
			
			cursoRepository.save(curso);
			
			for (int i = 0; i < curso.getUnidadeCurricular().size(); i++) {
				UnidadeCurricular uc = curso.getUnidadeCurricular().get(i);
				uc.setCurso(curso);
				curricularRepository.save(uc);
			}
			
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");

			Object[] filtro = new Object[2];
			filtro[0] = sucesso;
			filtro[1] = curso.getId(); 

			ResponseEntity<Object> okpost = new ResponseEntity<Object>(filtro, HttpStatus.OK);
			return okpost;
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar o curso", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/inativar/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> desativarCurso(@PathVariable("id") Long id, HttpServletRequest request) {
		Optional<Curso> inativar = cursoRepository.findById(id);
		if (inativar.get().getId() == id) {
			inativar.get().setAtivo(false);
			cursoRepository.save(inativar.get());
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar um curso", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Curso> listaCurso(Curso curso) {
		return cursoRepository.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> atualizarCurso(@PathVariable("id") Long id, @RequestBody Curso curso,
			HttpServletRequest request) {
		if (curso.getId() != id) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			
			for(int i = 0; i < curso.getUnidadeCurricular().size(); i++) {
				UnidadeCurricular unidade = new UnidadeCurricular();
				if (curso.getUnidadeCurricular().get(i).getId() != null) {
					unidade.setId(curso.getUnidadeCurricular().get(i).getId());
				}
				unidade.setNome(curso.getUnidadeCurricular().get(i).getNome());
				unidade.setHoras(curso.getUnidadeCurricular().get(i).getHoras());
				curricularRepository.save(unidade);
				curso.getUnidadeCurricular().get(i).setId(unidade.getId());
			}
			
			cursoRepository.save(curso);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		}
	}

	// Feito o metodo para retornar a ENUM para o Front (Kalebe pediu)
	@RequestMapping(value = "/tipocurso", method = RequestMethod.GET)
	public TipoCurso[] busca() {
		// setando todos os valores relacionados a enum
		return TipoCurso.values();
	}

	// Feito o metodo para retornar quais tipos de Cursos forem aplicados (Kalebe pediu)
	@RequestMapping(value = "/buscacurso/{tipo_curso}", method = RequestMethod.GET)
	public Iterable<Curso> buscaTipoCurso(@PathVariable("tipo_curso") TipoCurso tCurso) {
		// Fazendo uma Query para que o Front selcione um tipo de curso e traga so
		// valores relacionas a ele
		return cursoRepository.buscaTipoCurso(tCurso);
	}

	// Feito no intuito para retornar valores escritos na busca (Kalebe pediu)
	@RequestMapping(value = "/buscapalavra/{nome}", method = RequestMethod.GET)
	public Iterable<Curso> buscaPalavrasChaves(@PathVariable("nome") String nome) {
		// Fiz um like para que com qualquer palavra que ele digitar aparece algo
		// relacionado aquilo
		return cursoRepository.palavraChave(nome);
	}
	
	
	//metodo para Retornar o uma lista de Curso conforme uma Unidade Curricular (Pedido Kalebe e Matheus)
	@RequestMapping(value = "/buscaUn/{nome}", method = RequestMethod.GET)
	public Iterable<Curso> buscaUnidade (@PathVariable("nome") String nome){
		return cursoRepository.buscaUnidade(nome);
	}

}
