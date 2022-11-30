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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.backend.sga.model.Ausencia;
import com.backend.sga.model.Erro;
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

	@RequestMapping(value = "/criaAusencia", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> criaAusenciaParaProfessores(@RequestBody RecebeAula recebe) {
		try {
			
			List<Ausencia> ListAusencia = (List<Ausencia>) ausenciaRepository.findAll();

			if (ListAusencia.isEmpty()) {
				for (int i = 0; i < recebe.getProfList().size(); i++) {

					Ausencia ausencia = new Ausencia();
					ausencia.setProfessor(recebe.getProfList().get(i));
					ausencia.setDataInicio(recebe.getDataInicio());
					ausencia.setDataFinal(recebe.getDataFinal());
					ausencia.setTipo(TipoAusencia.FERIAS);
					ausenciaRepository.save(ausencia);
				}
				
			} else {

					for (int i = 0; i < recebe.getProfList().size(); i++) {
						Ausencia ausencia = new Ausencia();
						ausencia.setProfessor(recebe.getProfList().get(i));
						ausencia.setDataInicio(recebe.getDataInicio());
						ausencia.setDataFinal(recebe.getDataFinal());
						ausencia.setTipo(TipoAusencia.FERIAS);
						
						List<Ausencia> auseResp = ausenciaRepository.listaAusenciaDeProf(recebe.getDataInicio(), recebe.getDataFinal(), recebe.getProfList().get(i).getId());

						if (!auseResp.isEmpty()) {
							for (int j = 0; j < auseResp.size(); j++) {
								
								if (ausencia.getProfessor().getId() == auseResp.get(j).getProfessor().getId()) {
									Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR,"Erro em cadastrar Ausencia, Professor " + recebe.getProfList().get(i).getNome() + "já tem uma ausencia nessa data !", null);
									return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
								} else {
									ausenciaRepository.save(ausencia);
								}
							}
						} else {
							ausenciaRepository.save(ausencia);
						}			
				}
			}
			Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
			return new ResponseEntity<Object>(sucesso, HttpStatus.OK);

		} catch (Exception e) {
			System.out.println(e);
			Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Erro!", null);
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
	
	@RequestMapping(value = "/buscaDataAusencia/{id}", method = RequestMethod.GET)
	public List<Object> listaDataDeAusenciaProf(@PathVariable("id") Long id){
		
//		int ano = LocalDate.now().getYear();
//		
//		String dataInicioStr = "01/01/"+ano;
//		String dataFinalStr = "31/12/"+ano;
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//		Calendar dataInicio = Calendar.getInstance();
//		try {
//			dataInicio.setTime(sdf.parse(dataInicioStr));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Calendar dataFinal = Calendar.getInstance();
//		try {
//			dataFinal.setTime(sdf.parse(dataFinalStr));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		ArrayList<String> datas = new ArrayList<String>();
//		
//		List<Ausencia> ferias = ausenciaRepository.listaAusenciaDeUmProfessor(id, dataInicio, dataFinal);
//		
//		for(int i = 0; i < ferias.size(); i++) {
//			Calendar data = ferias.get(i).getDataInicio();
//			while(data.before(ferias.get(i).getDataFinal()) || data.equals(ferias.get(i).getDataFinal())) {
//				String dataFormInicio;
//				int mes = ferias.get(i).getDataFinal().get(Calendar.MONTH) + 1;
//				// formatado a variável Calendar para String (dataInicio)
//				if ((ferias.get(i).getDataInicio().get(Calendar.MONTH) + 1) < 10
//						&& ferias.get(i).getDataInicio().get(Calendar.DAY_OF_MONTH) < 10) {
//					dataFormInicio = ferias.get(i).getDataInicio().get(Calendar.YEAR) + "-0" + mes + "-0"
//							+ ferias.get(i).getDataInicio().get(Calendar.DAY_OF_MONTH);
//				} else if (ferias.get(i).getDataInicio().get(Calendar.DAY_OF_MONTH) < 10) {
//					dataFormInicio = ferias.get(i).getDataInicio().get(Calendar.YEAR) + "-" + mes + "-0"
//							+ ferias.get(i).getDataInicio().get(Calendar.DAY_OF_MONTH);
//				} else if ((ferias.get(i).getDataInicio().get(Calendar.MONTH) + 1) < 10) {
//					dataFormInicio = ferias.get(i).getDataInicio().get(Calendar.YEAR) + "-0" + mes + "-"
//							+ ferias.get(i).getDataInicio().get(Calendar.DAY_OF_MONTH);
//				} else {
//					dataFormInicio = ferias.get(i).getDataInicio().get(Calendar.YEAR) + "-" + mes + "-"
//							+ ferias.get(i).getDataInicio().get(Calendar.DAY_OF_MONTH);
//				}
//				
//				datas.add(dataFormInicio);
//				data.add(Calendar.DAY_OF_MONTH, 1);
//			}
//		}
//		
//		
//		return datas;
		
		ArrayList<Object> datas = new ArrayList<Object>();
		
		List<Ausencia> ausencias = ausenciaRepository.listaAusenciaDeUmProfessor(id);
		
		for(int i = 0; i < ausencias.size(); i++) {
			
			String dataFormInicio;
			String dataFormFinal;
			int mesIni;
			mesIni = ausencias.get(i).getDataInicio().get(Calendar.MONTH) + 1;
			int mesFin;
			mesFin = ausencias.get(i).getDataFinal().get(Calendar.MONTH) + 1;
			// formatado a variável Calendar para String (dataInicio)
			if ((ausencias.get(i).getDataInicio().get(Calendar.MONTH) + 1) < 10
					&& ausencias.get(i).getDataInicio().get(Calendar.DAY_OF_MONTH) < 10) {
				dataFormInicio = ausencias.get(i).getDataInicio().get(Calendar.YEAR) + "-0" + mesIni + "-0"
						+ ausencias.get(i).getDataInicio().get(Calendar.DAY_OF_MONTH);
			} else if (ausencias.get(i).getDataInicio().get(Calendar.DAY_OF_MONTH) < 10) {
				dataFormInicio = ausencias.get(i).getDataInicio().get(Calendar.YEAR) + "-" + mesIni + "-0"
						+ ausencias.get(i).getDataInicio().get(Calendar.DAY_OF_MONTH);
			} else if ((ausencias.get(i).getDataInicio().get(Calendar.MONTH) + 1) < 10) {
				dataFormInicio = ausencias.get(i).getDataInicio().get(Calendar.YEAR) + "-0" + mesIni + "-"
						+ ausencias.get(i).getDataInicio().get(Calendar.DAY_OF_MONTH);
			} else {
				dataFormInicio = ausencias.get(i).getDataInicio().get(Calendar.YEAR) + "-" + mesIni + "-"
						+ ausencias.get(i).getDataInicio().get(Calendar.DAY_OF_MONTH);
			}
			// formatado a variável Calendar para String (dataFinal)
			if ((ausencias.get(i).getDataFinal().get(Calendar.MONTH) + 1) < 10
					&& ausencias.get(i).getDataFinal().get(Calendar.DAY_OF_MONTH) < 10) {
				dataFormFinal = ausencias.get(i).getDataFinal().get(Calendar.YEAR) + "-0" + mesFin + "-0"
						+ ausencias.get(i).getDataFinal().get(Calendar.DAY_OF_MONTH);
			} else if (ausencias.get(i).getDataFinal().get(Calendar.DAY_OF_MONTH) < 10) {
				dataFormFinal = ausencias.get(i).getDataFinal().get(Calendar.YEAR) + "-" + mesFin + "-0"
						+ ausencias.get(i).getDataFinal().get(Calendar.DAY_OF_MONTH);
			} else if ((ausencias.get(i).getDataFinal().get(Calendar.MONTH) + 1) < 10) {
				dataFormFinal = ausencias.get(i).getDataFinal().get(Calendar.YEAR) + "-0" + mesFin + "-"
						+ ausencias.get(i).getDataFinal().get(Calendar.DAY_OF_MONTH);
			} else {
				dataFormFinal = ausencias.get(i).getDataFinal().get(Calendar.YEAR) + "-" + mesFin + "-"
						+ ausencias.get(i).getDataFinal().get(Calendar.DAY_OF_MONTH);
			}
			
			Object result[] = new Object[2];
			result[0] = dataFormInicio;
			result[1] = dataFormFinal;
			
			datas.add(result);
			
			
		}
		return datas;
		
	}
	
}