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
import org.springframework.util.StringUtils;

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

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Usuario criarUsuario(String nome, String senha, String email, String autorizacao) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setSenha(passEncoder.encode(senha));
        usuario.setEmail(email);
        usuario.setAutorizacoes(new HashSet<Autorizacao>());
        usuario.getAutorizacoes().add(buscarOuCriarAutorizacao(autorizacao));
        ur.save(usuario);
        return usuario;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Usuario atualizarUsuario(Long id, String nome, String email, String senha, String autorizacao) {
        Usuario usuario = buscarUsuarioPorId(id);
        usuario.setNome(nome);
        usuario.setEmail(email);
        if (StringUtils.hasText(senha)) {
            usuario.setSenha(passEncoder.encode(senha));
        }
        usuario.setAutorizacoes(new HashSet<Autorizacao>());
        usuario.getAutorizacoes().add(buscarOuCriarAutorizacao(autorizacao));
        return ur.save(usuario);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void alterarSenhaUsuario(Long id, String senha) {
        Usuario usuario = buscarUsuarioPorId(id);
        usuario.setSenha(passEncoder.encode(senha));
        ur.save(usuario);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void excluirUsuario(Long id) {
        Usuario usuario = buscarUsuarioPorId(id);
        boolean usuarioAdmin = usuario.getAutorizacoes().stream()
                .anyMatch(autorizacao -> "ROLE_ADMIN".equals(autorizacao.getNome()));
        if (usuarioAdmin && ur.countByAutorizacoesNome("ROLE_ADMIN") <= 1) {
            throw new IllegalStateException("Nao e possivel excluir o ultimo administrador do sistema.");
        }
        ur.delete(usuario);
    }

    private Autorizacao buscarOuCriarAutorizacao(String autorizacao) {
        String nomeAutorizacao = StringUtils.hasText(autorizacao) ? autorizacao : "ROLE_USUARIO";
        Autorizacao aut = ar.findByNome(nomeAutorizacao);
        if(aut == null) {
            aut = new Autorizacao();
            aut.setNome(nomeAutorizacao);
            ar.save(aut);
        }
        return aut;
    }
    
    @Override
    @PreAuthorize("hasRole('ADMIN')")
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
        throw new RegistroNaoEncontradoException("Usuario nao encontrado!");              
    }
    
    @Override
    public Usuario buscarUsuarioPorNome(String nome) {
        Usuario usuario = ur.findByNome(nome);
        if(usuario != null) {
            return usuario;
        }
        throw new RegistroNaoEncontradoException("Usuario nao encontrado!");
        
    }
    
    @Override
    public Autorizacao buscarAutorizacaoPorNome(String nome) {
        Autorizacao autorizacao = ar.findByNome(nome);
        if(autorizacao != null) {
            return autorizacao;
        }
        throw new RegistroNaoEncontradoException("Autorizacao nao encontrada!");
        
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<Autorizacao> buscarTodasAutorizacoes() {
        return ar.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = ur.findByNome(username);
        if(usuario==null) {
            throw new UsernameNotFoundException("Usuario " + username + " nao encontrado!");
        }
        return User.builder().username(username).password(usuario.getSenha())
                .authorities(usuario.getAutorizacoes().stream()
                        .map(Autorizacao::getNome).collect(Collectors.toList())
                        .toArray(new String[usuario.getAutorizacoes().size()]))
                    .build();
    }

}
