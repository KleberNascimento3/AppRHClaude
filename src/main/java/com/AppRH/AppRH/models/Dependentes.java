package com.AppRH.AppRH.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="func_dep")
public class Dependentes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="func_dep_id")
	private int funcdepid;
	
	@Column(name="func_dep_cpf",unique = true)
	private String funcdepcpf;
	
	@Column(name="func_dep_nome")
	private String funcdepnome;
	
	
	@Column(name="func_dep_data")
	private Date funcdepdata;
	
	
	public int getFuncdepid() {
		return funcdepid;
	}

	public void setFuncdepid(int funcdepid) {
		this.funcdepid = funcdepid;
	}

	public String getFuncdepcpf() {
		return funcdepcpf;
	}

	public void setFuncdepcpf(String funcdepcpf) {
		this.funcdepcpf = funcdepcpf;
	}

	public String getFuncdepnome() {
		return funcdepnome;
	}

	public void setFuncdepnome(String funcdepnome) {
		this.funcdepnome = funcdepnome;
	}
	
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	

	public Date getFuncdepdata() {
		return funcdepdata;
	}

	public void setFuncdepdata(Date funcdepdata) {
		this.funcdepdata = funcdepdata;
	}


	
	@ManyToOne
	@JoinColumn(name = "func_matricula")
	private Funcionario funcionario;

		
	
}