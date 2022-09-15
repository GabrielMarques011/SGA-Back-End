package com.backend.sga.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.sga.repository.AusenciaRepository;

//CrossOrigin serve para que o projeto receba JSON
@CrossOrigin
@RestController
@RequestMapping("/api/ausencia")
public class AusenciaRestController {

	@Autowired
	private AusenciaRepository ausenciaRepository;
	
}
