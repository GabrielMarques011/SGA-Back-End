package com.backend.sga.rest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.asn1.dvcs.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.backend.sga.model.Aula;
import com.backend.sga.model.DiaNaoLetivo;
import com.backend.sga.model.Erro;
import com.backend.sga.model.FeriadosNacionais;
import com.backend.sga.model.RecebeAula;
import com.backend.sga.model.Sucesso;
import com.backend.sga.repository.AulaRepository;
import com.backend.sga.repository.DiaNaoLetivoRepository;
import com.backend.sga.repository.FeriadosNacionaisRepository;
import com.backend.sga.repository.ProfessorRepository;

//CrossOrigin serve para que o projeto receba JSON
@CrossOrigin
@RestController
@RequestMapping("/api/aula")
public class AulaRestController {

	@Autowired
	private AulaRepository aulaRepository;

	@Autowired
	private ProfessorRepository professorRepository;
	
	@Autowired
	private DiaNaoLetivoRepository diaNaorepository;
	
	@Autowired
	private FeriadosNacionaisRepository feriadosRepository;

	// AJEITAR O METODO DE SALVAR AS AULAS
	// método para cadastrar uma aula
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarAula(@RequestBody RecebeAula recebeAula, HttpServletRequest request) {

		boolean dia[] = recebeAula.getDiaSemana();

		Calendar dataInicio = recebeAula.getDataInicio();

		System.out.println(dataInicio.get(Calendar.DAY_OF_WEEK));
		
		double cargaHoraria = recebeAula.getUnidadeCurricular().getHoras();

		// retornando uma listagem de aula
		List<Aula> listaAula = aulaRepository.diaSemanal(recebeAula.getDataInicio());

		// recebeAula.verificarDiasSemana(dia);
		/*System.out.println(recebeAula.verificarDiasSemana(dia));
		 * 
		
		System.out.println(dataInicio);*/
		
		
		
		

		// verificando se é vazio
		if (listaAula.isEmpty()) {
			
			List<Integer> diasVerifica = recebeAula.verificarDiasSemana(dia);
			// fazendo a repetição das horas até chegar a 0
			while (cargaHoraria > 0) {
				
						//criando variavel para que sete os valores da dataInicio
						//!necessario (TimeZone.getTimeZone("GMT-00:00"))
						Calendar data = Calendar.getInstance(TimeZone.getTimeZone("GMT-00:00"));
						data.setTime(dataInicio.getTime());
						
						//! necessario
						data.add(Calendar.DAY_OF_MONTH, 1);
						
						int diaSemana = data.get(Calendar.DAY_OF_WEEK);	
						
						
						if(dia[diaSemana - 1] == true) {
							
							System.out.println(diaNaorepository.buscaDNL(data));
							String dataStr;
							int mes;
							mes = data.get(Calendar.MONTH) + 1 ;
							
							//formatado a variável Calendar para String
							 if(data.get(Calendar.MONTH+1) < 10 && data.get(Calendar.DAY_OF_MONTH) < 10) {
								 dataStr = data.get(Calendar.YEAR) + "-0" + mes + "-0" + data.get(Calendar.DAY_OF_MONTH);
							 }else if(data.get(Calendar.DAY_OF_MONTH) < 10){
								 dataStr = data.get(Calendar.YEAR) + "-" + mes + "-0" + data.get(Calendar.DAY_OF_MONTH);
							 }else if(data.get(Calendar.MONTH+1) < 10){
								 dataStr = data.get(Calendar.YEAR) + "-0" + mes + "-" + data.get(Calendar.DAY_OF_MONTH);
							 }else {
								 dataStr = data.get(Calendar.YEAR) + "-" + mes + "-" + data.get(Calendar.DAY_OF_MONTH);
							 }
							
							if(feriadosRepository.buscaData(dataStr).isEmpty()){
								
								if (diaNaorepository.buscaDNL(data).isEmpty()) {
								
									// criando a aula(trazendo ela)
									Aula aula = new Aula();
									
									// setando os valores que precisam no cadastro de aula
									aula.setUnidadeCurricular(recebeAula.getUnidadeCurricular());
									aula.setCodTurma(recebeAula.getCodTurma());
									aula.setPeriodo(recebeAula.getPeriodo());
									aula.setAmbiente(recebeAula.getAmbiente());
									aula.setProfessor(recebeAula.getProfessor());
									aula.setCargaDiaria(recebeAula.getCargaDiaria());
									aula.setData(data);
									
									aulaRepository.save(aula);
									
									// Subtraindo a carga horaria depois que o cadastro acontece
									cargaHoraria = cargaHoraria - aula.getCargaDiaria();
									
								}
								
							}
							
						}
						
						
						// Pulando de 1 dia em 1 dia...
						dataInicio.add(Calendar.DAY_OF_MONTH, 1);
					}
			
			
				}
		
				return null;
	}

	// método que deleta a aula pelo ID
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deletarAula(@RequestBody Aula aula, @PathVariable("id") Long id,
			HttpServletRequest request) {
		if (aula.getId() == id) { // verifica se o id da aula é igual o id selecionado
			aulaRepository.deleteById(id); // deleta a aula do banco de dados
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso"); // moldando a mensagem de sucesso
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK); // retornando a mensagem de sucesso
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível deletar a aula", null); // moldando
																												// a
																												// mensagem
																												// de
																												// erro
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR); // retornando a mensagem de erro
		}
	}

	// método que lista todas as aulas
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Aula> listarAulas() {
		return aulaRepository.findAll(); // retorna a lista de todos os comonentes do banco de dados
	}

	// metodo para alterar
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> atualizarAula(@PathVariable("id") Long id, @RequestBody Aula aula,
			HttpServletRequest request) {
		if (aula.getId() != id) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			aulaRepository.save(aula);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		}
	}

}
