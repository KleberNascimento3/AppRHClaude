package com.AppRH.AppRH.models;

import java.io.Serializable;
import java.sql.Date;

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
@Table(name="coop_cota_parte")
public class Cotaparte implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="coop_index_cod")
	private int coopindexcod;
	
	@NotNull
	@Column(name="coop_matricula",insertable = false, updatable = false)
	private int coopmatricula;
	
	@Column(name="coop_descricao")
	private String coopdescricao;
	
	@Column(name="coop_valor")
	private float coopvalor;
	
	@Column(name="coop_formapagto")
	private String coopformapagto;
	
	@Column(name="coop_datavencimento")
	private Date coopdatavencimento;
	
	@Column(name="coop_datapagamento")
	private Date coopdatapagamento;
	
	@Column(name="coop_flagcotaparte")
	private Integer coopflagcotaparte;
	
	@ManyToOne
	@JoinColumn(name = "coop_matricula")
	private Cooperado cooperado;

	public Cooperado getCooperado() {
		return cooperado;
	}

	public void setCooperado(Cooperado cooperado) {
		this.cooperado = cooperado;
	}

	public int getCoopindexcod() {
		return coopindexcod;
	}

	public void setCoopindexcod(int coopindexcod) {
		this.coopindexcod = coopindexcod;
	}

	public int getCoopmatricula() {
		return coopmatricula;
	}

	public void setCoopmatricula(int coopmatricula) {
		this.coopmatricula = coopmatricula;
	}

	public String getCoopdescricao() {
		return coopdescricao;
	}

	public void setCoopdescricao(String coopdescricao) {
		this.coopdescricao = coopdescricao;
	}

	public String getCoopformapagto() {
		return coopformapagto;
	}

	public void setCoopformapagto(String coopformapagto) {
		this.coopformapagto = coopformapagto;
	}

	public Date getCoopdatavencimento() {
		return coopdatavencimento;
	}

	public void setCoopdatavencimento(Date coopdatavencimento) {
		this.coopdatavencimento = coopdatavencimento;
	}

	public Date getCoopdatapagamento() {
		return coopdatapagamento;
	}

	public void setCoopdatapagamento(Date coopdatapagamento) {
		this.coopdatapagamento = coopdatapagamento;
	}

	public int getCoopflagcotaparte() {
		return coopflagcotaparte;
	}

	public void setCoopflagcotaparte(int coopflagcotaparte) {
		this.coopflagcotaparte = coopflagcotaparte;
	}

	public float getCoopvalor() {
		return coopvalor;
	}

	public void setCoopvalor(float coopvalor) {
		this.coopvalor = coopvalor;
	}

	public void setCoopflagcotaparte(Integer coopflagcotaparte) {
		this.coopflagcotaparte = coopflagcotaparte;
	}
	
	
	

}
