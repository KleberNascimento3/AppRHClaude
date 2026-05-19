package com.AppRH.AppRH.models;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.AppRH.AppRH.controllers.View;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="usr_usuario")
public class Usuario {
    
    @JsonView(View.UsuarioCompleto.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="usr_id")
    private Long id;

    @JsonView({View.UsuarioResumo.class,View.AutorizacaoResumo.class})
    @Column(name="usr_nome")
    private String nome;
    
    @JsonView(View.UsuarioResumo.class)
    @Column(name="usr_email")
    private String email;

    
    @JsonView(View.UsuarioResumo.class)
    @Column(name="usr_senha")
    private String senha;

    @JsonView(View.UsuarioResumo.class)
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="uau_usuario_autorizacao",
        joinColumns = { @JoinColumn(name="usr_id") },
        inverseJoinColumns = { @JoinColumn(name = "aut_id") })
    private Set<Autorizacao> autorizacoes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Set<Autorizacao> getAutorizacoes() {
        return autorizacoes;
    }

    public void setAutorizacoes(Set<Autorizacao> autorizacoes) {
        this.autorizacoes = autorizacoes;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }    
}