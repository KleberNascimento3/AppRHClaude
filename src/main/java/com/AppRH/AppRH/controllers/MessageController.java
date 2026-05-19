package com.AppRH.AppRH.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MessageController {
	@GetMapping("/acesso-negado")
    public ModelAndView acessoNegado() {
		ModelAndView mv = new ModelAndView("mensagens/acesso-negado");
		return mv;
    }

}
