package com.AppRH.AppRH.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

//import com.AppRH.AppRH.models.Cooperado;
//import com.AppRH.AppRH.models.Telefone;
//import com.AppRH.AppRH.models.Funcionario;
//import com.AppRH.AppRH.models.Dependentes;

import com.AppRH.AppRH.repository.CooperadoRepository;
//import com.AppRH.AppRH.repository.TelefoneRepository;


@Controller
public class BuscaController {
	
	@Autowired
	private CooperadoRepository cr;
	
//	@Autowired
	//private TelefoneRepository tr;
	
	//Get
	@RequestMapping(value = "/listaCooperados", method = RequestMethod.GET)
	public ModelAndView abrirLista() {
		System.out.print("kleberZ");
		ModelAndView mv = new ModelAndView("index");
		return mv;
	}
	
	//Post
	@RequestMapping(value = "/listaCooperados", method = RequestMethod.POST)
	public ModelAndView buscarLista(@RequestParam("buscar") String buscar, @RequestParam("coopnome") String coopnome) {
		
		ModelAndView mv = new ModelAndView("index");
		String mensagem = "Resultados da busca por " + buscar;
		
		System.out.print("klebery");
		
		if(coopnome.equals("all")){
			mv.addObject("cooperados",cr.findByCoopnomesCooperados(buscar));
		}else if(coopnome.equals("ativos")) {
			//mv.addObject("cooperados",cr.encontrarAtivos());
		}else if(coopnome.equals("inativos")) {
		//	mv.addObject("cooperados",cr.findByCoopnomesCooperado(buscar));
		}else if(coopnome.equals("nometelefone")) {
		//	mv.addObject("cooperados",cr.findByCoopnomesCooperado(buscar));
		}else {
		//	mv.addObject("cooperados",cr.findByCoopnomesCooperado(buscar));
		}
		
		mv.addObject("mensagem", mensagem);
		
		return mv;	
	}
	
	//Get
		@RequestMapping(value = "/", method = RequestMethod.GET)
		public ModelAndView abrirIndex() {
			System.out.print("kleber");
			ModelAndView mv = new ModelAndView("index");
			return mv;
		}
		
		//Post
		@RequestMapping(value = "/", method = RequestMethod.POST)
		public ModelAndView buscarIndex(@RequestParam("buscar") String buscar, @RequestParam("coopnome") String coopnome) {
			
			ModelAndView mv = new ModelAndView("index");
			String mensagem = "Resultados da busca por " + buscar;
			System.out.print("kleberw");
			
			if(coopnome.equals("nomecooperado")){
				mv.addObject("cooperados",cr.findByCoopnomesCooperado(buscar));
			}else if(coopnome.equals("nomedependente")) {
				mv.addObject("cooperados",cr.findByCoopnomesCooperado(buscar));
			}else if(coopnome.equals("nomefuncionario")) {
				mv.addObject("cooperados",cr.findByCoopnomesCooperado(buscar));
			}else if(coopnome.equals("nometelefone")) {
				mv.addObject("cooperados",cr.findByCoopnomesCooperado(buscar));
			}else {
				mv.addObject("cooperados",cr.findByCoopnomesCooperado(buscar));
			}
			
			mv.addObject("mensagem", mensagem);
			
			return mv;	
		}
}