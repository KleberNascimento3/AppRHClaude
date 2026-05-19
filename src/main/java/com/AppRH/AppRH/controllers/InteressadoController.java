package com.AppRH.AppRH.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.AppRH.AppRH.models.Interessado;
import com.AppRH.AppRH.repository.InteressadoRepository;

public class InteressadoController {
	
	@Autowired
	private InteressadoRepository ir;
	
	//LISTAR COOPERADOS
		@PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER')")
		@GetMapping("/interessados")		
		public ModelAndView listaInteressados() {
			ModelAndView mv = new ModelAndView("interessado/listaInteressados");
			Iterable<Interessado> interessadosIterable = ir.findAll(); // Recupera os funcionários como Iterable
			List<Interessado> interessados = new ArrayList<>(); // Converte Iterable para List
			interessadosIterable.forEach(interessados::add); // Adiciona os funcionários à lista
		 	mv.addObject("interessados",interessados);
			return mv;
		}    
		 

}
