package com.AppRH.AppRH.dao;

import java.util.List;

import com.AppRH.AppRH.models.Cooperado;
import com.AppRH.AppRH.util.PaginacaoUtil;

public interface CooperadoDao {
	
	void save (Cooperado cooperado);
	
	void update (Cooperado cooperado);
	
	void delete (Cooperado cooperado);
	
	Cooperado findById(Long id);
	
	List<Cooperado> findAll();
	
	PaginacaoUtil<Cooperado> buscaPaginada(int pagina);

}
