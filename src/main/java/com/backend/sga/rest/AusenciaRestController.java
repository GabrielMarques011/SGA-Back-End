package com.backend.sga.rest;

import java.time.LocalTime;
import java.util.Calendar;
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

import com.backend.sga.model.Ausencia;
import com.backend.sga.model.Erro;
import com.backend.sga.model.Periodo;
import com.backend.sga.model.Professor;
import com.backend.sga.model.RecebeAula;
import com.backend.sga.model.Sucesso;
import com.backend.sga.model.TipoAusencia;
import com.backend.sga.repository.AulaRepository;
import com.backend.sga.repository.AusenciaRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/ausencia")
public class AusenciaRestController {

	@Autowired
	private AusenciaRepository ausenciaRepository;

	@Autowired
	private AulaRepository aulaRepository;

	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarAusencia(@RequestBody Ausencia ausencia, HttpServletRequest request) {
		if (ausencia != null) {
			ausenciaRepository.save(ausencia);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");

			Object[] filtro = new Object[2];
			filtro[0] = sucesso;
			filtro[1] = ausencia.getId();

			ResponseEntity<Object> okpost = new ResponseEntity<Object>(filtro, HttpStatus.OK);
			return okpost;
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível cadstrar a ausência", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deletarAusencia(@PathVariable("id") Long id, Ausencia ausencia,
			HttpServletRequest request) {
		if (ausencia.getId() == id) {
			ausenciaRepository.deleteById(id);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível deletar a ausência", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Ausencia> listarAusencias() {
		return ausenciaRepository.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> atualizarAusencia(@PathVariable("id") Long id, @RequestBody Ausencia ausencia,
			HttpServletRequest request) {
		if (ausencia.getId() != id) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID invalido!", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			ausenciaRepository.save(ausencia);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		}
	}

	// retorna apenas o prof, ausencia e se esta em aula
	@RequestMapping(value = "/professor/{id}", method = RequestMethod.GET)
	public Object[] buscarAusenciaProfessor(@PathVariable("id") Long id) {
		List<Ausencia> ausencias = ausenciaRepository.findByProfessorId(id);
		Professor professor = ausencias.get(0).getProfessor();
		int hora = LocalTime.now().getHour();
		Calendar data = Calendar.getInstance();
		Periodo periodo = null;
		boolean emAula;

		if (hora < 12) {
			periodo = Periodo.MANHA;
		} else if (hora > 12 && hora < 18) {
			periodo = Periodo.TARDE;
		} else if (hora >= 18) {
			periodo = Periodo.NOITE;
		}

		if (aulaRepository.buscaProf(professor, data, periodo).isEmpty()) {
			emAula = false;
		} else {
			emAula = true;
		}

		Object result[] = new Object[3];
		result[0] = professor;
		result[1] = ausencias;
		result[2] = emAula;

		return result;
	}
	
	
	@RequestMapping(value = "/ferias", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> ferias(@RequestBody RecebeAula recebe){
		
		try {
			for (int i = 0; i < recebe.getProfList().size(); i++) {
				
				Ausencia ausencia = new Ausencia();
				ausencia.setProfessor(recebe.getProfList().get(i));
				ausencia.setDataInicio(recebe.getDataInicio());
				ausencia.setDataFinal(recebe.getDataFinal());
				ausencia.setTipo(TipoAusencia.FERIAS);
				
				ausenciaRepository.save(ausencia);
			}
			
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
			
		} catch (Exception e) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID invalido!", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
