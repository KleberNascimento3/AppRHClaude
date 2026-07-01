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

@Entity
@Table(name="coop_dados_pessoais")
public class Coopdadospessoais implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="coop_dados_index_cod")
    private Long coopdadosindexcod;

    @Column(name="coop_matricula", insertable = false, updatable = false)
    private Integer coopmatricula;

    @Column(name="coop_pai")
    private String cooppai;

    @Column(name="coop_mae")
    private String coopmae;

    @Column(name="coop_conjuge")
    private String coopconjuge;

    @Column(name="coop_num_filhos")
    private Integer coopnumfilhos;

    @Column(name="coop_local_nasc")
    private String cooplocalnasc;

    @Column(name="coop_data_nasc")
    private Date coopdatanasc;

    @Column(name="coop_foto")
    private byte[] coopfoto;

    @Column(name="coop_escolaridade")
    private String coopescolaridade;

    @Column(name="coop_estado_civil")
    private String coopestadocivil;

    @Column(name="coop_sexo")
    private String coopsexo;

    @Column(name="coop_nacionalidade")
    private String coopnacionalidade;

    @Column(name="coop_email")
    private String coopemail;

    @Column(name="coop_nr_dep")
    private Integer coopnrdep;

    @Column(name="coop_aniversario")
    private String coopaniversario;

    @Column(name="coop_idade")
    private Integer coopidade;

    @Column(name="coop_aposentado")
    private String coopaposentado;

    @Column(name="coop_beneficio")
    private String coopbeneficio;

    @Column(name="coop_dep_ir")
    private Integer coopdepir;

    @Column(name="coop_issqn")
    private String coopissqn;

    @ManyToOne
    @JoinColumn(name = "coop_matricula")
    private Cooperado cooperado;

    public Long getCoopdadosindexcod() { return coopdadosindexcod; }
    public void setCoopdadosindexcod(Long coopdadosindexcod) { this.coopdadosindexcod = coopdadosindexcod; }
    public Integer getCoopmatricula() { return coopmatricula; }
    public void setCoopmatricula(Integer coopmatricula) { this.coopmatricula = coopmatricula; }
    public String getCooppai() { return cooppai; }
    public void setCooppai(String cooppai) { this.cooppai = cooppai; }
    public String getCoopmae() { return coopmae; }
    public void setCoopmae(String coopmae) { this.coopmae = coopmae; }
    public String getCoopconjuge() { return coopconjuge; }
    public void setCoopconjuge(String coopconjuge) { this.coopconjuge = coopconjuge; }
    public Integer getCoopnumfilhos() { return coopnumfilhos; }
    public void setCoopnumfilhos(Integer coopnumfilhos) { this.coopnumfilhos = coopnumfilhos; }
    public String getCooplocalnasc() { return cooplocalnasc; }
    public void setCooplocalnasc(String cooplocalnasc) { this.cooplocalnasc = cooplocalnasc; }
    public Date getCoopdatanasc() { return coopdatanasc; }
    public void setCoopdatanasc(Date coopdatanasc) { this.coopdatanasc = coopdatanasc; }
    public byte[] getCoopfoto() { return coopfoto; }
    public void setCoopfoto(byte[] coopfoto) { this.coopfoto = coopfoto; }
    public String getCoopescolaridade() { return coopescolaridade; }
    public void setCoopescolaridade(String coopescolaridade) { this.coopescolaridade = coopescolaridade; }
    public String getCoopestadocivil() { return coopestadocivil; }
    public void setCoopestadocivil(String coopestadocivil) { this.coopestadocivil = coopestadocivil; }
    public String getCoopsexo() { return coopsexo; }
    public void setCoopsexo(String coopsexo) { this.coopsexo = coopsexo; }
    public String getCoopnacionalidade() { return coopnacionalidade; }
    public void setCoopnacionalidade(String coopnacionalidade) { this.coopnacionalidade = coopnacionalidade; }
    public String getCoopemail() { return coopemail; }
    public void setCoopemail(String coopemail) { this.coopemail = coopemail; }
    public Integer getCoopnrdep() { return coopnrdep; }
    public void setCoopnrdep(Integer coopnrdep) { this.coopnrdep = coopnrdep; }
    public String getCoopaniversario() { return coopaniversario; }
    public void setCoopaniversario(String coopaniversario) { this.coopaniversario = coopaniversario; }
    public Integer getCoopidade() { return coopidade; }
    public void setCoopidade(Integer coopidade) { this.coopidade = coopidade; }
    public String getCoopaposentado() { return coopaposentado; }
    public void setCoopaposentado(String coopaposentado) { this.coopaposentado = coopaposentado; }
    public String getCoopbeneficio() { return coopbeneficio; }
    public void setCoopbeneficio(String coopbeneficio) { this.coopbeneficio = coopbeneficio; }
    public Integer getCoopdepir() { return coopdepir; }
    public void setCoopdepir(Integer coopdepir) { this.coopdepir = coopdepir; }
    public String getCoopissqn() { return coopissqn; }
    public void setCoopissqn(String coopissqn) { this.coopissqn = coopissqn; }
    public Cooperado getCooperado() { return cooperado; }
    public void setCooperado(Cooperado cooperado) { this.cooperado = cooperado; }
}