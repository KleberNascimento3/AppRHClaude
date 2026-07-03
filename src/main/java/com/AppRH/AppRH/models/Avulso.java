package com.AppRH.AppRH.models;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "avulsos")
public class Avulso implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "index_cod")
    private Integer id;

    @Column(name = "matricula")
    private Integer matricula;

    @Column(name = "nome")
    private String nome;

    @Column(name = "telefonecelular")
    private String telefonecelular;

    @Column(name = "telefoneresidencial")
    private String telefoneresidencial;

    @Column(name = "telefonecomercial")
    private String telefonecomercial;

    @Column(name = "email")
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "datanascimento")
    private LocalDate datanascimento;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "datacadastro")
    private LocalDate datacadastro;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dataadmissao")
    private LocalDate dataadmissao;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "datademissao")
    private LocalDate datademissao;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado")
    private String estado;

    @Column(name = "cep")
    private String cep;

    @Column(name = "pais")
    private String pais;

    @Column(name = "RG")
    private String rg;

    @Column(name = "CPF")
    private String cpf;

    @Column(name = "profissional")
    private String profissional;

    @Column(name = "funcao")
    private String funcao;

    @Column(name = "cooperado")
    private String cooperado;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getMatricula() { return matricula; }
    public void setMatricula(Integer matricula) { this.matricula = matricula; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTelefonecelular() { return telefonecelular; }
    public void setTelefonecelular(String telefonecelular) { this.telefonecelular = telefonecelular; }
    public String getTelefoneresidencial() { return telefoneresidencial; }
    public void setTelefoneresidencial(String telefoneresidencial) { this.telefoneresidencial = telefoneresidencial; }
    public String getTelefonecomercial() { return telefonecomercial; }
    public void setTelefonecomercial(String telefonecomercial) { this.telefonecomercial = telefonecomercial; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getDatanascimento() { return datanascimento; }
    public void setDatanascimento(LocalDate datanascimento) { this.datanascimento = datanascimento; }
    public LocalDate getDatacadastro() { return datacadastro; }
    public void setDatacadastro(LocalDate datacadastro) { this.datacadastro = datacadastro; }
    public LocalDate getDataadmissao() { return dataadmissao; }
    public void setDataadmissao(LocalDate dataadmissao) { this.dataadmissao = dataadmissao; }
    public LocalDate getDatademissao() { return datademissao; }
    public void setDatademissao(LocalDate datademissao) { this.datademissao = datademissao; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getProfissional() { return profissional; }
    public void setProfissional(String profissional) { this.profissional = profissional; }
    public String getFuncao() { return funcao; }
    public void setFuncao(String funcao) { this.funcao = funcao; }
    public String getCooperado() { return cooperado; }
    public void setCooperado(String cooperado) { this.cooperado = cooperado; }
}
