package com.backend.sga.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backend.sga.model.FeriadosNacionais;
import com.backend.sga.repository.FeriadosNacionaisRepository;

@Service
public class FeriadosNacionaisService {

	@Autowired
	private FeriadosNacionaisRepository repository;
	
	public void salvarFeriados() {
		int ano = LocalDate.now().getYear();
		FeriadosNacionais feriadosApi[] = new RestTemplate().getForObject("https://brasilapi.com.br/api/feriados/v1/" + ano, FeriadosNacionais[].class);
		
		List<FeriadosNacionais> feriadosBD = (List<FeriadosNacionais>) repository.findAll();
				
		if(feriadosApi != null) {
			if(feriadosBD.isEmpty()) {
				for(int i = 0; i < feriadosApi.length; i++) {
					FeriadosNacionais feriado = new FeriadosNacionais();
					feriado.setDate(feriadosApi[i].getDate());
					feriado.setName(feriadosApi[i].getName());
					feriado.setType(feriadosApi[i].getType());
					
					repository.save(feriado);
				}
			} else {
				String anoBD = feriadosBD.get(feriadosBD.size() - 1).getDate().substring(0, 4);
				String anoAtual = LocalDate.now().getYear()+"";
				
				System.out.println(anoBD +" / "+ anoAtual);
				
				if(!anoBD.equals(anoAtual)) {
					try {
						// deleta todos os feriados nacionais antigos
						repository.deleteAll();
						
						for(int i = 0; i < feriadosApi.length; i++) {
							FeriadosNacionais feriado = new FeriadosNacionais();
							feriado.setDate(feriadosApi[i].getDate());
							feriado.setName(feriadosApi[i].getName());
							feriado.setType(feriadosApi[i].getType());
							
							repository.save(feriado);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
