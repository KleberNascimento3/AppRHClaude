package com.AppRH.AppRH.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="coop_endereco")
public class Coopendereco {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="coop_end_index_cod")
	private int coopendindexcod;
	
	@NotNull
	@Column(name="coop_matricula",insertable = false, updatable = false)
	private int coopmatricula;
	
	@NotNull
	@Column(name="coop_end_endereco")
	private String coopendendereco;
	
	@Column(name="coop_bairro")
	private String coopbairro;
	
	@Column(name="coop_cidade")
	private String coopcidade;
	
	@Column(name="coop_estado")
	private String coopestado;
	
	@Column(name="coop_cep")
	private String coopcep;
	
	@Column(name="coop_pais")
	private String cooppais;
	
	
	@ManyToOne
	@JoinColumn(name = "coop_matricula")
	private Cooperado cooperado;


	public int getCoopendindexcod() {
		return coopendindexcod;
	}


	public void setCoopendindexcod(int coopendindexcod) {
		this.coopendindexcod = coopendindexcod;
	}


	public int getCoopmatricula() {
		return coopmatricula;
	}


	public void setCoopmatricula(int coopmatricula) {
		this.coopmatricula = coopmatricula;
	}


	public String getCoopendendereco() {
		return coopendendereco;
	}


	public void setCoopendendereco(String coopendendereco) {
		this.coopendendereco = coopendendereco;
	}


	public String getCoopbairro() {
		return coopbairro;
	}


	public void setCoopbairro(String coopbairro) {
		this.coopbairro = coopbairro;
	}


	public String getCoopcidade() {
		return coopcidade;
	}


	public void setCoopcidade(String coopcidade) {
		this.coopcidade = coopcidade;
	}


	public String getCoopestado() {
		return coopestado;
	}


	public void setCoopestado(String coopestado) {
		this.coopestado = coopestado;
	}


	public String getCoopcep() {
		return coopcep;
	}


	public void setCoopcep(String coopcep) {
		this.coopcep = coopcep;
	}


	public String getCooppais() {
		return cooppais;
	}


	public void setCooppais(String cooppais) {
		this.cooppais = cooppais;
	}


	public Cooperado getCooperado() {
		return cooperado;
	}


	public void setCooperado(Cooperado cooperado) {
		this.cooperado = cooperado;
	}
	
	
	

}

