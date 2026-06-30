package com.AppRH.AppRH.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AppRH.AppRH.service.SegurancaService;

@Controller
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasAnyRole('ADMIN', 'DESENVOLVEDOR', 'DEVELOPER')")
public class AdminUsuarioController {

    @Autowired
    private SegurancaService segurancaService;

    @GetMapping
    public ModelAndView listarUsuarios() {
        ModelAndView mv = new ModelAndView("admin/usuarios");
        mv.addObject("usuarios", segurancaService.buscarTodosUsuarios());
        mv.addObject("autorizacoes", segurancaService.buscarTodasAutorizacoes());
        return mv;
    }

    @PostMapping
    public String salvarUsuario(@RequestParam(value = "id", required = false) Long id,
            @RequestParam("nome") String nome,
            @RequestParam("email") String email,
            @RequestParam(value = "senha", required = false) String senha,
            @RequestParam("autorizacao") String autorizacao,
            RedirectAttributes attributes) {
        try {
            if (id == null) {
                segurancaService.criarUsuario(nome, senha, email, autorizacao);
                attributes.addFlashAttribute("mensagem", "Usuario cadastrado com sucesso.");
            } else {
                segurancaService.atualizarUsuario(id, nome, email, senha, autorizacao);
                attributes.addFlashAttribute("mensagem", "Usuario atualizado com sucesso.");
            }
        } catch (Exception ex) {
            attributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/{id}/senha")
    public String alterarSenha(@PathVariable("id") Long id,
            @RequestParam("senha") String senha,
            RedirectAttributes attributes) {
        try {
            segurancaService.alterarSenhaUsuario(id, senha);
            attributes.addFlashAttribute("mensagem", "Senha alterada com sucesso.");
        } catch (Exception ex) {
            attributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/{id}/excluir")
    public String excluirUsuario(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            segurancaService.excluirUsuario(id);
            attributes.addFlashAttribute("mensagem", "Usuario excluido com sucesso.");
        } catch (Exception ex) {
            attributes.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/admin/usuarios";
    }
}
