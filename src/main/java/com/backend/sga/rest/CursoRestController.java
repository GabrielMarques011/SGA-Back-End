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
	@Administrador
	@User
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

	@Administrador
	@User
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

	@Administrador
	@User
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Curso> listaCurso(Curso curso) {
		return cursoRepository.findAllOrdeyBy();
	}

	@Administrador
	@User
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> atualizarCurso(@PathVariable("id") Long id, @RequestBody Curso curso,
			HttpServletRequest request) {
		if (curso.getId() != id) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			Optional<Curso> cid = cursoRepository.findById(id);
			// vendo se o id do curso ou unidade é vazia
			if (cid.isEmpty()) {
				Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
				return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				// fazendo o for para percorrer as unidades
				for (int i = 0; i < curso.getUnidadeCurricular().size(); i++) {
					UnidadeCurricular unidade = new UnidadeCurricular();
					if (curso.getUnidadeCurricular().get(i).getId() != null) {
						unidade.setId(curso.getUnidadeCurricular().get(i).getId());
					}
					unidade.setNome(curso.getUnidadeCurricular().get(i).getNome());
					unidade.setHoras(curso.getUnidadeCurricular().get(i).getHoras());
					// setando o id do curso no unidade
					unidade.setCurso(cid.get());
					curricularRepository.save(unidade);
					curso.getUnidadeCurricular().get(i).setId(unidade.getId());
				}
				cursoRepository.save(curso);
				Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
				return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
			}
		}
	}

	@Administrador
	@User
	@RequestMapping(value = "/tipocurso", method = RequestMethod.GET)
	public TipoCurso[] busca() {
		return TipoCurso.values();
	}

	@Administrador
	@User
	@RequestMapping(value = "/buscacurso/{tipo}", method = RequestMethod.GET)
	public Iterable<Curso> buscaTipoCurso(@PathVariable("tipo") TipoCurso tipo) {
		return cursoRepository.buscaTipoCurso(tipo);
	}

	@Administrador
	@User
	@RequestMapping(value = "/buscapalavra/{nome}", method = RequestMethod.GET)
	public Iterable<Curso> buscaPalavrasChaves(@PathVariable("nome") String nome) {
		return cursoRepository.palavraChave(nome);
	}
	
	@Administrador
	@User
	@RequestMapping(value = "/buscaCr/{nome}", method = RequestMethod.GET)
	public Iterable<Curso> buscaCurso(@PathVariable("nome") String nome) {
		return cursoRepository.buscaCurso(nome);
	}
	
	@Administrador
	@User
	@RequestMapping(value = "/alterarStatus/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> alterarStatusCurso(@PathVariable("id") Long id, HttpServletRequest request) {
        Optional<Curso> status = cursoRepository.findById(id);
        if (status.get().getId() == id) {
            if (status.get().isAtivo()) {
                status.get().setAtivo(false);
            } else {
                status.get().setAtivo(true);
            }
            cursoRepository.save(status.get());
            Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
            return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
        } else {
            Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel alterar o status do curso", null);
            return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}