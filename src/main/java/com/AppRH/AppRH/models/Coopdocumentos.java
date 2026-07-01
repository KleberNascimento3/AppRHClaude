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
@Table(name="coop_documentos")
public class Coopdocumentos implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="coop_doc_index_cod")
    private Long coopdocindexcod;

    @Column(name="coop_matricula", insertable = false, updatable = false)
    private Integer coopmatricula;

    @Column(name="coop_rg")
    private String cooprg;

    @Column(name="coop_rgoem")
    private String cooprgoem;

    @Column(name="coop_rgemis")
    private Date cooprgemis;

    @Column(name="coop_cpf")
    private String coopcpf;

    @Column(name="coop_inss")
    private Integer coopinss;

    @Column(name="coop_passaporte")
    private String cooppassaporte;

    @Column(name="coop_dataexpedicao")
    private Date coopdataexpedicao;

    @Column(name="coop_dt_vencto_pass")
    private Date coopdtvenctopass;

    @Column(name="coop_oem_pass")
    private String coopoempass;

    @Column(name="coop_nr_nie")
    private String coopnrnie;

    @Column(name="coop_nie_vencto")
    private Date coopnievencto;

    @Column(name="coop_eb2")
    private String coopeb2;

    @Column(name="coop_eb2_dtinicio")
    private Date coopeb2dtinicio;

    @Column(name="coop_eb2_dtvencto")
    private Date coopeb2dtvencto;

    @ManyToOne
    @JoinColumn(name = "coop_matricula")
    private Cooperado cooperado;

    public Long getCoopdocindexcod() { return coopdocindexcod; }
    public void setCoopdocindexcod(Long coopdocindexcod) { this.coopdocindexcod = coopdocindexcod; }
    public Integer getCoopmatricula() { return coopmatricula; }
    public void setCoopmatricula(Integer coopmatricula) { this.coopmatricula = coopmatricula; }
    public String getCooprg() { return cooprg; }
    public void setCooprg(String cooprg) { this.cooprg = cooprg; }
    public String getCooprgoem() { return cooprgoem; }
    public void setCooprgoem(String cooprgoem) { this.cooprgoem = cooprgoem; }
    public Date getCooprgemis() { return cooprgemis; }
    public void setCooprgemis(Date cooprgemis) { this.cooprgemis = cooprgemis; }
    public String getCoopcpf() { return coopcpf; }
    public void setCoopcpf(String coopcpf) { this.coopcpf = coopcpf; }
    public Integer getCoopinss() { return coopinss; }
    public void setCoopinss(Integer coopinss) { this.coopinss = coopinss; }
    public String getCooppassaporte() { return cooppassaporte; }
    public void setCooppassaporte(String cooppassaporte) { this.cooppassaporte = cooppassaporte; }
    public Date getCoopdataexpedicao() { return coopdataexpedicao; }
    public void setCoopdataexpedicao(Date coopdataexpedicao) { this.coopdataexpedicao = coopdataexpedicao; }
    public Date getCoopdtvenctopass() { return coopdtvenctopass; }
    public void setCoopdtvenctopass(Date coopdtvenctopass) { this.coopdtvenctopass = coopdtvenctopass; }
    public String getCoopoempass() { return coopoempass; }
    public void setCoopoempass(String coopoempass) { this.coopoempass = coopoempass; }
    public String getCoopnrnie() { return coopnrnie; }
    public void setCoopnrnie(String coopnrnie) { this.coopnrnie = coopnrnie; }
    public Date getCoopnievencto() { return coopnievencto; }
    public void setCoopnievencto(Date coopnievencto) { this.coopnievencto = coopnievencto; }
    public String getCoopeb2() { return coopeb2; }
    public void setCoopeb2(String coopeb2) { this.coopeb2 = coopeb2; }
    public Date getCoopeb2dtinicio() { return coopeb2dtinicio; }
    public void setCoopeb2dtinicio(Date coopeb2dtinicio) { this.coopeb2dtinicio = coopeb2dtinicio; }
    public Date getCoopeb2dtvencto() { return coopeb2dtvencto; }
    public void setCoopeb2dtvencto(Date coopeb2dtvencto) { this.coopeb2dtvencto = coopeb2dtvencto; }
    public Cooperado getCooperado() { return cooperado; }
    public void setCooperado(Cooperado cooperado) { this.cooperado = cooperado; }
}