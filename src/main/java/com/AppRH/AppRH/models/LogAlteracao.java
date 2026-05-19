package com.AppRH.AppRH.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="coop_log")
public class LogAlteracao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="coop_log_id")
	private Integer id;
    
	@Column(name="coop_tabela")
	private String tabela;
    
	@Column(name="coop_local_data_hora")
	private LocalDateTime data;
	
	@Column(name="coop_operacao")
	private String operacao;
    
	@Column(name="coop_detalhes")
	private String detalhes;
    
    @NotNull
	@Column(name="coop_matricula",insertable = true, updatable = false)
	private int coopmatricula;
    
    @Column(name="coop_usuario")
	private String usuario;

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTabela() {
		return tabela;
	}

	public void setTabela(String tabela) {
		this.tabela = tabela;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	public String getDetalhes() {
		return detalhes;
	}

	public void setDetalhes(String detalhes) {
		this.detalhes = detalhes;
	}

	public int getCoopmatricula() {
		return coopmatricula;
	}

	public void setCoopmatricula(int coopmatricula) {
		this.coopmatricula = coopmatricula;
	}

}
