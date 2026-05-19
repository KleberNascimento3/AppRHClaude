package com.AppRH.AppRH.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.AppRH.AppRH.exception.RegistroNaoEncontradoException;
import com.AppRH.AppRH.models.Autorizacao;
import com.AppRH.AppRH.models.Usuario;
import com.AppRH.AppRH.repository.AutorizacaoRepository;
import com.AppRH.AppRH.repository.UsuarioRepository;

@Service("segurancaService")
public class SegurancaServiceImpl implements SegurancaService{
	
	@Autowired
	private AutorizacaoRepository ar;
	
	@Autowired
	private UsuarioRepository ur;
	
	@Autowired
	private PasswordEncoder passEncoder;

	@Transactional
	public Usuario criarUsuario(String nome, String senha, String email, String autorizacao) {
		Autorizacao aut = ar.findByNome(autorizacao);
		if(aut == null) {
			aut = new Autorizacao();
			aut.setNome(autorizacao);
			ar.save(aut);
		}
		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		usuario.setSenha(passEncoder.encode(senha));
		usuario.setEmail(email);
		usuario.setAutorizacoes(new HashSet<Autorizacao>());
		usuario.getAutorizacoes().add(aut);
		ur.save(usuario);
		return usuario;
	}
	
	@Override
	@PreAuthorize("isAuthenticated")
	public List<Usuario> buscarTodosUsuarios(){
		return ur.findAll();
	}
	
	@Override
	@PreAuthorize("hasAnyRole('ADMIN','USUARIO')")
	public Usuario buscarUsuarioPorId(Long id){
		Optional<Usuario> usuarioOp = ur.findById(id);
		if(usuarioOp.isPresent()) {
			return usuarioOp.get();
		}
		throw new RegistroNaoEncontradoException("Usuário não encontrado!");				
	}
	
	@Override
	public Usuario buscarUsuarioPorNome(String nome) {
		Usuario usuario = ur.findByNome(nome);
		if(usuario != null) {
			return usuario;
		}
		throw new RegistroNaoEncontradoException("Usuario não encontrado!");
		
	}
	
	@Override
	public Autorizacao buscarAutorizacaoPorNome(String nome) {
		Autorizacao autorizacao = ar.findByNome(nome);
		if(autorizacao != null) {
			return autorizacao;
		}
		throw new RegistroNaoEncontradoException("Autorização não encontrada!");
		
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = ur.findByNome(username);
		if(usuario==null) {
			throw new UsernameNotFoundException("Usuário " + username + " não encontrado!");
		}
		return User.builder().username(username).password(usuario.getSenha())
                .authorities(usuario.getAutorizacoes().stream()
                        .map(Autorizacao::getNome).collect(Collectors.toList())
                        .toArray(new String[usuario.getAutorizacoes().size()]))
                    .build();
	}

}
