package com.backend.sga.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.backend.sga.model.DiaNaoLetivo;
import com.backend.sga.model.Erro;
import com.backend.sga.model.FeriadosNacionais;
import com.backend.sga.model.Sucesso;
import com.backend.sga.repository.DiaNaoLetivoRepository;
import com.backend.sga.repository.FeriadosNacionaisRepository;
import com.backend.sga.service.FeriadosNacionaisService;

@CrossOrigin
@RestController
@RequestMapping("/api/dnl")
public class DiaNaoLetivoRestController {

	@Autowired
	private DiaNaoLetivoRepository diaNaoLetivoRepository;
	
	@Autowired
	private FeriadosNacionaisRepository feriadosRepository;

	@Autowired
	private FeriadosNacionaisService service;

	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarDnl(@RequestBody DiaNaoLetivo dnl, HttpServletRequest request) {
		if (dnl != null) {
			diaNaoLetivoRepository.save(dnl);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");

			Object[] filtro = new Object[2];
			filtro[0] = sucesso;
			filtro[1] = dnl.getId();

			service.salvarFeriados();
			ResponseEntity<Object> okpost = new ResponseEntity<Object>(filtro, HttpStatus.OK);
			return okpost;
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar um Dia não letivo",null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> excluirDnl(@PathVariable("id") Long id, DiaNaoLetivo dnl,
			HttpServletRequest request) {
		if (dnl.getId() == id) {
			diaNaoLetivoRepository.delete(dnl);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel excluir o Dia Não Letivo", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<DiaNaoLetivo> listaDnl(DiaNaoLetivo dnl) {
		return diaNaoLetivoRepository.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> atualizarDnl(@PathVariable("id") Long id, @RequestBody DiaNaoLetivo dnl,
			HttpServletRequest request) {
		if (dnl.getId() != id) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			diaNaoLetivoRepository.save(dnl);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/buscaDnls", method = RequestMethod.GET)
	public ArrayList<String> retornaDatas() {
		int ano = LocalDate.now().getYear();
		String dataInicioStr = ano + "-01-01";
		String datafinalStr = ano + "-12-31";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar dataInicio = Calendar.getInstance();
		try {
			dataInicio.setTime(sdf.parse(dataInicioStr));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Calendar dataFinal = Calendar.getInstance();
		try {
			dataFinal.setTime(sdf.parse(datafinalStr));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<String> datas = new ArrayList<String>();
		
		List<DiaNaoLetivo> dnls = diaNaoLetivoRepository.buscaAno(dataInicio, dataFinal);
		List<FeriadosNacionais> feriados = feriadosRepository.buscaAno(dataInicioStr, datafinalStr);
		
		for(int i = 0; i < dnls.size(); i++) {
			String dataStr;
			
			String mes = dnls.get(i).getData().get(Calendar.MONTH) + 1 + "";
			String anoStr = ano + "";
			
			System.out.println(dnls.get(i).getData().get(Calendar.MONTH) + 1);
			
			if ((dnls.get(i).getData().get(Calendar.MONTH) + 1) < 10 && dnls.get(i).getData().get(Calendar.DAY_OF_MONTH) < 10) {
				dataStr = anoStr + "-0" + mes + "-0" + dnls.get(i).getData().get(Calendar.DAY_OF_MONTH);
			} else if (dnls.get(i).getData().get(Calendar.DAY_OF_MONTH) < 10) {
				dataStr = anoStr + "-" + mes + "-0" + dnls.get(i).getData().get(Calendar.DAY_OF_MONTH);
			} else if ((dnls.get(i).getData().get(Calendar.MONTH) + 1) < 10) {
				dataStr = anoStr + "-0" + mes + "-" + dnls.get(i).getData().get(Calendar.DAY_OF_MONTH);
			} else {
				dataStr = anoStr + "-" + mes + "-" + dnls.get(i).getData().get(Calendar.DAY_OF_MONTH);
			}
			
			datas.add(dataStr);
		}
		
		for(int i = 0; i < feriados.size(); i++) {
			datas.add(feriados.get(i).getDate());
		}
		
		
		return datas;
	}

}
