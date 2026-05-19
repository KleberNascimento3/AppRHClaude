package com.AppRH.AppRH.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.AppRH.AppRH.dao.CooperadoDao;
import com.AppRH.AppRH.models.Cooperado;
import com.AppRH.AppRH.repository.CooperadoRepository;
import com.AppRH.AppRH.util.PaginacaoUtil;


@Service @Transactional(readOnly=false)
public class CooperadoServiceImpl implements CooperadoService {
	
	@Autowired
	private CooperadoDao cd;
	
	@Autowired
	private CooperadoRepository cr;
	

	@Override
	public PaginacaoUtil<Cooperado> buscarPorPagina(int pagina) {
		
		return cd.buscaPaginada(pagina);
	}
	
	/*public Page<Cooperado> getCooperadosPaginados(int pageNum, int pageSize){
		Pageable pageable = PageRequest.of(pageNum -1,pageSize);
		return cr.findAll(pageable);
	}
*/
	@Override
	public Page<Cooperado> findPaginated(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo -1, pageSize);
		return this.cr.encontrarTodos(pageable);
	}
	
	@Override
	public Page<Cooperado> findPaginatedA(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo -1, pageSize);
		return this.cr.encontrarAtivos(pageable);
	}
	
	@Override
	public Page<Cooperado> findPaginatedI(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo -1, pageSize);
		return this.cr.encontrarInativos(pageable);
	}

}
