package com.AppRH.AppRH.models;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

//import java.util.List;
//import javax.persistence.CascadeType;
//import javax.persistence.Id;
//import javax.persistence.OneToMany;
//import javax.persistence.OneToOne;

@Entity
@Table(name="func_pro")
public class Interessado implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="func_pro_index")
	private int funcproindex;
	
	@NotNull
	@Column(name="nome")
	private String funcpronome;
	
	@Column(name="data_nascimento")
	private Date funcprodatanascimento;
	
	@Column(name="telefone")
	private String funcprotelefone;
	
	@Column(name="email")
	private String funcproemail;
	
	@Column(name="endereco")
	private String funcproendereco;
	
	@Column(name="ocupacao")
	private String ocupacao;

	public int getFuncproindex() {
		return funcproindex;
	}

	public void setFuncproindex(int funcproindex) {
		this.funcproindex = funcproindex;
	}

	public String getFuncpronome() {
		return funcpronome;
	}

	public void setFuncpronome(String funcpronome) {
		this.funcpronome = funcpronome;
	}

	public Date getFuncprodatanascimento() {
		return funcprodatanascimento;
	}

	public void setFuncprodatanascimento(Date funcprodatanascimento) {
		this.funcprodatanascimento = funcprodatanascimento;
	}

	public String getFuncprotelefone() {
		return funcprotelefone;
	}

	public void setFuncprotelefone(String funcprotelefone) {
		this.funcprotelefone = funcprotelefone;
	}

	public String getFuncproemail() {
		return funcproemail;
	}

	public void setFuncproemail(String funcproemail) {
		this.funcproemail = funcproemail;
	}

	public String getFuncproendereco() {
		return funcproendereco;
	}

	public void setFuncproendereco(String funcproendereco) {
		this.funcproendereco = funcproendereco;
	}

	public String getOcupacao() {
		return ocupacao;
	}

	public void setOcupacao(String ocupacao) {
		this.ocupacao = ocupacao;
	}
	
	
}
