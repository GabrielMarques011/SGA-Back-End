package com.backend.sga.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

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

import com.backend.sga.model.Ambiente;
import com.backend.sga.model.Analise;
import com.backend.sga.model.Aula;
import com.backend.sga.model.Erro;
import com.backend.sga.model.Periodo;
import com.backend.sga.model.Professor;
import com.backend.sga.model.RecebeAula;
import com.backend.sga.model.Sucesso;
import com.backend.sga.model.TipoCurso;
import com.backend.sga.model.UnidadeCurricular;
import com.backend.sga.repository.AmbienteRepository;
import com.backend.sga.repository.AulaRepository;
import com.backend.sga.repository.DiaNaoLetivoRepository;
import com.backend.sga.repository.FeriadosNacionaisRepository;
import com.backend.sga.repository.ProfessorRepository;
import com.backend.sga.repository.UnidadeCurricularRepository;

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

	@Autowired
	private AmbienteRepository ambRepository;

	@Autowired
	private UnidadeCurricularRepository repository;

	ArrayList<Aula> aulas = new ArrayList<Aula>();
	ArrayList<Professor> professoresOcp = new ArrayList<Professor>();
	ArrayList<Ambiente> ambientesOcp = new ArrayList<Ambiente>();

	/*
	 * @RequestMapping(value = "", method = RequestMethod.POST, consumes =
	 * MediaType.APPLICATION_JSON_VALUE) public Object criarAula(@RequestBody
	 * RecebeAula recebeAula, HttpServletRequest request) {
	 * 
	 * boolean dia[] = recebeAula.getDiaSemana(); Calendar dataInicio =
	 * recebeAula.getDataInicio(); UnidadeCurricular uc =
	 * repository.findById(recebeAula.getUnidadeCurricular().getId()).get(); double
	 * cargaHoraria = uc.getHoras();
	 * 
	 * // retornando uma listagem de aula List<Aula> listaAula =
	 * aulaRepository.diaSemanal(recebeAula.getDataInicio());
	 * 
	 * if (!aulaRepository.diaAula(dataInicio, recebeAula.getPeriodo(),
	 * recebeAula.getAmbiente()).isEmpty()) { Erro erro = new
	 * Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Este dia não está disponível", null);
	 * return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR); }
	 * else {
	 * 
	 * // fazendo a repetição das horas até chegar a 0 while (cargaHoraria > 0) {
	 * 
	 * // criando variavel para que sete os valores da dataInicio // !necessario
	 * (TimeZone.getTimeZone("GMT-00:00")) Calendar data =
	 * Calendar.getInstance(TimeZone.getTimeZone("GMT-00:00"));
	 * data.setTime(dataInicio.getTime()); int diaSemana =
	 * data.get(Calendar.DAY_OF_WEEK);
	 * 
	 * if (dia[diaSemana - 1] == true) {
	 * 
	 * String dataStr; int mes; mes = data.get(Calendar.MONTH) + 1;
	 * 
	 * // formatado a variável Calendar para String if (data.get(Calendar.MONTH + 1)
	 * < 10 && data.get(Calendar.DAY_OF_MONTH) < 10) { dataStr =
	 * data.get(Calendar.YEAR) + "-0" + mes + "-0" +
	 * data.get(Calendar.DAY_OF_MONTH); } else if (data.get(Calendar.DAY_OF_MONTH) <
	 * 10) { dataStr = data.get(Calendar.YEAR) + "-" + mes + "-0" +
	 * data.get(Calendar.DAY_OF_MONTH); } else if (data.get(Calendar.MONTH + 1) <
	 * 10) { dataStr = data.get(Calendar.YEAR) + "-0" + mes + "-" +
	 * data.get(Calendar.DAY_OF_MONTH); } else { dataStr = data.get(Calendar.YEAR) +
	 * "-" + mes + "-" + data.get(Calendar.DAY_OF_MONTH); }
	 * 
	 * if (feriadosRepository.buscaData(dataStr).isEmpty()) {
	 * 
	 * if (diaNaorepository.buscaDNL(data).isEmpty()) {
	 * 
	 * // criando a aula(trazendo ela) Aula aula = new Aula();
	 * 
	 * // setando os valores que precisam no cadastro de aula
	 * aula.setCurso(recebeAula.getCurso());
	 * aula.setUnidadeCurricular(recebeAula.getUnidadeCurricular());
	 * aula.setCodTurma(recebeAula.getCodTurma());
	 * aula.setPeriodo(recebeAula.getPeriodo());
	 * aula.setCargaDiaria(recebeAula.getCargaDiaria()); aula.setData(data);
	 * 
	 * aulas.add(aula);
	 * 
	 * List<Professor> profOcupado = professorRepository.buscaOcupado(data,
	 * recebeAula.getPeriodo()); if(!profOcupado.isEmpty()) { for(int i = 0; i <
	 * profOcupado.size(); i++) { professoresOcp.add(profOcupado.get(i)); } }
	 * 
	 * List<Ambiente> ambOcopados = ambRepository.retornaOcupadosDia(data,
	 * recebeAula.getPeriodo()); if(!ambOcopados.isEmpty()) { for(int i = 0; i <
	 * ambOcopados.size(); i++) { ambientesOcp.add(ambOcopados.get(i)); } }
	 * 
	 * // Subtraindo a carga horaria depois que o cadastro acontece cargaHoraria =
	 * cargaHoraria - aula.getCargaDiaria();
	 * 
	 * } } } // Pulando de 1 dia em 1 dia... dataInicio.add(Calendar.DAY_OF_MONTH,
	 * 1); } } Calendar result[] = new Calendar[2]; result[0] =
	 * aulas.get(0).getData(); result[1] = aulas.get(aulas.size() - 1).getData();
	 * return result; }
	 */

	@RequestMapping(value = "/valoresLivres", method = RequestMethod.GET)
	public Object[] retornaProfsEAmbsLivres() {

		List<Professor> professores = (List<Professor>) professorRepository.findAll();
		List<Ambiente> ambientes = (List<Ambiente>) ambRepository.findAll();

		System.out.println(ambientesOcp);
		System.out.println(professoresOcp.get(0).getNome());

		for (int i = 0; i < professoresOcp.size(); i++) {
			for (int j = 0; j < professores.size(); j++) {
				if (professores.get(j) == professoresOcp.get(i)) {
					professores.remove(j);
				}
			}
		}

		for (int i = 0; i < ambientesOcp.size(); i++) {
			for (int j = 0; j < ambientes.size(); j++) {
				if (ambientes.get(j) == ambientesOcp.get(i)) {
					System.out.println("passou prof");
					ambientes.remove(j);
				}
			}
		}

		Object result[] = new Object[2];
		result[0] = professores;
		result[1] = ambientes;

		return result;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deletarAula(@RequestBody Aula aula, @PathVariable("id") Long id,
			HttpServletRequest request) {
		if (aula.getId() == id) {
			aulaRepository.deleteById(id);
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		} else {
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível deletar a aula", null); // de //
																												// erro
			return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Aula> listarAulas() {
		return aulaRepository.findAll();
	}

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

	@RequestMapping(value = "/turma/{codTurma}", method = RequestMethod.PUT)
	public ResponseEntity<Object> attAulas(@PathVariable("codTurma") String codTurma,
			@RequestBody RecebeAula recebeAula) {

		List<Aula> codData = aulaRepository.buscaDatasECod(codTurma, recebeAula.getDataInicio(),
				recebeAula.getDataFinal());

		if (!codData.isEmpty()) {

			for (int i = 0; i < codData.size(); i++) {
				// setando novos valores
				codData.get(i).setProfessor(recebeAula.getProfessor());
				codData.get(i).setAmbiente(recebeAula.getAmbiente());
				aulaRepository.save(codData.get(i));
			}
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
		}
		Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "ERRO ARROMBADO, VERIFICA ESSA PORRA", null);
		return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@RequestMapping(value = "/periodo", method = RequestMethod.GET)
	public Optional<Aula> retornaPeriodo(@RequestParam("periodo") Periodo periodo,
			@RequestParam("data") String dataStr) {

		System.out.println(periodo);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // formatador de data

		Calendar data = Calendar.getInstance(); // variável para guardar a data_inicio
		try {
			data.setTime(sdf.parse(dataStr)); // tranformando a String em calendar
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Optional<Aula> aula = aulaRepository.retornaPeriodo(periodo, data);

		return aula;
	}

	@RequestMapping(value = "/filtro/{value}", method = RequestMethod.GET)
	public List<Aula> buscaFiltroAula(@PathVariable("value") String value) {
		return aulaRepository.filtroAula(value);
	}

	// add
	@RequestMapping(value = "/{data}", method = RequestMethod.GET)
	public List<Aula> buscaPorData(@PathVariable("data") String dataStr) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // formatador de data

		Calendar data = Calendar.getInstance(); // variável para guardar a data_inicio
		try {
			data.setTime(sdf.parse(dataStr)); // tranformando a String em calendar
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return aulaRepository.buscaData(data);
	}

	// METODO COMPARAÇAÕ DO MES ANTERIOS (VALOR PERIODO DASHBOARD)
	@RequestMapping(value = "/analise/{mes}", method = RequestMethod.GET)
	public ArrayList<Object> comparacaoMes(@PathVariable("mes") int mes) {
		int ano = LocalDate.now().getYear();

		String data = ano + "-" + mes + "-01";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar comecoMes = Calendar.getInstance();
		try {
			comecoMes.setTime(sdf.parse(data));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String dataStr;

		if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) {
			dataStr = ano + "-" + mes + "-31";
		} else if (mes == 2) {
			dataStr = ano + "-" + mes + "-28";
		} else {
			dataStr = ano + "-" + mes + "-30";
		}
		Calendar finalMes = Calendar.getInstance();
		try {
			finalMes.setTime(sdf.parse(dataStr));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<Object> valores = new ArrayList<Object>();

		List<Periodo> periodos = aulaRepository.comparacaoMes(comecoMes, finalMes);
		List<Integer> atual = aulaRepository.valorMes(comecoMes, finalMes);
		comecoMes.add(Calendar.MONTH, -1);
		finalMes.add(Calendar.MONTH, -1);
		List<Integer> passado = aulaRepository.valorMes(comecoMes, finalMes);

		for (int i = 0; i < periodos.size(); i++) {
			Analise result = new Analise();
			result.setPeriodo(periodos.get(i));
			result.setQuantidade(atual.get(i));
			if (!passado.isEmpty()) {
				if (atual.get(i) > passado.get(i)) {
					result.setMaior(true);
				} else {
					result.setMaior(false);
				}
			} else {
				result.setMaior(true);
			}

			valores.add(result);
		}

		return valores;
	}

	@RequestMapping(value = "/trasPeriodo/{periodo}", method = RequestMethod.GET)
	public List<Aula> trasPorPeriodo(@PathVariable("periodo") Periodo periodo) {
		return aulaRepository.listaPorPeriodo(periodo);
	}

	@RequestMapping(value = "/professor/{idProf}", method = RequestMethod.GET)
	public List<Aula> retornaAulaProf(@PathVariable("idProf") Long id) {
		return aulaRepository.retornaAulasProf(id);
	}

	@RequestMapping(value = "/aulaTipo", method = RequestMethod.GET)
	public List<Calendar> retornaAulaTipo(@RequestParam("prof") Long id, @RequestParam("tipo") TipoCurso tipo) {
		return aulaRepository.retornaAulaProfTipoData(id, tipo);
	}

	@RequestMapping(value = "/lista", method = RequestMethod.GET)
	public List<Aula> retornaEntredatas(@RequestParam("dataInicio") String dataInicioStr,
			@RequestParam("dataFinal") String dataFinalStr) {
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
			dataFinal.setTime(sdf.parse(dataFinalStr));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return aulaRepository.buscaEntreDatas(dataInicio, dataFinal);

	}

}
