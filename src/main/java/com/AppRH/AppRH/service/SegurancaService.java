package com.AppRH.AppRH.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.AppRH.AppRH.models.Autorizacao;
import com.AppRH.AppRH.models.Usuario;

public interface SegurancaService extends UserDetailsService {
	
	public Usuario criarUsuario(String nome, String senha, String email, String autorizacao);
	
	public List<Usuario> buscarTodosUsuarios();
	
	public Usuario buscarUsuarioPorId(Long id);
	
	public Usuario buscarUsuarioPorNome(String nome);
	
	public Autorizacao buscarAutorizacaoPorNome(String nome);
	

}
