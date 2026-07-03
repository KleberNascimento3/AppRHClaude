package com.AppRH.AppRH.models;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "interessados")
public class Interessado implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "index_cod")
    private Integer funcproindex;

    @NotBlank
    @Column(name = "nome")
    private String funcpronome;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "datanascimento")
    private LocalDate funcprodatanascimento;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "datacadastro")
    private LocalDate datacadastro;

    @Column(name = "telefoneresidencial")
    private String telefoneresidencial;

    @Column(name = "telefonecelular")
    private String funcprotelefone;

    @Column(name = "telefonecomercial")
    private String telefonecomercial;

    @Column(name = "email")
    private String funcproemail;

    @Column(name = "endereco")
    private String funcproendereco;

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

    @Column(name = "escolaridade")
    private String escolaridade;

    @Column(name = "EstadoCivil")
    private String estadoCivil;

    @Column(name = "Sexo")
    private String sexo;

    @Column(name = "classificacao")
    private String ocupacao;

    @Column(name = "restricao")
    private String restricao;

    @Column(name = "obsrestricao")
    private String obsrestricao;

    public Integer getFuncproindex() { return funcproindex; }
    public void setFuncproindex(Integer funcproindex) { this.funcproindex = funcproindex; }
    public String getFuncpronome() { return funcpronome; }
    public void setFuncpronome(String funcpronome) { this.funcpronome = funcpronome; }
    public LocalDate getFuncprodatanascimento() { return funcprodatanascimento; }
    public void setFuncprodatanascimento(LocalDate funcprodatanascimento) { this.funcprodatanascimento = funcprodatanascimento; }
    public LocalDate getDatacadastro() { return datacadastro; }
    public void setDatacadastro(LocalDate datacadastro) { this.datacadastro = datacadastro; }
    public String getTelefoneresidencial() { return telefoneresidencial; }
    public void setTelefoneresidencial(String telefoneresidencial) { this.telefoneresidencial = telefoneresidencial; }
    public String getFuncprotelefone() { return funcprotelefone; }
    public void setFuncprotelefone(String funcprotelefone) { this.funcprotelefone = funcprotelefone; }
    public String getTelefonecomercial() { return telefonecomercial; }
    public void setTelefonecomercial(String telefonecomercial) { this.telefonecomercial = telefonecomercial; }
    public String getFuncproemail() { return funcproemail; }
    public void setFuncproemail(String funcproemail) { this.funcproemail = funcproemail; }
    public String getFuncproendereco() { return funcproendereco; }
    public void setFuncproendereco(String funcproendereco) { this.funcproendereco = funcproendereco; }
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
    public String getEscolaridade() { return escolaridade; }
    public void setEscolaridade(String escolaridade) { this.escolaridade = escolaridade; }
    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public String getOcupacao() { return ocupacao; }
    public void setOcupacao(String ocupacao) { this.ocupacao = ocupacao; }
    public String getRestricao() { return restricao; }
    public void setRestricao(String restricao) { this.restricao = restricao; }
    public String getObsrestricao() { return obsrestricao; }
    public void setObsrestricao(String obsrestricao) { this.obsrestricao = obsrestricao; }
}
