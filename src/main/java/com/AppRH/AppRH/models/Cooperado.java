package com.AppRH.AppRH.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="coop")
public class Cooperado implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="coop_index_cod")
	private Integer coopindexcod;
	
	@Id //(Anotação que identifica que é chave primária)
	@GeneratedValue(strategy = GenerationType.IDENTITY)//(Anotação que faz com que esse campo seja auto increment)
	@NotNull//(Anotação que faz com que esse campo não seja nulo)
	@Column(name="coop_matricula",unique = true)//)(Anotação que indica o nome do campo na tabela no banco de dados)
	private Integer coopmatricula;//(Indica o nome do campo na tabela no banco de dados)
	
	@NotNull
	@Column(name="coop_nome")
	private String coopnome;
	
	@NotNull
	@Column(name="coop_nome_guerra")
	private String coopnomeguerra;
	
	
	@OneToMany(mappedBy = "cooperado", cascade = CascadeType.REMOVE)//(Indica que um cooperado pode ter muitos telefones, 
																	//essa anotação que faz o relacionamento da tabela cooperado com a tabela telefone)
	private List<Telefone> telefones;
	
	@OneToMany(mappedBy = "cooperado", cascade = CascadeType.REMOVE)
	private List<Dividas> dividas;
	
	@OneToMany(mappedBy = "cooperado", cascade = CascadeType.ALL)
	private List<Coopendereco> coopenderecos;
	
	@OneToOne(mappedBy = "cooperado", cascade = CascadeType.ALL)
	private Coopcadastro coopcadastro;
	
	
	//@OneToOne(mappedBy = "cooperado", cascade = CascadeType.REMOVE)
	@OneToOne(mappedBy = "cooperado", cascade = CascadeType.ALL)
	//@JoinColumn(name="coop_matricula", referencedColumnName = "coop_matricula")
	//@OneToOne(cascade = CascadeType.ALL)
	private Lgpd lgpd;
	
	public List<Coopendereco> getCoopendereco() {
		return coopenderecos;
	}


	public void setCoopendereco(List<Coopendereco> coopendereco) {
		this.coopenderecos = coopendereco;
	}


	public List<Dividas> getDividas() {
		return dividas;
	}


	public void setDividas(List<Dividas> dividas) {
		this.dividas = dividas;
	}


	public int getCoopindexcod() {
		return coopindexcod;
	}


	public void setCoopindexcod(int coopindexcod) {
		this.coopindexcod = coopindexcod;
	}


	public Integer getCoopmatricula() {
		return coopmatricula;
	}


	public void setCoopmatricula(Integer coopmatricula) {
		this.coopmatricula = coopmatricula;
	}


	public String getCoopnome() {
		return coopnome;
	}


	public void setCoopnome(String coopnome) {
		this.coopnome = coopnome;
	}


	public String getCoopnomeguerra() {
		return coopnomeguerra;
	}


	public void setCoopnomeguerra(String coopnomeguerra) {
		this.coopnomeguerra = coopnomeguerra;
	}


	public List<Telefone> getTelefones() {
		return telefones;
	}


	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	

	public Coopcadastro getCoopcadastro() {
		return coopcadastro;
	}


	public void setCoopcadastro(Coopcadastro coopcadastro) {
		this.coopcadastro = coopcadastro;
	}


	public Lgpd getLgpd() {
		return lgpd;
	}


	public void setLgpd(Lgpd lgpd) {
		this.lgpd = lgpd;
	}
	
	
}