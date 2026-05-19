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
@Table(name="func_telefones")
public class FuncTelefone {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="func_tel_index_cod")
	private int functelindexcod;
	
	@NotNull
	@Column(name ="func_matricula", insertable = false, updatable = false)
	private int funcmatricula;
	
	@Column(name="func_tel_numero")
	private String cooptelnumero;
	
	@Column(name="func_tel_tipo")
	private String functeltipo;
	
	@ManyToOne//(Indica que um cooperado pode ter muitos telefones)
	@JoinColumn(name = "func_matricula")//")(indica qual o campo que fará a ligação entre as tabelas)
	private Funcionario funcionario;//(Nome da tabela que será relacionada.)

}
