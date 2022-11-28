package com.backend.sga.rest;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
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
import org.springframework.web.bind.annotation.RequestParam;
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

	@RequestMapping(value = "/ferias", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> ferias(@RequestBody RecebeAula recebe) {
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

	@RequestMapping(value = "/listProf/{id}", method = RequestMethod.GET)
	public ArrayList<String> filtroFeriasProf(@PathVariable("id") Long id) {
		ArrayList<String> datas = new ArrayList<String>();
		List<Ausencia> listAus = ausenciaRepository.listaAusenciaDeProf(id);
		for (int i = 0; i < listAus.size(); i++) {
			String dataFormInicio;
			String dataFormFinal;
			int mesIni;
			mesIni = listAus.get(i).getDataInicio().get(Calendar.MONTH) + 1;
			int mesFin;
			mesFin = listAus.get(i).getDataFinal().get(Calendar.MONTH) + 1;
			// formatado a variável Calendar para String (dataInicio)
			if ((listAus.get(i).getDataInicio().get(Calendar.MONTH) + 1) < 10
					&& listAus.get(i).getDataInicio().get(Calendar.DAY_OF_MONTH) < 10) {
				dataFormInicio = listAus.get(i).getDataInicio().get(Calendar.YEAR) + "-0" + mesIni + "-0"
						+ listAus.get(i).getDataInicio().get(Calendar.DAY_OF_MONTH);
			} else if (listAus.get(i).getDataInicio().get(Calendar.DAY_OF_MONTH) < 10) {
				dataFormInicio = listAus.get(i).getDataInicio().get(Calendar.YEAR) + "-" + mesIni + "-0"
						+ listAus.get(i).getDataInicio().get(Calendar.DAY_OF_MONTH);
			} else if ((listAus.get(i).getDataInicio().get(Calendar.MONTH) + 1) < 10) {
				dataFormInicio = listAus.get(i).getDataInicio().get(Calendar.YEAR) + "-0" + mesIni + "-"
						+ listAus.get(i).getDataInicio().get(Calendar.DAY_OF_MONTH);
			} else {
				dataFormInicio = listAus.get(i).getDataInicio().get(Calendar.YEAR) + "-" + mesIni + "-"
						+ listAus.get(i).getDataInicio().get(Calendar.DAY_OF_MONTH);
			}
			// formatado a variável Calendar para String (dataFinal)
			if ((listAus.get(i).getDataFinal().get(Calendar.MONTH) + 1) < 10
					&& listAus.get(i).getDataFinal().get(Calendar.DAY_OF_MONTH) < 10) {
				dataFormFinal = listAus.get(i).getDataFinal().get(Calendar.YEAR) + "-0" + mesFin + "-0"
						+ listAus.get(i).getDataFinal().get(Calendar.DAY_OF_MONTH);
			} else if (listAus.get(i).getDataFinal().get(Calendar.DAY_OF_MONTH) < 10) {
				dataFormFinal = listAus.get(i).getDataFinal().get(Calendar.YEAR) + "-" + mesFin + "-0"
						+ listAus.get(i).getDataFinal().get(Calendar.DAY_OF_MONTH);
			} else if ((listAus.get(i).getDataFinal().get(Calendar.MONTH) + 1) < 10) {
				dataFormFinal = listAus.get(i).getDataFinal().get(Calendar.YEAR) + "-0" + mesFin + "-"
						+ listAus.get(i).getDataFinal().get(Calendar.DAY_OF_MONTH);
			} else {
				dataFormFinal = listAus.get(i).getDataFinal().get(Calendar.YEAR) + "-" + mesFin + "-"
						+ listAus.get(i).getDataFinal().get(Calendar.DAY_OF_MONTH);
			}
			datas.add(dataFormInicio);
			datas.add(dataFormFinal);
		}
		return datas;
	}
}