package com.AppRH.AppRH.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.AppRH.AppRH.models.Cooperado;
import com.AppRH.AppRH.util.PaginacaoUtil;

@Repository
public class CooperadoDaoImpl extends AbstractDao<Cooperado, Long> implements CooperadoDao{

	public PaginacaoUtil<Cooperado> buscaPaginada(int pagina){
		int tamanho=5;
		int inicio = (pagina-1)*tamanho;
		List<Cooperado> cooperados=getEntityManager()
				.createQuery("select c from Cooperado c order by c.coopnome asc", Cooperado.class)
				.setFirstResult(inicio)
				.setMaxResults(tamanho)
				.getResultList();
		long totalRegistros = count();
		long totalDePaginas=(totalRegistros + (tamanho-1))/tamanho;
		
		return new PaginacaoUtil<>(tamanho,pagina,totalDePaginas,cooperados);
	}
	
	public long count() {
		return getEntityManager()
				.createQuery("select count(*) from Cooperado",Long.class)
				.getSingleResult();
	}
	
	@Override
	public void save(Cooperado cooperado) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Cooperado cooperado) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Cooperado cooperado) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Cooperado findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
