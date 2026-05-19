package com.AppRH.AppRH.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
	
	@Autowired
	private UserDetailsService userDetailsService;

 	@GetMapping("/user")
    @ResponseBody
    public String getCurrentUser() {
 		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

 	    // Obtém todas as autorizações associadas ao usuário autenticado
 	    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

 	    // Percorre as autorizações para obter os nomes das roles
 	    StringBuilder roles = new StringBuilder();
 	    for (GrantedAuthority authority : authorities) {
 	        roles.append(authority.getAuthority()).append(", ");
 	    }

 	    // Remove a vírgula e o espaço extras no final da string
 	    if (roles.length() > 2) {
 	        roles.setLength(roles.length() - 2);
 	    }

 	    return "Usuário logado: " + authentication.getName() + ", Tipo de autorização: " + roles.toString();
    }
 	
 	/*@GetMapping("/user")
    @ResponseBody
    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return "Usuário logado: " + username;
    }
	*/
	@GetMapping("/trocar-usuario/{nome}")
    public String trocarUsuario(@PathVariable("nome") String nome) {
        try {
            // Obter o UserDetails do novo usuário
            UserDetails newUserDetails = userDetailsService.loadUserByUsername(nome);

            // Criar uma nova autenticação com o novo UserDetails e as credenciais antigas (senha)
            Authentication newAuthentication = new UsernamePasswordAuthenticationToken(newUserDetails,
                    SecurityContextHolder.getContext().getAuthentication().getCredentials(),
                    newUserDetails.getAuthorities());

            // Definir a nova autenticação no contexto de segurança
            SecurityContextHolder.getContext().setAuthentication(newAuthentication);
        } catch (UsernameNotFoundException e) {
            // Lidar com o cenário em que o novo usuário não foi encontrado
            // Aqui, você pode redirecionar para uma página de erro ou tomar outra ação apropriada
        }

        return "redirect:/";
    }

}
