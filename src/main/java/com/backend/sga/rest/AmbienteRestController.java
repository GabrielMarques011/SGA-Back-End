package com.backend.sga.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.backend.sga.model.Ambiente;
import com.backend.sga.model.Aula;
import com.backend.sga.model.Erro;
import com.backend.sga.model.Periodo;
import com.backend.sga.model.Sucesso;
import com.backend.sga.repository.AmbienteRepository;
import com.backend.sga.repository.AulaRepository;

//CrossOrigin serve para que o projeto receba JSON
@CrossOrigin
@RestController
@RequestMapping("/api/ambiente")
public class AmbienteRestController {

	@Autowired
	private AmbienteRepository ambienteRepository;
	@Autowired
	private AulaRepository aulaRepository;
	
	//metodo para criar o ambiente
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarAmbiente (@RequestBody Ambiente ambiente, HttpServletRequest request){
			if(ambiente != null) {
				ambiente.setAtivo(true); // setando o ativo como true (padrão)
				ambienteRepository.save(ambiente); // salvando o ambiente no banco de dados
				Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
				
				//criando um vetor para que armazene dois dados para retornar no ResponseEntity
				Object[] filtro = new Object[2];
				filtro[0] = sucesso;
				filtro[1] = ambiente.getId();
				
				//setando o o filtro junto com o 'Status OK'
				ResponseEntity<Object> okpost = new ResponseEntity<Object>(filtro, HttpStatus.OK);
				
				return okpost;
			}else {
				Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar um ambiente", null);
				return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
	
	//metodo para excluir por 'ID'
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> desativarAmbiente (@PathVariable("id") Long id, Ambiente ambiente,HttpServletRequest request){// Verificando se o id do 'ambiente' é igual ao do passado
		if(ambiente.getId() == id) {
			ambiente.setAtivo(false); // setando o Ativo como false, para estar desativado
			ambienteRepository.save(ambiente); // salvando o ambiente
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		}else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar um ambiente", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Buscando todos os dados no banco
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Ambiente> listaAmbiente (Ambiente ambiente){
		return ambienteRepository.findAll();
	}
	
	//metodo para alterar
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> atualizarAmbiente(@PathVariable("id") Long id, @RequestBody Ambiente ambiente, HttpServletRequest request){
		if (ambiente.getId() != id) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}else {
			ambienteRepository.save(ambiente);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		}
	}
	
	// método que busca os ambientes disponíveis
	@RequestMapping(value = "/disponiveis", method = RequestMethod.GET)
	public ArrayList<Long> buscarAmbientesDisponiveis(@RequestParam("unidade_id") Long id, @RequestParam("data") String data, @RequestParam("periodo") Periodo periodo){
		List<Aula> aulas = aulaRepository.findByAmbientesId(id); // buscando as listas de aulas de acordo com a unidade curricular
		
		
		/***
		 * 
		 * select ambientes from aulas where dataInicial > data and periodo = periodo
		 * se trazer significa que ta em aula
		 * 
		 * findAllAmbientes []
		 * 
		 * 
		 * retorna array [99999]
		 * 
		 * 
		 */
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // formatador de data
		
		Calendar data1 = Calendar.getInstance(); // variável para guardar a data_inicio 
		try {
			data1.setTime(sdf.parse(data)); // tranformando a String em calendar
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		ArrayList<Long> ambiDisponiveis = new ArrayList<Long>(); // lista que vai armazenar os ids dos ambientes que estão disponíveis
		
		for(int i = 0; i < aulas.size(); i++) { // for para passar pela lista que veio do bnco de dados
			
			if(aulas.get(i).getData().compareTo(data1) != 0) {
				if(aulas.get(i).getPeriodo() != periodo) {
					ambiDisponiveis.add(aulas.get(i).getId()); // adicionando o id do ambiente no arrayList
				}
			}
		}
		
		return ambiDisponiveis; // retornando a lista de ambientes disponíveis 
	}
	
}
