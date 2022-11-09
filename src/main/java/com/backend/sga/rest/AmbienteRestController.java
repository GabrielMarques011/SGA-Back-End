package com.backend.sga.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

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
import com.backend.sga.model.RecebeBuscaAmbiente;
import com.backend.sga.model.Sucesso;
import com.backend.sga.model.TipoAmbiente;
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

	// URL = localhost:8080/api/ambiente
	// METODO PARA CRIAR AMBIENTE
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarAmbiente(@RequestBody Ambiente ambiente, HttpServletRequest request) {
		if (ambiente != null) {
			ambiente.setAtivo(true);
			ambienteRepository.save(ambiente);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");

			Object[] filtro = new Object[2];
			filtro[0] = sucesso;
			filtro[1] = ambiente.getId();

			// setando o o filtro junto com o 'Status OK'
			ResponseEntity<Object> okpost = new ResponseEntity<Object>(filtro, HttpStatus.OK);

			return okpost;
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar um ambiente", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// URL = localhost:8080/api/ambiente/1
	// METODO PARA ALTERAR O AMBIENTE
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> atualizarAmbiente(@PathVariable("id") Long id, @RequestBody Ambiente ambiente,
			HttpServletRequest request) {
		if (ambiente.getId() != id) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			ambienteRepository.save(ambiente);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		}
	}

	// URL = localhost:8080/api/ambiente/1
	// METODO PARA DESATIVAR AMBIENTE
	@RequestMapping(value = "/inativar/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> desativarAmbiente(@PathVariable("id") Long id, HttpServletRequest request) {
		Optional<Ambiente> inativar = ambienteRepository.findById(id); // setando o Ativo como false, para estar
																		// desativado
		if (inativar.get().getId() == id) {
			inativar.get().setAtivo(false);
			ambienteRepository.save(inativar.get());
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar um ambiente", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// URL = localhost:8080/api/ambiente
	// METODO PARA TRAZER TODOS OS AMBIENTES
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Ambiente> listaAmbiente(Ambiente ambiente) {
		return ambienteRepository.findAll();
	}

	// URL =
	// localhost:8080/api/ambiente/disponiveis?unidade=1&data=09/11/2022&periodo=MANHA
	// METODO PARA BUSCAR AMBIENTE DISPONIVEL (MOBILE)
	@RequestMapping(value = "/disponiveis", method = RequestMethod.GET)
	public ArrayList<Long> buscarAmbientesDisponiveis(@RequestParam("unidade") Long id,
			@RequestParam("data") String data, @RequestParam("periodo") Periodo periodo) {
		List<Aula> aulas = aulaRepository.findByAmbientesId(id); // BUSCANDO LISTA DE AULA DE ACORDO COM UNIDADE

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // FORMATANDO DATA

		Calendar data1 = Calendar.getInstance(); // GUARDANDO A DATA INICIO
		try {
			data1.setTime(sdf.parse(data)); // TRANSFORMANDO CALENDAR EM STRING
		} catch (ParseException e) {
			e.printStackTrace();
		}

		ArrayList<Long> ambiDisponiveis = new ArrayList<Long>(); // LISTA QUE ARMAZENA OS IDs DOS AMBIENTES DISPONIVEIS

		for (int i = 0; i < aulas.size(); i++) { // FOR PARA PERCORRER A LISTA

			if (aulas.get(i).getData().compareTo(data1) != 0) {
				if (aulas.get(i).getPeriodo() != periodo) {
					ambiDisponiveis.add(aulas.get(i).getId()); // ADD ID DO AMBIENTE NO ARRAY
				}
			}
		}
		return ambiDisponiveis; // RETORNA OS DISPONIVEIS
	}

	@RequestMapping(value = "/tipoambiente", method = RequestMethod.GET)
	public TipoAmbiente[] buscaTipoAmbiente() {
		return TipoAmbiente.values();
	}

	@RequestMapping(value = "/buscapalavra/{nome}", method = RequestMethod.GET)
	public List<Ambiente> buscaChaveAmbiente(@PathVariable("nome") String nome) {
		return ambienteRepository.palavraChave(nome);
	}

	@RequestMapping(value = "/buscaambiente/{tipo}", method = RequestMethod.GET)
	public Iterable<Ambiente> buscandoAmbiente(@PathVariable("tipo") TipoAmbiente tipo) {
		return ambienteRepository.buscaAmbiente(tipo);
	}

	@RequestMapping(value = "/capacidade", method = RequestMethod.GET)
	public Iterable<Ambiente> retornaCapacidade(@PathParam("capacidadeMin") int capacidadeMin,
			@PathParam("capacidadeMax") int capacidadeMax) {
		return ambienteRepository.retornaCapacidade(capacidadeMin, capacidadeMax);
	}

	@RequestMapping(value = "/tipoecapacidade", method = RequestMethod.GET)
	public Iterable<Ambiente> retornaTipoeCapacidade(@PathParam("tipo") TipoAmbiente tipo,
			@PathParam("capacidadeMin") int capacidadeMin, @PathParam("capacidadeMax") int capacidadeMax) {
		return ambienteRepository.retornaTipoCapacidade(tipo, capacidadeMin, capacidadeMax);
	}

	// METODO PARA O MOBILE / RETORNAR OS AMBIENTES LIVRES
	@RequestMapping(value = "/disponivel", method = RequestMethod.GET)
	public List<Ambiente> retornaDisponivel(@RequestParam("dataInicio") String dataInicio,
			@RequestParam("dias") boolean dia[], @RequestParam("dataFinal") String dataFinal,
			@RequestParam("periodo") Periodo periodo) {

		ArrayList<Ambiente> ocupados = new ArrayList<Ambiente>();

		List<Ambiente> ambientes = (List<Ambiente>) ambienteRepository.findAll();

		//PASSANDO UMA DATA FORMATADA
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		//TRANSFORMANDO EM CALENDAR
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(sdf.parse(dataInicio));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar calendar2 = Calendar.getInstance();
		try {
			calendar2.setTime(sdf.parse(dataFinal));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//SETANDO OS DIAS DAS SEMANAS NO TIPO CALENDAR
		int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);

		while (calendar.before(calendar2) || calendar.equals(calendar2)) {
			if (dia[diaSemana - 1] == true) {

				List<Ambiente> ocupado = ambienteRepository.retornaOcupadosDia(dataInicio, periodo);

				if (!ocupado.isEmpty()) {
					for (int i = 0; i < ocupado.size(); i++) {
						ocupados.add(ocupado.get(i));
					}
				}
			}
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		for (int i = 0; i < ambientes.size(); i++) {
			for (int j = 0; j < ocupados.size(); j++) {
				if (ambientes.get(i) == ocupados.get(j)) {
					ambientes.remove(i);
				}
			}
		}

		return ambientes;
	}

	@RequestMapping(value = "/ocupados/{data}", method = RequestMethod.GET)
	public List<Ambiente> ambientesOcupadosData(@PathVariable("data") String dataStr) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar data = Calendar.getInstance();
		try {
			data.setTime(sdf.parse(dataStr));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ambienteRepository.ocupadosPorData(data);
	}

	@RequestMapping(value = "/disponibilidade/periodo", method = RequestMethod.POST)
	public ArrayList<Aula> disponivelDataPeriodo(@RequestBody RecebeBuscaAmbiente busca) {
		Calendar data = busca.getDataInicio();
		boolean dia[] = busca.getDiasSemana();
		int diaSemana = data.get(Calendar.DAY_OF_WEEK);

		ArrayList<Aula> aulas = new ArrayList<Aula>();

		while (data.before(busca.getDataFinal()) || data.equals(busca.getDataFinal())) {
			if (dia[diaSemana - 1] == true) {
				Optional<Aula> ocupado = aulaRepository.ocupadoPorDataPeriodo(data, busca.getPeriodo(),
						busca.getAmbiente());

				if (!ocupado.isEmpty()) {
					aulas.add(ocupado.get());
				}
			}
			data.add(Calendar.DAY_OF_MONTH, 1);
		}

		return aulas;
	}

	@RequestMapping(value = "/livre/{data}", method = RequestMethod.GET)
	public List<Ambiente> livresPorData(@PathVariable("data") String dataStr) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // formatador de data

		Calendar data = Calendar.getInstance();
		try {
			data.setTime(sdf.parse(dataStr));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println(aulaRepository.buscaData(data));

		System.out.println(data);
		List<Aula> ocupados = aulaRepository.buscaData(data);

		List<Ambiente> ambientes = (List<Ambiente>) ambienteRepository.findAll();

		System.out.println(ocupados.get(0).getAmbiente());

		for (int i = 0; i < ambientes.size(); i++) {
			for (int j = 0; j < ocupados.size(); j++) {
				if (ambientes.get(i) == ocupados.get(j).getAmbiente()) {
					ambientes.remove(i);
				}
			}
		}

		return ambientes;
	}

	@RequestMapping(value = "/orderAmb")
	public List<Ambiente> orderAmbiente() {
		return ambienteRepository.orderAmbiente();
	}

}
