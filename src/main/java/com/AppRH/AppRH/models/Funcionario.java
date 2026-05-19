package com.AppRH.AppRH.models;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="func")
public class Funcionario implements Serializable{
	
private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="func_id")
	private int funcid;
	
	@Id
	@NotNull
	@Column(name="func_matricula",unique = true)
	private int funcmatricula;
	
	@Column(name="func_nome")
	private String funcnome;
	
	@Column(name="func_data")
	private Date funcdata;
	
	@Column(name="func_email")
	private String funcemail;
	
	@OneToMany(mappedBy = "funcionario", cascade = CascadeType.REMOVE)
	private List<Dependentes>dependentes;

		public int getFuncid() {
		return funcid;
	}

	public void setFuncid(int funcid) {
		this.funcid = funcid;
	}

	public String getFuncnome() {
		return funcnome;
	}

	public void setFuncnome(String funcnome) {
		this.funcnome = funcnome;
	}

	public Date getFuncdata() {
		return funcdata;
	}

	public void setFuncdata(Date funcdata) {
		this.funcdata = funcdata;
	}

	public String getFuncemail() {
		return funcemail;
	}

	public void setFuncemail(String funcemail) {
		this.funcemail = funcemail;
	}
	
	

	public int getFuncmatricula() {
		return funcmatricula;
	}

	public void setFuncmatricula(int funcmatricula) {
		this.funcmatricula = funcmatricula;
	}

	public void setDependentes(List<Dependentes> dependentes) {
		this.dependentes = dependentes;
	}

		public List<Dependentes> getDependentes() {
		return dependentes;
	}

		
}
