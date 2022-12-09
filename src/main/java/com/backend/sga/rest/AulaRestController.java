package com.backend.sga.rest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
import com.backend.sga.annotation.Administrador;
import com.backend.sga.annotation.User;
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
    private DiaNaoLetivoRepository diaNaoLetivoRepository;
    @Autowired
    private FeriadosNacionaisRepository feriadosNacionaisRepository;
    @Autowired
    private AmbienteRepository ambienteRepository;
    @Autowired
    private UnidadeCurricularRepository unidadeCurricularRepository;
    ArrayList<Aula> aulas = new ArrayList<Aula>();
    @User
    @Administrador
    @RequestMapping(value = "/criar", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object criarAula(@RequestBody RecebeAula recebeAula, HttpServletRequest request) {
        // Limpando a variavel global
        aulas.clear();
        ArrayList<Professor> professoresOcupados = new ArrayList<Professor>();
        ArrayList<Ambiente> ambientesOcupados = new ArrayList<Ambiente>();
        boolean diasDaSemana[] = recebeAula.getDiaSemana();
        Calendar dataInicio = recebeAula.getDataInicio();
        UnidadeCurricular uc = unidadeCurricularRepository.findById(recebeAula.getUnidadeCurricular().getId()).get();
        double cargaHorariaUC = uc.getHoras();
        double cargaDiaria = recebeAula.getCargaDiaria();
        // RETORNA UMA LISTAGEM DE AULA
        List<Aula> listaAula = aulaRepository.diaSemanal(recebeAula.getDataInicio());
        /*
         * if (!aulaRepository.diaAula(dataInicio, recebeAula.getPeriodo(),
         * recebeAula.getAmbiente()).isEmpty()) { Erro erro = new
         * Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Este dia não está disponível", null);
         * return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR); }
         * else
         */
        if (cargaDiaria <= 0) {
            Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR,
                    "A carga diária da aula não pode ser menor ou igual a zero...", null);
            return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            int valorRandom;
            do {
                // CRIANDO UMA VARIAVEL QUE 'SORTEIA' NUMEROS ALEATORIOS
                Random random = new Random();
                // SETANDO A VARIAVEL DANDO UM NUMERO MAX PARA ELA
                valorRandom = random.nextInt(10000);
            } while (!aulaRepository.findByPartitionKey(valorRandom).isEmpty());
            // FAZENDO A REPETIÇÃO DE HORAS ATÉ CHEGAR A 0
            while (cargaHorariaUC > 0) {
                // !NECESSARIO (TimeZone.getTimeZone("GMT-00:00"))
                Calendar data = Calendar.getInstance(TimeZone.getTimeZone("GMT-00:00"));
                data.setTime(dataInicio.getTime());
                String dataStr;
                int mes;
                mes = data.get(Calendar.MONTH) + 1;
                // FORMATANDO DE CALENDAR PARA STRING
                if (data.get(Calendar.MONTH + 1) < 10 && data.get(Calendar.DAY_OF_MONTH) < 10) {
                    dataStr = data.get(Calendar.YEAR) + "-0" + mes + "-0" + data.get(Calendar.DAY_OF_MONTH);
                } else if (data.get(Calendar.DAY_OF_MONTH) < 10) {
                    dataStr = data.get(Calendar.YEAR) + "-" + mes + "-0" + data.get(Calendar.DAY_OF_MONTH);
                } else if (data.get(Calendar.MONTH + 1) < 10) {
                    dataStr = data.get(Calendar.YEAR) + "-0" + mes + "-" + data.get(Calendar.DAY_OF_MONTH);
                } else {
                    dataStr = data.get(Calendar.YEAR) + "-" + mes + "-" + data.get(Calendar.DAY_OF_MONTH);
                }
                if (feriadosNacionaisRepository.findByDate(dataStr).isEmpty()
                        || diaNaoLetivoRepository.findByData(data).isEmpty()) {
                    int diaSemana = data.get(Calendar.DAY_OF_WEEK);
                    if (diasDaSemana[diaSemana - 1] == true) {
                        // CRIANDO A AULA
                        Aula aula = new Aula();
                        // SETANDO OS VALORES
                        aula.setCurso(recebeAula.getCurso());
                        aula.setUnidadeCurricular(recebeAula.getUnidadeCurricular());
                        aula.setCodTurma(recebeAula.getCodTurma());
                        aula.setPeriodo(recebeAula.getPeriodo());
                        aula.setCargaDiaria(recebeAula.getCargaDiaria());
                        aula.setPartitionKey(valorRandom);
                        aula.setData(data);
                        aulas.add(aula);
                        if (recebeAula.getPeriodo() != Periodo.INTEGRAL) {
                            if (!professorRepository.buscaOcupado(data, recebeAula.getPeriodo()).isEmpty()) {
                                professoresOcupados
                                        .addAll(professorRepository.buscaOcupado(data, recebeAula.getPeriodo()));
                            }
                            if (!ambienteRepository.retornaOcupadosPorDia(data, recebeAula.getPeriodo()).isEmpty()) {
                                ambientesOcupados.addAll(
                                        ambienteRepository.retornaOcupadosPorDia(data, recebeAula.getPeriodo()));
                            }
                            if(recebeAula.getPeriodo() != Periodo.NOITE) {
                                ambientesOcupados.addAll(ambienteRepository.retornaOcupadosPorDia(data, Periodo.INTEGRAL));
                                professoresOcupados.addAll(professorRepository.buscaOcupado(data, Periodo.INTEGRAL));   
                            }
                        } else {
                            //Validação para não permitir que crie nenhuma aula se a aula do front for integral
                            professoresOcupados.addAll(professorRepository.buscaOcupadoData(dataInicio));
                            ambientesOcupados.addAll(ambienteRepository.ocupadosPorData(dataInicio));
                        }
                        // SUBTRAINDO A CARGA HORARIA DEPOIS QUE O CADASTRO ACONTECE
                        cargaHorariaUC = cargaHorariaUC - aula.getCargaDiaria();
                    }
                    // PULANDO DE 1 EM 1 DIA...
                    dataInicio.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
        }
        List<Professor> professores = (List<Professor>) professorRepository.findAllAtivo();
        List<Ambiente> ambientes = (List<Ambiente>) ambienteRepository.findAllAtivo();
        ambientes.removeAll(ambientesOcupados);
        professores.removeAll(professoresOcupados);
        Object result[] = new Object[3];
        result[0] = aulas.get(aulas.size() - 1).getData();
        result[1] = professores;
        result[2] = ambientes;
        return result;
    }
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Object> salvarAulas(@RequestBody RecebeAula recebeAula) {
        System.out.println(aulas.size());
        try {
            for (int i = 0; i < aulas.size(); i++) {
                aulas.get(i).setAmbiente(recebeAula.getAmbiente());
                aulas.get(i).setProfessor(recebeAula.getProfessor());
                aulaRepository.save(aulas.get(i));
            }
            aulas.clear();
            Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
            return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
        } catch (Exception e) {
            Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível cadastrar a aula", null);
            return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // LISTA DE AULAS
    // URL = localhost:8080/api/aula
    @User
    @Administrador
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<Aula> listarAulas() {
        return aulaRepository.findAll();
    }
    // DELETAR AULAS
    // URL = localhost:8080/api/aula/key/9176
    @User
    @Administrador
    @RequestMapping(value = "/key/{partitionKey}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> DeletarAulas(@PathVariable("partitionKey") int partitionKey) {
        List<Aula> keyData = aulaRepository.findByPartitionKey(partitionKey);
        if (!keyData.isEmpty()) {
            for (int i = 0; i < keyData.size(); i++) {
                aulaRepository.deleteAll(keyData);
            }
            Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
            return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
        }
        Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não excluir as aulas", null);
        return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // ATUALIZAR AULA
    // URL = localhost:8080/api/aula/1
    @User
    @Administrador
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
    // METODO PEDIDO MOBILE
    // URL = localhost:8080/api/aula/busca/81
    @User
    @Administrador
    @RequestMapping(value = "/busca/{id}", method = RequestMethod.GET)
    public Iterable<Aula> listaPorId(@PathVariable("id") Long id) {
        return aulaRepository.listaID(id);
    }
    // METODO PARA EDITAR AULAS PELO KEY
    // URL = localhost:8080/api/aula/key/9176
    @User
    @Administrador
    @RequestMapping(value = "/key/{partitionKey}", method = RequestMethod.PUT)
    public ResponseEntity<Object> attAulas(@PathVariable("partitionKey") int partitionKey,
            @RequestBody RecebeAula recebeAula) {
        List<Aula> keyData = aulaRepository.buscaDatasEKey(partitionKey, recebeAula.getDataInicio(),
                recebeAula.getDataFinal());
        if (!keyData.isEmpty()) {
            for (int i = 0; i < keyData.size(); i++) {
                // setando novos valores
                keyData.get(i).setProfessor(recebeAula.getProfessor());
                keyData.get(i).setAmbiente(recebeAula.getAmbiente());
                aulaRepository.save(keyData.get(i));
            }
            Sucesso sucesso = new Sucesso(HttpStatus.OK, "Sucesso");
            return new ResponseEntity<Object>(sucesso, HttpStatus.OK);
        }
        Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR,
                "Não foi editar aulas no periodo desejado pois já existe aulas dentro desse intervalo de datas", null);
        return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // *NÃO SERIA MELHOR PASSAR SÓ O PERIODO E TRAZER AULAS RELACIONADAS A ISSO
    // METODO QUE TRAS AULA POR PERIODO
    // URL = localhost:8080/api/aula/periodo?periodo=TARDE&data=2022-11-23
    @User
    @Administrador
    @RequestMapping(value = "/periodo", method = RequestMethod.GET)
    public Optional<Aula> retornaPeriodo(@RequestParam("periodo") Periodo periodo,
            @RequestParam("data") String dataStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // FORMATANDO DATA
        Calendar data = Calendar.getInstance(); // VARIAVEL PARA GUARDAR
        try {
            data.setTime(sdf.parse(dataStr)); // TRANSFORMANDO STRING EM CALENDAR
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Optional<Aula> aula = aulaRepository.findByPeriodoEData(periodo, data);
        return aula;
    }
    // AUTOCOMPLETE
    // URL = localhost:8080/api/aula/filtro/Desenvolvimento
    @User
    @Administrador
    @RequestMapping(value = "/filtro/{value}", method = RequestMethod.GET)
    public List<Aula> buscaFiltroAula(@PathVariable("value") String value) {
        return aulaRepository.filtroAula(value);
    }
    // METODO BUSCA UMA AULA POR DATA
    // URL = localhost:8080/api/aula/2022-11-23
    @User
    @Administrador
    @RequestMapping(value = "/{data}", method = RequestMethod.GET)
    public List<Aula> buscaPorData(@PathVariable("data") String dataStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // FORMATANDO DATA
        Calendar data = Calendar.getInstance(); // VARIAVEL PARA GUARDAR
        try {
            data.setTime(sdf.parse(dataStr)); // TRANSFORMANDO A STRING EM CALENDAR
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return aulaRepository.buscaData(data);
    }
    // METODO COMPARAÇAÕ DO MES ANTERIOS (VALOR PERIODO DASHBOARD)
    // URL = localhost:8080/api/aula/analise/11
    @User
    @Administrador
    @RequestMapping(value = "/analise/mes/{mes}/fimMes/{fimMes}", method = RequestMethod.GET)
    public ArrayList<Object> comparacaoMes(@PathVariable("mes") String mes, @PathVariable("fimMes") String mesFinal) {
        ArrayList<Object> valores = new ArrayList<Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar dataFim = Calendar.getInstance();
        Calendar dataInicio = Calendar.getInstance();
        Calendar lastMonth = Calendar.getInstance();
        try {
            dataFim.setTime(sdf.parse(mesFinal)); // TRANSFORMANDO A STRING EM CALENDAR
            dataInicio.setTime(sdf.parse(mes));
            lastMonth.setTime(sdf.parse(mes));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        lastMonth.add(Calendar.DAY_OF_MONTH, -1);
        List<Periodo> periodos = aulaRepository.comparacaoMes(dataInicio, dataFim);
        List<Integer> atual = aulaRepository.valorMes(dataInicio, dataFim);
        System.out.println(dataInicio);
        System.out.println(lastMonth);
        List<Integer> passado = aulaRepository.valorMes(lastMonth, dataInicio);
        for (int i = 0; i < periodos.size(); i++) {
            Analise result = new Analise();
            result.setPeriodo(periodos.get(i));
            result.setQuantidade(atual.get(i));
            if (!passado.isEmpty()) {
                if (atual.size() > passado.size()) {
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
    @User
    @Administrador
    @RequestMapping(value = "/trasPeriodo/{periodo}", method = RequestMethod.GET)
    public List<Aula> trasPorPeriodo(@PathVariable("periodo") Periodo periodo) {
        return aulaRepository.listaPorPeriodo(periodo);
    }
    // URL = localhost:8080/api/aula/prof?idProf=1&data=11/11/2022
    // METODO PARA RETORNAR UMA LISTA DE AULA CONFORME ID PASSADO E DATA
    @User
    @Administrador
    @RequestMapping(value = "/prof", method = RequestMethod.GET)
    public List<Aula> retornaAulaProf(@RequestParam("idProf") Long id, @RequestParam("data") String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar dataFormat = Calendar.getInstance();
        try {
            dataFormat.setTime(sdf.parse(data));
        } catch (Exception e) {
        }
        return aulaRepository.retornaAulasProf(id, dataFormat);
    }
    // METODO QUE RETORNA APENAS DATAS DE UM PROFESSOR E UM TIPO ESPECIFICO
    // URL = localhost:8080/api/aula/aulaTipo?prof=3&tipo=FIC
    @User
    @Administrador
    @RequestMapping(value = "/aulaTipo", method = RequestMethod.GET)
    public ArrayList<String> retornaAulaTipo(@RequestParam("prof") Long id, @RequestParam("tipo") TipoCurso tipo) {
        List<Calendar> datas = aulaRepository.retornaAulaProfTipoData(id, tipo);
        ArrayList<String> datasFormat = new ArrayList<String>();
        // FORMATANDO DATA
        for (int i = 0; i < datas.size(); i++) {
            String dataStr;
            int mes = datas.get(i).get(Calendar.MONTH) + 1;
            if (datas.get(i).get(Calendar.MONTH + 1) < 10 && datas.get(i).get(Calendar.DAY_OF_MONTH) < 10) {
                dataStr = datas.get(i).get(Calendar.YEAR) + "-0" + mes + "-0" + datas.get(i).get(Calendar.DAY_OF_MONTH);
            } else if (datas.get(i).get(Calendar.DAY_OF_MONTH) < 10) {
                dataStr = datas.get(i).get(Calendar.YEAR) + "-" + mes + "-0" + datas.get(i).get(Calendar.DAY_OF_MONTH);
            } else if (datas.get(i).get(Calendar.MONTH + 1) < 10) {
                dataStr = datas.get(i).get(Calendar.YEAR) + "-0" + mes + "-" + datas.get(i).get(Calendar.DAY_OF_MONTH);
            } else {
                dataStr = datas.get(i).get(Calendar.YEAR) + "-" + mes + "-" + datas.get(i).get(Calendar.DAY_OF_MONTH);
            }
            datasFormat.add(dataStr);
        }
        return datasFormat;
    }
    // METODO QUE LISTA AULAS EM DETERMINADAS DATAS
    // URL =
    // localhost:8080/api/aula/lista?dataInicio=2022-11-23&dataFinal=2022-11-28
    @User
    @Administrador
    @RequestMapping(value = "/lista", method = RequestMethod.GET)
    public List<Aula> retornaEntredatas(@RequestParam("dataInicio") String dataInicioStr,
            @RequestParam("dataFinal") String dataFinalStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar dataInicio = Calendar.getInstance();
        try {
            dataInicio.setTime(sdf.parse(dataInicioStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar dataFinal = Calendar.getInstance();
        try {
            dataFinal.setTime(sdf.parse(dataFinalStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return aulaRepository.buscaEntreDatas(dataInicio, dataFinal);
    }
    // DISPONIBILIDADE PROF E AMBIENTE ATIVOS
    // URL =
    // localhost:8080/api/aula/aulaProfessorAmbienteDisponivel?periodo=MANHA&dataInicio=23/11/2022
    @User
    @Administrador
    @RequestMapping(value = "/aulaProfessorAmbienteDisponivel", method = RequestMethod.GET)
    public Object aulaProfessorAmbienteDisponivel(@RequestParam("data") String data,
            @RequestParam("periodo") Periodo periodo, @RequestParam("id") Long id) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // FORMATANDO DATA
        Calendar calendar = Calendar.getInstance(); // GUARDANDO AS VARIAVEL
        try {
            calendar.setTime(sdf.parse(data)); // TRANSFORMANDO DE STRING PARA CALENDAR
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Aula aula = aulaRepository.findById(id).get();
        if (aula != null && aula.getId() != id) {
            Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR, "Aula existente nesse dia e periodo", null);
            return new ResponseEntity<Object>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            List<Professor> professores = (List<Professor>) professorRepository.findAllAtivo();
            List<Ambiente> ambientes = (List<Ambiente>) ambienteRepository.findAllAtivo();
            professores.removeAll(professorRepository.buscaOcupado(calendar, periodo));
            ambientes.removeAll(ambienteRepository.retornaOcupadosPorDia(calendar, periodo));
            Object result[] = new Object[2];
            result[0] = professores;
            result[1] = ambientes;
            return result;
        }
    }
    // DISPONIBILIDADE PROF E AMBIENTE POR DATA INICIO E FINAL ATIVOS
    // URL =
    // localhost:8080/api/aula/aulasProfessorAmbienteDisponivel?periodo=NOITE&dataInicio=16/01/2023&dataFinal=17/01/2023
    @User
    @Administrador
    @RequestMapping(value = "/aulasProfessorAmbienteDisponivel", method = RequestMethod.GET)
    public Object aulasProfessorAmbienteDisponivel(@RequestParam("dataInicio") String dataInicio,
            @RequestParam("dataFinal") String dataFinal, @RequestParam("periodo") Periodo periodo) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // FORMATANDO DATA
        Calendar calendar = Calendar.getInstance(); // GUARDANDO AS VARIAVEL
        try {
            calendar.setTime(sdf.parse(dataInicio)); // TRANSFORMANDO DE STRING PARA CALENDAR
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendarDois = Calendar.getInstance(); // GUARDANDO AS VARIAVEL
        try {
            calendarDois.setTime(sdf.parse(dataFinal)); // TRANSFORMANDO DE STRING PARA CALENDAR
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Professor> professores = (List<Professor>) professorRepository.findAllAtivo();
        List<Ambiente> ambientes = (List<Ambiente>) ambienteRepository.findAllAtivo();
//      for (int i = 0; i < professores.size(); i++) {
//          for (int j = 0; j < professorRepository.buscaOcupado(calendar, periodo).size(); j++) {
//              if (professores.get(i).getId() == professorRepository.buscaOcupado(calendar, periodo).get(j).getId()) {
//                  professores.remove(i);
//              }
//          }
//      }
        Object result[] = new Object[2];
        result[0] = professores;
        result[1] = ambientes;
        return result;
    }
    @User
    @Administrador
    @RequestMapping(value = "/listaPorData", method = RequestMethod.GET)
    public Object listaAulaPorDataExpecifica(@RequestParam("data") String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar dataInicio = Calendar.getInstance();
        try {
            dataInicio.setTime(sdf.parse(data));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Object[] result = new Object[1];
        result[0] = aulaRepository.listaAulaDeDataExpecifica(dataInicio);
        return result;
    }
    @User
    @Administrador
    @RequestMapping(value = "/filtro", method = RequestMethod.GET)
    public List<Aula> filtraAulaGeral(@RequestParam("value") String value, @RequestParam("data") String dataStr,
            @RequestParam("periodo") Periodo periodo) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar data = Calendar.getInstance();
        try {
            data.setTime(sdf.parse(dataStr));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return aulaRepository.filtroAulaGeral(value, periodo, data);
    }
//  @RequestMapping(value = "/filtroAula", method = RequestMethod.GET)
//  public List<RecebeMobile> filtraAulaPorAmbiente(@RequestParam("data") String data) {
//
//      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//      Calendar date = Calendar.getInstance();
//      try {
//          date.setTime(sdf.parse(data));
//      } catch (Exception e) {
//
//      }
//
//      List<RecebeMobile> rb = new ArrayList<RecebeMobile>();
//
//      List<Aula> aulas = aulaRepository.trasAulaPorData(date);
//
//      List<Ambiente> amb = ambienteRepository.findAllByOrderById();
//
//      for (Ambiente a : amb) {
//          RecebeMobile rec = new RecebeMobile();
//          rec.setAmbiente(a);
//          rb.add(rec);
//      }
//
//      int i = 0;
//
//      RecebeMobile r = rb.get(i);
//      for (Aula a : aulas) {
//          a.getProfessor().setFoto(null);
//          if (a.getAmbiente().equals(r.getAmbiente())) {
//              r.addAula(a);
//          } else {
//              if (i < rb.size()) {
//                  i++;
//                  r = rb.get(i);
//              }
//          }
//      }
//
//      return rb;
//  }
}