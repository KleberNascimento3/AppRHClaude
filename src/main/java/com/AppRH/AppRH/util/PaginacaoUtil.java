package com.AppRH.AppRH.util;

import java.util.List;

public class PaginacaoUtil<T> {
	
	private int tamanho;	
	
	private int pagina;
	
	private Long totaldepaginas;	
	
	private List<T> registros;

	public PaginacaoUtil(int tamanho, int pagina, Long totaldepaginas, List<T> registros) {
		super();
		this.tamanho = tamanho;
		this.pagina = pagina;
		this.totaldepaginas = totaldepaginas;
		this.registros = registros;
	}


	public int getTamanho() {
		return tamanho;
	}

	public int getPagina() {
		return pagina;
	}


	public Long getTotaldepaginas() {
		return totaldepaginas;
	}
	
	public List<T> getRegistros() {
		return registros;
	}


}
