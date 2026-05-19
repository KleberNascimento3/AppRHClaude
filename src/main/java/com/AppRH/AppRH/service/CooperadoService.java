package com.AppRH.AppRH.service;

import org.springframework.data.domain.Page;

import com.AppRH.AppRH.models.Cooperado;
import com.AppRH.AppRH.util.PaginacaoUtil;

public interface CooperadoService {
	
	PaginacaoUtil<Cooperado> buscarPorPagina(int pagina);
	
//	public Page<Cooperado> getCooperadosPaginados(int pageNum, int pageSize);
	
	Page<Cooperado> findPaginated(int pageNo, int pageSize);

	Page<Cooperado> findPaginatedA(int pageNo, int pageSize);

	Page<Cooperado> findPaginatedI(int pageNo, int pageSize);
}
