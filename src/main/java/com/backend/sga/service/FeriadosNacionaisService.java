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

	public FeriadosNacionais[] consultaFeriados() {
		int ano = LocalDate.now().getYear();
		
		FeriadosNacionais resT[] = new RestTemplate().getForObject("https://brasilapi.com.br/api/feriados/v1/" + ano, FeriadosNacionais[].class);
		
		System.out.println(resT);
		
		return resT;  
	}
	
	
	
	public void salvarFeriados() {
		FeriadosNacionais feriados[] = consultaFeriados();
		
		List<FeriadosNacionais> feriadosBD = (List<FeriadosNacionais>) repository.findAll();
				
		if(feriados != null) {
			if(feriadosBD.isEmpty()) {
				for(int i = 0; i < feriados.length; i++) {
					FeriadosNacionais feriado = new FeriadosNacionais();
					feriado.setDate(feriados[i].getDate());
					feriado.setName(feriados[i].getName());
					feriado.setType(feriados[i].getType());
					
					repository.save(feriado);
				}
			}else {
				String anoBD = feriadosBD.get(feriadosBD.size() - 1).getDate().substring(0, 4);
				String anoAtual = LocalDate.now().getYear()+"";
				
				System.out.println(anoBD +" / "+ anoAtual);
				
				if(!anoBD.equals(anoAtual)) {
					for(int i = 0; i < feriados.length; i++) {
						FeriadosNacionais feriado = new FeriadosNacionais();
						feriado.setDate(feriados[i].getDate());
						feriado.setName(feriados[i].getName());
						feriado.setType(feriados[i].getType());
						
						repository.save(feriado);
					}
				}
				
			}
		}
	}
}
