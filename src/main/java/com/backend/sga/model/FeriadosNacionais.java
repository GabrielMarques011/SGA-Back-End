package com.backend.sga.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;

import com.backend.sga.repository.FeriadosNacionaisRepository;
import com.backend.sga.service.FeriadosNacionaisService;
import com.google.common.util.concurrent.Service;

import lombok.Data;

@Data
@Entity
public class FeriadosNacionais {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String date;
	
	private String name;
	
	private String type;
	
	
	
	
	
}
