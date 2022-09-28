package com.backend.sga.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.backend.sga.model.Chamado;

public interface ChamadoRepository extends PagingAndSortingRepository<Chamado, Long>{

}
