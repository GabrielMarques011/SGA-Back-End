package com.backend.sga.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backend.sga.model.FeriadosNacionais;

@Service
public class FeriadosNacionaisService {

	public FeriadosNacionais[] consultaFeriados() {
		int ano = LocalDate.now().getYear();
		
		FeriadosNacionais resT[] = new RestTemplate().getForObject("https://brasilapi.com.br/api/feriados/v1/" + ano, FeriadosNacionais[].class);
		
		System.out.println(resT);
		
		return resT;  
	}
}
