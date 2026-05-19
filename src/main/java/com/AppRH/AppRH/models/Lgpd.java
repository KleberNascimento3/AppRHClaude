package com.AppRH.AppRH.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="coop_lgpd")
public class Lgpd implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="coop_lgpd_index_cod")
	private int cooplgpdindexcod;
	
	@NotNull
	@Column(name="coop_matricula",insertable = false, updatable = false)
	//@Column(name="coop_matricula")
	private int coopmatricula;
	
	@Column(name="coop_ligacao")
	private String coopligacao;
	
	@Column(name="coop_whatsapp")
	private String coopwhatsapp;
	
	@Column(name="coop_correio")
	private String coopcorreio;
	
	@Column(name="coop_nenhum")
	private String coopnenhum;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "coop_matricula")
	private Cooperado cooperado;

	public int getCooplgpdindexcod() {
		return cooplgpdindexcod;
	}

	public void setCooplgpdindexcod(int cooplgpdindexcod) {
		this.cooplgpdindexcod = cooplgpdindexcod;
	}

	public int getCoopmatricula() {
		return coopmatricula;
	}

	public void setCoopmatricula(int coopmatricula) {
		this.coopmatricula = coopmatricula;
	}

	public String getCoopligacao() {
		return coopligacao;
	}
	
	public Boolean isCoopligacao() {
		return this.coopligacao.equals("SIM");
	}

	public void setCoopligacao(String coopligacao) {
		this.coopligacao = coopligacao;
	}

	public String getCoopwhatsapp() {
		return coopwhatsapp;
	}
	
	public Boolean isCoopwhatsapp() {
		return this.coopwhatsapp.equals("SIM");
	}

	public void setCoopwhatsapp(String coopwhatsapp) {
		this.coopwhatsapp = coopwhatsapp;
	}

	public String getCoopcorreio() {
		return coopcorreio;
	}
	
	public Boolean isCoopcorreio() {
		return this.coopcorreio.equals("SIM");
	}

	public void setCoopcorreio(String coopcorreio) {
		this.coopcorreio = coopcorreio;
	}

	public String getCoopnenhum() {
		return coopnenhum;
	}

	public void setCoopnenhum(String coopnenhum) {
		this.coopnenhum = coopnenhum;
	}

	public Cooperado getCooperado() {
		return cooperado;
	}

	public void setCooperado(Cooperado cooperado) {
		this.cooperado = cooperado;
	}	
	
}

