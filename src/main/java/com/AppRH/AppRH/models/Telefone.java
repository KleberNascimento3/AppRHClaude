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
@Table(name="coop_telefones")
public class Telefone {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="coop_tel_index_cod")
	private int cooptelindexcod;
	
	@NotNull
	@Column(name ="coop_matricula", insertable = false, updatable = false)
	private int coopmatricula;
	
	@Column(name="coop_tel_numero")
	private String cooptelnumero;
	
	@Column(name="coop_tel_tipo")
	private String coopteltipo;
	
	@ManyToOne//(Indica que um cooperado pode ter muitos telefones)
	@JoinColumn(name = "coop_matricula")//")(indica qual o campo que fará a ligação entre as tabelas)
	private Cooperado cooperado;//(Nome da tabela que será relacionada.)

	public int getCooptelindexcod() {
		return cooptelindexcod;
	}

	public void setCooptelindexcod(int cooptelindexcod) {
		this.cooptelindexcod = cooptelindexcod;
	}

	public int getCoopmatricula() {
		return coopmatricula;
	}

	public void setCoopmatricula(int coopmatricula) {
		this.coopmatricula = coopmatricula;
	}

	public String getCooptelnumero() {
		return cooptelnumero;
	}

	public void setCooptelnumero(String cooptelnumero) {
		this.cooptelnumero = cooptelnumero;
	}

	public String getCoopteltipo() {
		return coopteltipo;
	}

	public void setCoopteltipo(String coopteltipo) {
		this.coopteltipo = coopteltipo;
	}

	public Cooperado getCooperado() {
		return cooperado;
	}

	public void setCooperado(Cooperado cooperado) {
		this.cooperado = cooperado;
	}

		

}
