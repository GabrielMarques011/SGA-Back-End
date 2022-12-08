package com.backend.sga.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.backend.sga.annotation.Administrador;
import com.backend.sga.annotation.User;
import com.backend.sga.model.Ambiente;
import com.backend.sga.model.Aula;
import com.backend.sga.model.Ausencia;
import com.backend.sga.model.Competencia;
import com.backend.sga.model.DevolveDisp;
import com.backend.sga.model.Erro;
import com.backend.sga.model.Periodo;
import com.backend.sga.model.Professor;
import com.backend.sga.model.RecebeBuscaAmbiente;
import com.backend.sga.model.Sucesso;
import com.backend.sga.repository.AulaRepository;
import com.backend.sga.repository.AusenciaRepository;
import com.backend.sga.repository.CompetenciaRepository;
import com.backend.sga.repository.ProfessorRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/professor")
public class ProfessorRestController {
	@Autowired
	private ProfessorRepository professorRepository;
	@Autowired
	private CompetenciaRepository competenciaRepository;
	@Autowired
	private AulaRepository aulaRepository;
	@Autowired
	private AusenciaRepository ausenciaRepository;

	@Administrador
	@User
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criarProf(@RequestBody Professor prof) {
		if (prof != null) {
			professorRepository.save(prof);
			List<Competencia> un = prof.getCompetencia();
			for (int i = 0; i < un.size(); i++) {
				Competencia competencia = new Competencia();
				// setando os valores da unidade para competencia
				competencia.setUnidadeCurricular(un.get(i).getUnidadeCurricular());
				// setando o nivel
				competencia.setNivel(un.get(i).getNivel());
				// trazendo os atributos dos professore
				competencia.setProfessor(prof);
				competenciaRepository.save(competencia);
			}
			professorRepository.save(prof);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			Object[] filtro = new Object[2];
			filtro[0] = sucesso;
			filtro[1] = prof.getId();
			ResponseEntity<Object> okpost = new ResponseEntity<Object>(filtro, HttpStatus.OK);
			return okpost;
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possivel cadastrar o Professor", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Administrador
	@User
	@RequestMapping(value = "/desativar/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> desativarProf(@PathVariable("id") Long id, Professor prof,
			HttpServletRequest request) {
		prof = professorRepository.findById(id).get();
		prof.setAtivo(false);
		professorRepository.save(prof);
		Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
		return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
	}

	@Administrador
	@User
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Professor> listaDnl() {
		return professorRepository.orderProf();
	}

	@Administrador
	@User
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> atualizarProf(@PathVariable("id") Long id, @RequestBody Professor prof,
			HttpServletRequest request) {
		if (prof.getId() != id) {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID inválido", null);
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			professorRepository.save(prof);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		}
	}

	// metodo para trazer os professores digitados (Pedido Kalebe)
	@Administrador
	@User
	@RequestMapping(value = "/buscapalavra/{nome}", method = RequestMethod.GET)
	public Iterable<Professor> buscaPalavraChave(@PathVariable("nome") String nome) {
		// retorna o like da query que eles irão escrever
		return professorRepository.palavraChave(nome);
	}

	// retorna apenas o prof, ambiente e se esta em aula
	@Administrador
	@User
	@RequestMapping(value = "/professorDisp/", method = RequestMethod.GET)
	public ArrayList<DevolveDisp> buscarDisponibilidadeProfessor() {
		List<Professor> listaProf = (List<Professor>) professorRepository.findAllAtivo();
		int hora = LocalTime.now().getHour();
		Calendar data = Calendar.getInstance();
		Ambiente ambiente = null;
		Periodo periodo = null;
		boolean emAula;
		
		if (hora < 12) {
			periodo = Periodo.MANHA;
		} else if (hora > 12 && hora < 18) {
			periodo = Periodo.TARDE;
		} else if (hora >= 18) {
			periodo = Periodo.NOITE;
		}
		
		ArrayList<DevolveDisp> listaDisp = new ArrayList<DevolveDisp>();
		
		for (int i = 0; i < listaProf.size(); i++) {
			
			DevolveDisp devolveDisp = new DevolveDisp();
			devolveDisp.setProfessor(listaProf.get(i));
			
			List<Aula> listaAula = aulaRepository.buscaProf(listaProf.get(i), data, periodo);
			
			if (listaAula.isEmpty()) {
				devolveDisp.setEmAula(false);
				devolveDisp.setAmbiente(null);
			} else {
				devolveDisp.setEmAula(true);
				devolveDisp.setAmbiente(listaAula.get(0).getAmbiente());
			}
			listaDisp.add(devolveDisp);
		}
		return listaDisp;
	}

	@Administrador
	@User
	@RequestMapping(value = "/disponibilidade/periodo", method = RequestMethod.GET)
	public Object disponivelDataPeriodo(@RequestParam("dataInicio") String dataStr, @RequestParam("dia") boolean dia[],
			@RequestParam("dataFinal") String dataFinalStr, @RequestParam("periodo") Periodo periodo,
			@RequestParam("prof") Long idProf) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar data = Calendar.getInstance();
		try {
			data.setTime(sdf.parse(dataStr));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar dataFinal = Calendar.getInstance();
		try {
			dataFinal.setTime(sdf.parse(dataFinalStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int diaSemana = data.get(Calendar.DAY_OF_WEEK);
		ArrayList<Aula> aulas = new ArrayList<Aula>();
		System.out.println(data);
		System.out.println(dataFinal);
		while (data.before(dataFinal) || data.equals(dataFinal)) {
			if (dia[diaSemana - 1] == true) {
				Optional<Professor> professor = professorRepository.findById(idProf);
				if (professor.isEmpty()) {
					Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ID do professor inválido", null);
					return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
				} else {
					Optional<Aula> ocupado = aulaRepository.ocupadoProfessor(data, periodo, professor.get());
					if (!ocupado.isEmpty()) {
						aulas.add(ocupado.get());
					}
				}
			}
			data.add(Calendar.DAY_OF_MONTH, 1);
		}
		return aulas;
	}

	@Administrador
	@User
	@RequestMapping(value = "/disponibilidade", method = RequestMethod.POST)
	public List<Professor> disponibilidadeProf(@RequestBody RecebeBuscaAmbiente busca) {
		
		ArrayList<Professor> ocupados = new ArrayList<Professor>();
		List<Professor> profsDb = (List<Professor>) professorRepository
				.buscaPorUnidade(busca.getUnidadeCurricular().getId());
		Calendar data = busca.getDataInicio();
		int diaSemana = data.get(Calendar.DAY_OF_WEEK);
		boolean dia[] = busca.getDiasSemana();
		Calendar dataFinal = busca.getDataFinal();
		while (data.before(dataFinal) || data.equals(dataFinal)) {
			if (dia[diaSemana - 1] == true) {
				List<Professor> ocupado = professorRepository.disponibilidade(busca.getUnidadeCurricular().getId(),
						busca.getPeriodo(), data);
				for (int i = 0; i < ocupado.size(); i++) {
					ocupados.add(ocupado.get(i));
				}
			}
			data.add(Calendar.DAY_OF_MONTH, 1);
		}
		for (int i = 0; i < profsDb.size(); i++) {
			for (int j = 0; j < ocupados.size(); j++) {
				if (profsDb.get(i) == ocupados.get(j)) {
					profsDb.remove(i);
				}
			}
		}
		return profsDb;
	}
	
	//MOBILE
	@Administrador
	@User
	@RequestMapping(value = "/disponibilidadeMobile", method = RequestMethod.POST)
	public List<Professor> disponibilidadeProfMobile(@RequestBody RecebeBuscaAmbiente busca) {
		
		ArrayList<Professor> ocupados = new ArrayList<Professor>();
		List<Professor> profsDb = (List<Professor>) professorRepository
				.buscaPorUnidade(busca.getUnidadeCurricular().getId());
		Calendar data = busca.getDtInicio();
		int diaSemana = data.get(Calendar.DAY_OF_WEEK);
		boolean dia[] = busca.getDiasSemana();
		Calendar dataFinal = busca.getDtFinal();
		while (data.before(dataFinal) || data.equals(dataFinal)) {
			if (dia[diaSemana - 1] == true) {
				List<Professor> ocupado = professorRepository.disponibilidade(busca.getUnidadeCurricular().getId(),
						busca.getPeriodo(), data);
				for (int i = 0; i < ocupado.size(); i++) {
					ocupados.add(ocupado.get(i));
				}
			}
			data.add(Calendar.DAY_OF_MONTH, 1);
		}
		for (int i = 0; i < profsDb.size(); i++) {
			for (int j = 0; j < ocupados.size(); j++) {
				if (profsDb.get(i) == ocupados.get(j)) {
					profsDb.remove(i);
				}
			}
		}
		
		return profsDb;
	}

	@Administrador
	@User
	@RequestMapping(value = "/disponibilidadeProf/periodo", method = RequestMethod.POST)
	public ArrayList<Aula> disponivelPeriodoProf(@RequestBody RecebeBuscaAmbiente busca) {
		Calendar data = busca.getDataInicio();
		boolean dia[] = busca.getDiasSemana();
		////boolean diasSemana = busca.verificarDiasSemana(dia);
		
		ArrayList<Aula> aulas = new ArrayList<Aula>();
		
		while (data.before(busca.getDataFinal()) || data.equals(busca.getDataFinal())) {
			int diaSemana = data.get(Calendar.DAY_OF_WEEK);
			if (dia[diaSemana - 1] == true) {
				
				Optional<Aula> ocupado = aulaRepository.ocupadoPorDataPeriodoProf(data, busca.getPeriodo(),busca.getProfessor());
				
				if (!ocupado.isEmpty()) {
					aulas.add(ocupado.get());
				}
			}
			data.add(Calendar.DAY_OF_MONTH, 1);
		}
		return aulas;
	}

	@Administrador
	@User
	@RequestMapping(value = "/orderProf", method = RequestMethod.GET)
	public List<Professor> orderProfessor() {
		return professorRepository.orderProf();
	}

	// URL = localhost:8080/api/professor/buscProf?nomeCurso=WordAvançado&nomeUnidade=Word
	// METODO PARA TRAZER TODOS OS PROFESSORES DE UMA UNIDADE E UM CURSO ESPECIFICO
	// (MOBILE)
	@Administrador
	@User
	@RequestMapping(value = "/buscProf", method = RequestMethod.GET)
	public List<Professor> ordernarProCrEUc(@RequestParam("nomeCurso") String nomeCurso,
			@RequestParam("nomeUnidade") String nomeUnidade) {
		return professorRepository.listProfcuc(nomeCurso, nomeUnidade);
	}

	// URL =
	// localhost:8080/api/professor/diaria?id=1&data_inicio=09/11/2022&data_final=11/11/2022
	// METODO DASHBOARD (COLUNA)
	@Administrador
	@User
	@RequestMapping(value = "/diaria", method = RequestMethod.GET)
	public double[] busca(@RequestParam("id") Long id, @RequestParam("data_inicio") String data_inicio,
			@RequestParam("data_final") String data_final, Professor prof) {
		// formatando o formato da Data
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		// transcrevendo para Calendar
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(sdf.parse(data_inicio));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar2 = Calendar.getInstance();
		try {
			calendar2.setTime(sdf.parse(data_final));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Aula> lista = aulaRepository.buscaTempo(id, calendar, calendar2);
		double diaria = 0;
		// percorrer o for para que recebemos o valor
		for (int l = 0; l < lista.size(); l++) {
			diaria += lista.get(l).getCargaDiaria();
			System.out.println(diaria);
		}
		Optional<Professor> profe = professorRepository.buscaProfAtivoId(true, id);
		double[] horas = new double[2];
		horas[0] = diaria;
		horas[1] = profe.get().getCargaSemanal();
		return horas;
	}

	// URL = localhost:8080/api/professor/emAula
	// METODO PARA VER SE O PROFESSOR ESTÁ EM AULA OU NÃO
	@Administrador
	@User
	@RequestMapping(value = "/emAula", method = RequestMethod.GET)
	public ArrayList<Object> retornaEmAula() {
		List<Professor> prof = (List<Professor>) professorRepository.buscaProfAtivo(true);
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
		ArrayList<Object> valor = new ArrayList<Object>();
		//Object result[] = new Object[2];
		for (int i = 0; i < prof.size(); i++) {
			if (aulaRepository.buscaProf(prof.get(i), data, periodo).isEmpty()) {
				emAula = false;
			} else {
				emAula = true;
			}
			
			//result[0] = prof.get(i);
			//result[1] = emAula;
			valor.add(prof.get(i));
			valor.add(emAula);
		}
		
		return valor;
	}

	// retorna apenas o prof, ausencia e se esta em aula
	@Administrador
	@User
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Object[] buscarAusenciaProfessor(@PathVariable("id") Long id) {
		List<Ausencia> ausencias = ausenciaRepository.findByProfessorId(id);
		Professor professor = professorRepository.findById(id).get();
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
	
	@Administrador
	@User
	@RequestMapping(value = "/alterarStatus/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> alterarStatusProf(@PathVariable("id") Long id, Professor prof,
            HttpServletRequest request) {       
        prof = professorRepository.findById(id).get();
        if (prof.getAtivo()) {
            prof.setAtivo(false);
        } else {
            prof.setAtivo(true);
        }
        professorRepository.save(prof);
        Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
        return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
    }
	
	//METODO QUE TRAS PROFESSOR CONFORME O CURSO SELECIONADO
	// URL = localhost:8080/api/professor/buscaProfCurso/3
	@Administrador
	@User
	@RequestMapping(value = "/buscaProfCurso/{id}", method = RequestMethod.GET)
	public List<Professor> listaProfessorPorCurso(@PathVariable("id") Long id){
		return professorRepository.buscaProfessorPorCurso(id);
	}

}