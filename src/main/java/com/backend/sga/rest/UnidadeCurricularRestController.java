package com.backend.sga.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.sga.repository.UnidadeCurricularRepository;

//CrossOrigin serve para que o projeto receba JSON
@CrossOrigin
@RestController
@RequestMapping("/api/unidade")
public class UnidadeCurricularRestController {

	private UnidadeCurricularRepository curricularRepository;
	
}
