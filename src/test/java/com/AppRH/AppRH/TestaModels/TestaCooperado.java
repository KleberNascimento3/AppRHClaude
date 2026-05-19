package com.AppRH.AppRH.TestaModels;

import static org.junit.Assert.assertNotNull;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.AppRH.AppRH.models.Autorizacao;
import com.AppRH.AppRH.models.Usuario;
import com.AppRH.AppRH.repository.AutorizacaoRepository;
import com.AppRH.AppRH.repository.UsuarioRepository;


@SpringBootTest
@Transactional
@Rollback
public class TestaCooperado {
	
	@Autowired
	private UsuarioRepository ur;
	
	@Autowired
	private AutorizacaoRepository ar;

	@Test
	void testaInsercao() {
		Usuario usuario = new Usuario();
		usuario.setNome("Kleber");
		usuario.setSenha("$2a$10$i3.Z8Yv1Fwl0I5SNjdCGkOTRGQjGvHjh/gMZhdc3e7LIovAklqM6C");
		usuario.setEmail("kleber@serco.com.br");
		usuario.setAutorizacoes(new HashSet<Autorizacao>());
		Autorizacao aut = new Autorizacao();
		aut.setNome("ROLE_DEVELOPER");
		ar.save(aut);
		usuario.getAutorizacoes().add(aut);
		ur.save(usuario);
		
		assertNotNull(usuario.getAutorizacoes().iterator().next().getId());
		
	}

}

