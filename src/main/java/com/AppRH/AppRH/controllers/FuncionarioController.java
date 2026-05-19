package com.AppRH.AppRH.controllers;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AppRH.AppRH.models.Dependentes;
import com.AppRH.AppRH.models.Funcionario;
import com.AppRH.AppRH.models.LogAlteracao;
import com.AppRH.AppRH.repository.DependenteRepository;
import com.AppRH.AppRH.repository.FuncionarioRepository;
import com.AppRH.AppRH.repository.LogAlteracaoRepository;

@Controller
public class FuncionarioController {

	private static final Logger log = LoggerFactory.getLogger(FuncionarioController.class);

	@Autowired
	private FuncionarioRepository fr;

	@Autowired
	private DependenteRepository dr;
	
	@Autowired
    private LogAlteracaoRepository la;

	// GET que chama o form para cadastrar funcionários
	@PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
	@GetMapping("/cadastrarFuncionario")
	public String form() {
		return "funcionario/formFuncionario";
	}

	// POST que cadastra funcionários
	@PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
	@PostMapping(value = "/cadastrarFuncionario")
	public String form(@Valid Funcionario funcionario, BindingResult result, RedirectAttributes attributes) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos");
			return "redirect:/cadastrarFuncionario";
		}
		
		Integer maior =fr.achaMaior();
		log.debug("Cadastrando funcionário, próxima matrícula: {}", maior);
		     
	     funcionario.setFuncmatricula(maior+1);
	     funcionario.setFuncnome(funcionario.getFuncnome().toUpperCase());
	     funcionario.setFuncdata(funcionario.getFuncdata());
	     funcionario.setFuncemail(funcionario.getFuncemail());     
	        

		fr.save(funcionario);
		
		//SALVANDO UM LOG DE INCLUSÃO
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 String username = authentication.getName();
		 
		 
		 LogAlteracao lalte= new LogAlteracao();
		 lalte.setData(LocalDateTime.now());
		 lalte.setTabela("Funcionário");
		 lalte.setOperacao("Inclusão");
		 lalte.setDetalhes("Funcionário Inserido");
		 lalte.setCoopmatricula(funcionario.getFuncmatricula());
		 lalte.setUsuario(username);
	     la.save(lalte);
	     
		attributes.addFlashAttribute("mensagem", "Funcionário cadastrado com sucesso!");
		return "redirect:/cadastrarFuncionario";
	}

	// GET que lista funcionários
	@PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER', 'USUARIO')")
	@GetMapping("/funcionarios")
	public ModelAndView listaFuncionarios() {
		ModelAndView mv = new ModelAndView("funcionario/listaFuncionario");
		Iterable<Funcionario> funcionariosIterable = fr.findAll(); // Recupera os funcionários como Iterable
		List<Funcionario> funcionarios = new ArrayList<>(); // Converte Iterable para List
	    
	    funcionariosIterable.forEach(funcionarios::add); // Adiciona os funcionários à lista
	    
	    // Ordena a lista manualmente pela matrícula
	    funcionarios.sort(Comparator.comparing(Funcionario::getFuncmatricula));
		mv.addObject("funcionarios", funcionarios);
		return mv;
	}

	// GET que lista dependentes e detalhes dos funcionário
	@PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER', 'USUARIO')")
	@GetMapping("/detalhes-funcionario/{id}")
	public ModelAndView detalhesFuncionario(@PathVariable("funcmatricula") int funcmatricula) {
		Funcionario funcionario = fr.findByFuncmatricula(funcmatricula);
		ModelAndView mv = new ModelAndView("funcionario/detalhes-funcionario");
		mv.addObject("funcionarios", funcionario);

		// lista de dependentes baseada no id do funcionário
		Iterable<Dependentes> dependentes = dr.findByFuncionario(funcionario);
		mv.addObject("dependentes", dependentes);

		return mv;

	}

	// POST que adiciona dependentes
	@PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
	@PostMapping(value="/detalhes-funcionario/{id}")
	public String detalhesFuncionarioPost(@PathVariable("id") long id, Dependentes dependentes, BindingResult result,
			RedirectAttributes attributes) {
		
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/detalhes-funcionario/{id}";
		}
		
		if(dr.findByFuncdepcpf(dependentes.getFuncdepcpf()) != null) {
			attributes.addFlashAttribute("mensagem_erro", "CPF duplicado");
			return "redirect:/detalhes-funcionario/{id}";
		}
		
		Funcionario funcionario = fr.findById(id);
		dependentes.setFuncionario(funcionario);
		dr.save(dependentes);
		attributes.addFlashAttribute("mensagem", "Dependente adicionado com sucesso");
		return "redirect:/detalhes-funcionario/{id}";
		
	}
	
	//GET que deleta funcionário
	@PreAuthorize("hasAnyRole('DEVELOPER')")
	@GetMapping("/deletarFuncionario")
	public String deletarFuncionario(int funcmatricula) {
		Funcionario funcionario = fr.findByFuncmatricula(funcmatricula);
		fr.delete(funcionario);
		
		//SALVANDO UM LOG DE INCLUSÃO
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 String username = authentication.getName();
		 
		 
		 LogAlteracao lalte= new LogAlteracao();
		 lalte.setData(LocalDateTime.now());
		 lalte.setTabela("Funcionário");
		 lalte.setOperacao("Deleção");
		 lalte.setDetalhes("Funcionário Deletado");
		 lalte.setCoopmatricula(funcionario.getFuncmatricula());
		 lalte.setUsuario(username);
	     la.save(lalte);
	     
		return "redirect:/funcionarios";
		
	}
	
	
	// Métodos que atualizam funcionário
	// GET que chama o FORM de edição do funcionário
	@PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
	@GetMapping("/editar-funcionario")
	public ModelAndView editarFuncionario(int funcmatricula) {
		Funcionario funcionario = fr.findByFuncmatricula(funcmatricula);
		ModelAndView mv = new ModelAndView("funcionario/update-funcionario");
		mv.addObject("funcionario", funcionario);
		return mv;
	}
	
	// POST que atualiza o funcionário
	@PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
	@PostMapping(value = "/editar-funcionario")
	public String updateFuncionario(@Valid Funcionario funcionario,  BindingResult result, RedirectAttributes attributes){
		
		fr.save(funcionario);
		attributes.addFlashAttribute("successs", "Funcionário alterado com sucesso!");
		
		long idLong = funcionario.getFuncid();
		String id = "" + idLong;
		return "redirect:/detalhes-funcionario/" + id;
		
	}
	
	// GET que deleta dependente
	@PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
	@GetMapping("/deletarDependente")
	public String deletarDependente(String funcdepcpf) {
		Dependentes dependente = dr.findByFuncdepcpf(funcdepcpf);
		
		Funcionario funcionario = dependente.getFuncionario();
		String codigo = "" + funcionario.getFuncid();
		
		dr.delete(dependente);
		return "redirect:/detalhes-funcionario/" + codigo;
	
	}
}