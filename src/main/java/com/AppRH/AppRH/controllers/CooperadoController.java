
package com.AppRH.AppRH.controllers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
//import java.util.List;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AppRH.AppRH.models.Coopcadastro;
import com.AppRH.AppRH.models.Coopendereco;
import com.AppRH.AppRH.models.Cooperado;
import com.AppRH.AppRH.models.Cotaparte;
import com.AppRH.AppRH.models.Dividas;
import com.AppRH.AppRH.models.Lgpd;
import com.AppRH.AppRH.models.LogAlteracao;
import com.AppRH.AppRH.models.Telefone;
import com.AppRH.AppRH.repository.CoopcadastroRepository;
import com.AppRH.AppRH.repository.CooperadoRepository;
import com.AppRH.AppRH.repository.CotaRepository;
import com.AppRH.AppRH.repository.DividasRepository;
import com.AppRH.AppRH.repository.EnderecoRepository;
import com.AppRH.AppRH.repository.LgpdRepository;
import com.AppRH.AppRH.repository.LogAlteracaoRepository;
import com.AppRH.AppRH.repository.TelefoneRepository;
import com.AppRH.AppRH.service.CooperadoService;


@Controller
public class CooperadoController {

	private static final Logger log = LoggerFactory.getLogger(CooperadoController.class);
	
	@Autowired
	private CooperadoRepository cr;
	
	@Autowired
	private TelefoneRepository tr;
	
	@Autowired
	private DividasRepository dr;
	
	@Autowired
	private CotaRepository cpr;
	
	@Autowired
	private EnderecoRepository er;
	
	@Autowired
	private CoopcadastroRepository cc;
	
	@Autowired
	private LgpdRepository lr;
	
	@Autowired
    private LogAlteracaoRepository la;
	
	@Autowired
	private CooperadoService cs;
	
	@Autowired
	private CooperadoRepository cooperadoRepository;

	@Autowired
	private CoopcadastroRepository coopCadastroRepository;
	
	@Autowired
	private DividasRepository dividasRepository;
	
	//INSERE COOPERADO
	@PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
	@GetMapping(value = "/cadastrarCooperado")
	public String form() {
		return "cooperado/formCooperado";
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
	@PostMapping(value = "/cadastrarCooperado")
	public String form(@Valid Cooperado cooperado, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem","Verifique os campos....");
			return "redirect:/cadastrarCooperado";
		}
		Integer maior =cr.achaMaior();
		log.debug("Cadastrando cooperado, próxima matrícula: {}", maior);
		     
	     cooperado.setCoopindexcod(maior+1);
	     cooperado.setCoopnome(cooperado.getCoopnome().toUpperCase());
	     cooperado.setCoopnomeguerra(cooperado.getCoopnomeguerra().toUpperCase());
	     
	     Coopcadastro coopcadastro = new Coopcadastro();
	     coopcadastro.setCoopcooperado("ATIVO");
	     coopcadastro.setCoopdataadmissao(LocalDate.now());
	     cooperado.setCoopcadastro(coopcadastro);
	     coopcadastro.setCooperado(cooperado);
	     
	     //SERÁ IMPLANTADO POSTERIORMENTE
	     /*
	     Coopendereco coopendereco = new Coopendereco();
	     //coopendereco.setCoopendindexcod(maior+1);
	     coopendereco.setCoopmatricula(maior+1);
	     coopendereco.setCoopendendereco("rua");
	     coopendereco.setCoopendendereco(coopendereco.getCoopendendereco());
	     cooperado.setCoopnome(cooperado.getCoopnome().toUpperCase());
	     coopendereco.setCoopbairro("Itaim");
	     coopendereco.setCoopcidade("São Paulo");
	     coopendereco.setCoopestado("SP");
	     coopendereco.setCoopcep("12239-041");
	     coopendereco.setCooppais("Brasil");
	     
	  // Cria uma lista de Coopendereco e adiciona o objeto coopendereco
	     List<Coopendereco> listaEnderecos = new ArrayList<>();
	     listaEnderecos.add(coopendereco);

	     // Vincular o endereço ao cooperado
	     cooperado.setCoopendereco(listaEnderecos);  // Passa a lista de endereços
	     coopendereco.setCooperado(cooperado);
	     /*
	     cooperado.setCoopendereco(coopendereco);
	     coopendereco.setCooperado(cooperado);
	     
	     */    
	     
	      
	     Lgpd lgpd = new Lgpd();
		 lgpd.setCoopligacao("SIM");
		 lgpd.setCoopwhatsapp("SIM");
		 lgpd.setCoopcorreio("SIM");
		 lgpd.setCoopnenhum("SIM");
			 
		 cooperado.setLgpd(lgpd);
		 lgpd.setCooperado(cooperado); 
		 
		 cr.save(cooperado);
		 
		
		//SALVANDO UM LOG DE INCLUSÃO
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 String username = authentication.getName();
		 
		 
		 LogAlteracao lalte= new LogAlteracao();
		 lalte.setData(LocalDateTime.now());
		 lalte.setTabela("Cooperado");
		 lalte.setOperacao("Inclusão");
		 lalte.setDetalhes("Cooperado Inserido");
		 lalte.setCoopmatricula(cooperado.getCoopindexcod());
		 lalte.setUsuario(username);
	     la.save(lalte);
	      
	
		 attributes.addFlashAttribute("mensagem","Cooperado cadastrado com sucesso!");
		 return "redirect:/cadastrarCooperado";
		 		 
	}
	
	
	//CONTROLANDO O LGPD
	@PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")//(Anotação que define qual nível de autorização pode usar esse método.)
	@PostMapping(value = "/ControlaLGPD/{coopmatricula}")//Anotação que mostra a URK que chamará o método.
	@ResponseStatus(value = HttpStatus.OK)
	public void controlaLGPD(@PathVariable("coopmatricula") int coop_matricula, @RequestParam("value") String value, @RequestParam("type") String type) {
		
		Lgpd lgpd = lr.findByCoopmatricula(coop_matricula);// Localiza a matrícula e salva no LGPD
		
		String valor = value.equals("true") ? "SIM" : "NÃO"; //Se na variável value estiver como true, salva como SIM, se não, salva como NÂO 
		
		if (type.equals("ligacao")) {// Se o tipo for Ligação, salva o que estiver na variável valor
			lgpd.setCoopligacao(valor);
		}
		
		if (type.equals("whatsapp")) {// Se o tipo for whatsapp, salva o que estiver na variável valor
			lgpd.setCoopwhatsapp(valor);
		}
		
		if (type.equals("correio")) {// Se o tipo for correio, salva o que estiver na variável valor
			lgpd.setCoopcorreio(valor);
		}
		
		lr.save(lgpd); //Salva tudo que foi selecionado na tabela LGPD
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		LogAlteracao log = new LogAlteracao();
		log.setCoopmatricula(coop_matricula);
		log.setData(LocalDateTime.now(ZoneId.of("Brazil/East")));
		log.setDetalhes("Alterou o valor: " + type + " para " + valor);
		log.setTabela("coop_lgpd");
		log.setOperacao("Update");
		log.setUsuario(username);
		
		la.save(log);		
		
			}
	//LISTAR COOPERADOS
	
	/*@GetMapping("/cooperados")
	//@PreAuthorize("isAuthenticated()")
	@PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER')")
	public ModelAndView listaCooperados(@RequestParam("page")Optional<Integer> page) {
		int paginaAtual =page.orElse(1);
		PaginacaoUtil<Cooperado> pageCooperado=cs.buscaPorPagina(paginaAtual);
		ModelAndView mv = new ModelAndView("cooperado/listaCooperados");
		//Iterable<Cooperado>cooperados= new ArrayList<Cooperado>();	
		//model.Attribute("pagecargo",pageCooperado);
		mv.addObject("pageCargo",pageCooperado);
		return mv;
	}
	*/
	
	//LISTAR COOPERADOS
	@GetMapping("/cooperados")
	@PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER')")
	public ModelAndView listaCooperados() {
		ModelAndView mv = new ModelAndView("cooperado/listaCooperados");
		Iterable<Cooperado>cooperados= new ArrayList<Cooperado>();	
		mv.addObject("cooperados",cooperados);
		return mv;
	}
	
	//MÉTODO UTILIZADO PARA A BUSCA
	@PostMapping(value = "/cooperados")
	public ModelAndView buscarIndex(@RequestParam(defaultValue="1") int pageNum,@RequestParam(name="size",defaultValue="10") int pageSize,@RequestParam("buscar") String buscar, @RequestParam("coopnome") String coopnome, Model model) {
		
		ModelAndView mv = new ModelAndView("cooperado/listaCooperados");
		String mensagem = "Resultados da busca por " + buscar;
		//Page<Cooperado> page=cs.getCooperadosPaginados(pageNum, pageSize);
		
		Pageable pageable = PageRequest.of(pageNum-1, 20);
		//Page<Cooperado> resultPage=cr.encontrarAtivos(pageable);
		
		if(coopnome.equals("")){
			if (buscar.equals("")) {	
				log.debug("Busca sem filtro, listando paginado");
				//Preparando para paginar
				findPaginated(1, model);
			} else {
				mv.addObject("cooperados",cr.findByCoopnomesCooperados(buscar));
			}
			
		}else if(coopnome.equals("all")){
			if (buscar.equals("")) {	
				log.debug("Listando todos cooperados");
				//mv.addObject("cooperados",cr.encontrarTodos());
				findPaginated(1, model);
				//Preparando para paginar
				mv.addObject("cooperados",cr.encontrarTodos(pageable));
			} else {
				mv.addObject("cooperados",cr.findByCoopnomesCooperados(buscar));
			}
			
		}else if(coopnome.equals("inativos")) {
			if (buscar.equals("")) {
				ListarInicio();
				findPaginatedI(1, model);
				
				
				//mv.addObject("cooperados",cr.encontrarInativos());
				mv.addObject("cooperados",cr.encontrarInativos(pageable));
			} else {
				mv.addObject("cooperados",cr.findByCoopnomesCooperadoInativo(buscar));
			}
		}else if(coopnome.equals("ativos")) {
			if (buscar.equals("")) {
				findPaginatedA(1, model);
				log.debug("Listando cooperados ativos");
				//mv.addObject("cooperados",cr.encontrarAtivos());
				//mv.addObject("cooperados",resultPage);
				mv.addObject("cooperados",cr.encontrarAtivos(pageable));
			} else {
				mv.addObject("cooperados",cr.findByCoopnomesCooperadoAtivo(buscar));
			}
		}else {
			mv.addObject("cooperados",cr.findByCoopnomesCooperados(buscar));
		}
		
		mv.addObject("mensagem", mensagem);
		
		return mv;	
	}
	
	//COMENTEI PARA TER CERTEZA DE QUE NÃO ESTÁ SENDO USADO 11/10/2024
	/*
	//LISTAR COOPERADOS ATIVOS	
	@GetMapping("/cooperadosA")
	public ModelAndView listaCooperadosAtivos() {
		System.out.println("cooperadosA");
		ModelAndView mv = new ModelAndView("cooperado/listaCooperado");
		//Iterable<Cooperado>cooperados=cr.encontrarAtivos();	
		//Iterable<Cooperado>cooperados=cr.findAll(pageable);	
			
	//	mv.addObject("cooperados",cooperados);
		return mv;			
	}
	
	//LISTAR COOPERADOS INATIVOS
	
		@GetMapping("/cooperadosN")
		public ModelAndView listaCooperadosInativos() {
			ModelAndView mv = new ModelAndView("cooperado/listaCooperado");
			Iterable<Cooperado>cooperados=cr.encontrarInativos();
			System.out.println("cooperadosN");
			
			mv.addObject("cooperados",cooperados);
			return mv;			
		}
	
	//LISTAR ZERADA
	
		@GetMapping("/cooperadosI")
		public ModelAndView listaCooperadosInicio() {
			System.out.println("cooperadosI");
			ModelAndView mv = new ModelAndView("cooperado/listainicio");
			//ModelAndView mv = new ModelAndView("cooperado/listaCooperados");
			return mv;			
		}
	*/
	//VER DETALHES DOS COOPERADOS
	@PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER')")
	@GetMapping(value="/cooperado/{coopmatricula}")
	public ModelAndView detalhesCooperado(@PathVariable("coopmatricula") int coop_matricula) {

	    Cooperado cooperado = cr.findByCoopmatricula(coop_matricula);

	    ModelAndView mv = new ModelAndView("cooperado/detalhesCooperado");

	    mv.addObject("cooperado", cooperado);

	    Iterable<Telefone> telefones = tr.findByCooperado(cooperado);
	    mv.addObject("telefones", telefones);

	    Iterable<Dividas> dividas = dr.findByCooperado(cooperado);
	    mv.addObject("dividas", dividas);

	    Iterable<Cotaparte> cotaparte = cpr.findByCooperado(cooperado);
	    mv.addObject("cotaparte", cotaparte);

	    Iterable<Coopcadastro> coopcadastroList = cc.findByCooperado(cooperado);

	    Coopcadastro unicoCadastro =
	        coopcadastroList.iterator().hasNext()
	            ? coopcadastroList.iterator().next()
	            : null;

	    mv.addObject("coopcadastro", unicoCadastro);

	    Iterable<Lgpd> lgpd = lr.findByCooperado(cooperado);
	    mv.addObject("lgpd", lgpd);

	    return mv;
	}
	//MOSTRAR DÍVIDAS
	@PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER')")
	@GetMapping(value="/divida{coopmatricula}")
	public ModelAndView divida(@PathVariable("coopmatricula") int coop_matricula) {
		Cooperado cooperado = cr.findByCoopmatricula(coop_matricula);
		ModelAndView mv = new ModelAndView("cooperado/divida");
		mv.addObject("cooperado",cooperado);		
		
		Iterable<Dividas> dividas = dr.findByCooperado(cooperado);
		
		double soma=0;
		
		for (Dividas divida : dividas) {
			if(divida.getCoopdatapagamento()==null) {
				soma = soma + divida.getCoopvalor();
			}
					    
		}
		
		mv.addObject("dividas",dividas);
        mv.addObject("soma",soma);		
		
		return mv;		
	}
	//MOSTRAR COTA PARTE
	@PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER')")
	@GetMapping(value="/cota{coopmatricula}")
	public ModelAndView cota(@PathVariable("coopmatricula") int coop_matricula) {
		Cooperado cooperado = cr.findByCoopmatricula(coop_matricula);
		ModelAndView mv = new ModelAndView("cooperado/cota");
		mv.addObject("cooperado",cooperado);		
		
		Iterable<Cotaparte> cotaparte = cpr.findByCooperado(cooperado);
		
		double soma=0;
		
		for (Cotaparte cota : cotaparte) {
			if(cota.getCoopdatapagamento()!=null) {
				soma = soma + cota.getCoopvalor();
			}
					    
		}	
		mv.addObject("cotaparte",cotaparte);
		mv.addObject("soma",soma);	
		
		return mv;		
	}
	
	//MOSTRAR TODAS AS COTAS PARTES NÃO DEVOLVIDAS AOS COOPERADOS	
		@GetMapping("/cotas")
		@PreAuthorize("hasAnyRole('ADMIN','DEVELOPER')")
		public ModelAndView cotas() {
			ModelAndView mv = new ModelAndView("cooperado/cotas");
			Iterable<Cotaparte> cotapartes = cpr.encontrarTodos();
			mv.addObject("cotapartes",cotapartes);
			
			return mv;		
		}
		
		//ABRE PÁGINA DE RELATÓRIOS
		//@PreAuthorize("isAuthenticated")
		@PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER')")
		@GetMapping("/relatorios")
		public ModelAndView relatorios() {
			ModelAndView mv = new ModelAndView("cooperado/relatorios");
			return mv;		
		}
		
		
	//MOSTRA OS ENDEREÇOS
		@GetMapping(value="/coopendereco{coopmatricula}")
		@PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER')")
		//@PreAuthorize("isAuthenticated")
		public ModelAndView coopendereco(@PathVariable("coopmatricula") int coop_matricula) {
			Cooperado cooperado = cr.findByCoopmatricula(coop_matricula);
			ModelAndView mv = new ModelAndView("cooperado/coopendereco");
			mv.addObject("cooperado",cooperado);		
			
			Iterable<Coopendereco> coopendereco = er.findByCooperado(cooperado);
			mv.addObject("coopendereco",coopendereco);
			
			return mv;		
		}
		
	//MOSTRA OS DADOS DO CADASTRO
			@GetMapping(value="/coopcadastro{coopmatricula}")
			//@PreAuthorize("isAuthenticated")
			@PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER')")
			public ModelAndView coopcadastro(@PathVariable("coopmatricula") int coop_matricula) {
			Cooperado cooperado = cr.findByCoopmatricula(coop_matricula);
			ModelAndView mv = new ModelAndView("cooperado/dadoscadastrais");
			mv.addObject("cooperado",cooperado);		
			
			Iterable<Coopcadastro> coopcadastro = cc.findByCooperado(cooperado);
			mv.addObject("coopcadastro",coopcadastro);
			
			return mv;		
		}
		
	//DELETA COOPERADO
	@PreAuthorize("hasAnyRole('DEVELOPER')")
	@GetMapping("/deletarCooperado")
	public String deletarCooperado(int coopmatricula) {
		Cooperado cooperado = cr.findByCoopmatricula(coopmatricula);
		cr.delete(cooperado);
		
		//SALVANDO UM LOG DE INCLUSÃO
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 String username = authentication.getName();
		 
		 
		 LogAlteracao lalte= new LogAlteracao();
		 lalte.setData(LocalDateTime.now());
		 lalte.setTabela("Cooperado");
		 lalte.setOperacao("Deleção");
		 lalte.setDetalhes("Cooperado Deletado");
		 lalte.setCoopmatricula(cooperado.getCoopindexcod());
		 lalte.setUsuario(username);
	     la.save(lalte);
		
		
		return "redirect:/cooperados";
	}
	
	//ADICIONAR TELEFONE
	@PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER')")
	@PostMapping(value="/cooperado/{coopmatricula}/telefone")
	public String detalhesCooperadosPost(@PathVariable("coopmatricula") int coop_matricula, @Valid Telefone telefone,BindingResult result, RedirectAttributes attributes) {
		log.debug("Adicionando telefone ao cooperado: {}", coop_matricula);
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos");
			return "redirect:/{coopmatricula}";
		}
		
		
		//TIPO DE TELEFONE DUPLICADO
		if(tr.findBycooptelnumero(telefone.getCooptelnumero())!=null){
			attributes.addFlashAttribute("mensagem_erro","Número duplicado");
			return "redirect:/{coopmatricula}";
		}
		
		Cooperado cooperado = cr.findByCoopmatricula(coop_matricula);
			
		telefone.setCooperado(cooperado);
		tr.save(telefone);
		attributes.addFlashAttribute("mensagem","Telefone adicionado com sucesso!");
		return "redirect:/{coopmatricula}";
		
	}
	
	//DELETA TELEFONE
	@PreAuthorize("hasAnyRole('ADMIN','DEVELOPER')")
	@GetMapping("/deletarTelefone")
	public String deletarTelefone(int cooptelindexcod) {
		Telefone telefone = tr.findByCooptelindexcod(cooptelindexcod);
		Cooperado cooperado = telefone.getCooperado();
		String coop_matricula = "" + cooperado.getCoopmatricula();
		tr.delete(telefone);	
		
		return "redirect:/" +coop_matricula;
		
	}
	
	
	@PostMapping("/uploadFoto/{matricula}")
	public String uploadFoto(@PathVariable Integer matricula,
	                         @RequestParam("foto") MultipartFile foto) {

	    try {

	        String pasta = "C:/uploads/";

	        File diretorio = new File(pasta);

	        if (!diretorio.exists()) {
	            diretorio.mkdirs();
	        }

	        String caminho = pasta + matricula + ".jpg";

	        foto.transferTo(new File(caminho));

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return "redirect:/cooperado/" + matricula;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','DEVELOPER')")
	public String detalhesTelefonePost(@PathVariable("coopmatricula") int coop_matricula, @Valid Telefone telefone,
			BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos");
			return "redirect:/{coop_matricula}";
		}
		//TELEFONE DUPLICADO
		if(tr.findBycooptelnumero(telefone.getCooptelnumero())!=null) {
			attributes.addFlashAttribute("mensagem erro","Tipo duplicado");
			return "redirect:/{coop_matricula}";
		}
		
		Cooperado cooperado = cr.findByCoopmatricula(coop_matricula);
		telefone.setCooperado(cooperado);
		tr.save(telefone);
		attributes.addFlashAttribute("mensagem","Telefone adicionado com sucesso!");
		return "redirect:/{coop_matricula}";
	
	}
	//METODOS QUE ATUALIZAM COOPERADO
	//FORMULÁRIO ALTERA COOPERADO
	
	@GetMapping(value="/editar-cooperado")
	@PreAuthorize("hasAnyRole('ADMIN','DEVELOPER')")
	public ModelAndView editarCooperado(Integer coop_matricula) {
		Cooperado cooperado = cr.findByCoopmatricula(coop_matricula);
		ModelAndView mv = new ModelAndView("cooperado/update-cooperado");
		mv.addObject("cooperado",cooperado);
		
		return mv;	
			
	}
	
	// MÉTODO PARA EDITAR DADOS CADASTRAIS
	//@GetMapping("/cooperado/editar/{id}")
	@GetMapping("/cooperado/editar/{id:\\\\d+}")
	public String editar(@PathVariable Integer id, Model model) {
	    Optional<Cooperado> cooperadoOpt = cooperadoRepository.findById(id);

	    if (cooperadoOpt.isPresent()) {
	        Cooperado cooperado = cooperadoOpt.get();
	        Iterable<Coopcadastro> cadastros = coopCadastroRepository.findByCooperado(cooperado);

	        Coopcadastro cadastro = cadastros.iterator().hasNext() ? cadastros.iterator().next() : null;

	        if (cadastro != null) {
	            model.addAttribute("cooperado", cooperado);
	            model.addAttribute("coopcadastro", cadastro);
	            return "cooperado/editarDadosCadastrais"; // nome correto do HTML
	        }
	    }

	    return "redirect:/erro";
	}

	
	@PostMapping("/cooperado/atualizar")
	public String atualizarCadastro(
	        @ModelAttribute Coopcadastro coopcadastro,
	        @RequestParam("fotosFile") MultipartFile fotosFile) {

	    Optional<Coopcadastro> optionalCadastro = coopCadastroRepository.findById(coopcadastro.getCoopindexcod());

	    if (optionalCadastro.isPresent()) {
	        Coopcadastro cadastroExistente = optionalCadastro.get();

	        // Atualiza os campos normalmente
	        cadastroExistente.setCoopdataadmissao(coopcadastro.getCoopdataadmissao());
	        cadastroExistente.setCoopdatacadastro(coopcadastro.getCoopdatacadastro());
	        cadastroExistente.setCoopdatadesligamento(coopcadastro.getCoopdatadesligamento());
	        cadastroExistente.setCoopmotivodesl(coopcadastro.getCoopmotivodesl());
	        cadastroExistente.setCoopcooperado(coopcadastro.getCoopcooperado());
	        cadastroExistente.setCoopdataadmissibilidade(coopcadastro.getCoopdataadmissibilidade());
	        cadastroExistente.setCooptestepsicologico(coopcadastro.getCooptestepsicologico());
	        cadastroExistente.setCooptestetecnico(coopcadastro.getCooptestetecnico());
	        cadastroExistente.setCoopinformacoes(coopcadastro.getCoopinformacoes());
	        cadastroExistente.setCooprestricao(coopcadastro.getCooprestricao());
	        cadastroExistente.setCoopanotacoes(coopcadastro.getCoopanotacoes());
	        cadastroExistente.setCoopdataatestepsico(coopcadastro.getCoopdataatestepsico());
	        cadastroExistente.setCoopdataatestetecnico(coopcadastro.getCoopdataatestetecnico());
	        cadastroExistente.setCoopsel(coopcadastro.getCoopsel());
	        cadastroExistente.setCoopindicacao(coopcadastro.getCoopindicacao());

	        // Processa a imagem
	        if (fotosFile != null && !fotosFile.isEmpty()) {
	            try {
	                cadastroExistente.setCoopfoto(fotosFile.getBytes());
	            } catch (IOException e) {
	                e.printStackTrace();
	                return "redirect:/erro";
	            }
	        }

	        coopCadastroRepository.save(cadastroExistente);

	        // Redireciona com sucesso
	        Long matricula = cadastroExistente.getCoopcooperado() != null
	                ? cadastroExistente.getCooperado().getCoopmatricula()
	                : 0L;

	        return "redirect:/cooperado/editar/" + matricula + "?sucesso";
	    }

	    return "redirect:/erro";
	}

	//Endpoint para exibir a foto do cooperado
	@GetMapping("/cooperado/foto/{id}")
	@ResponseBody
	public ResponseEntity<byte[]> exibirFoto(@PathVariable("id") Long id) {
	    Optional<Coopcadastro> optional = coopCadastroRepository.findById(id);

	    if (optional.isPresent() && optional.get().getCoopfoto() != null) {
	        byte[] imagem = optional.get().getCoopfoto();

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.IMAGE_JPEG); // ou IMAGE_PNG dependendo do tipo

	        return new ResponseEntity<>(imagem, headers, HttpStatus.OK);
	    }

	    return ResponseEntity.notFound().build();
	}
	
	// MÉTODO PARA EDITAR UMA DÍVIDA ESPECÍFICA
	@GetMapping("/cooperado/editarDividas/{id}")
	public String editarDividas(@PathVariable Long id, Model model) {
	    Optional<Dividas> optionalDivida = dividasRepository.findById(id);
	    if (optionalDivida.isPresent()) {
	        model.addAttribute("divida", optionalDivida.get());
	        return "cooperado/editarDividas"; // tela de edição separada
	    }
	    return "redirect:/erro";
	}

	// MÉTODO PARA ATUALIZAR UMA DÍVIDA
	@PostMapping("/cooperado/atualizarDividas")
	public String atualizarDividas(@ModelAttribute Dividas dividas) {
	    Optional<Dividas> optionalDividas = dividasRepository.findById(dividas.getCoopindexcod());
	    
	    if (optionalDividas.isPresent()) {
	        Dividas dividaExistente = optionalDividas.get();

	        // Atualiza os campos
	        dividaExistente.setCoopdescricao(dividas.getCoopdescricao());
	        dividaExistente.setCoopformapagto(dividas.getCoopformapagto());
	        dividaExistente.setCoopdatavencimento(dividas.getCoopdatavencimento());
	        dividaExistente.setCoopdatapagamento(dividas.getCoopdatapagamento());
	        dividaExistente.setCoopflagcotaparte(dividas.getCoopflagcotaparte());
	        dividaExistente.setCoopvalor(dividas.getCoopvalor());

	        dividasRepository.save(dividaExistente);

	        // Redireciona para a lista de dívidas do cooperado
	        return "redirect:/divida" + dividaExistente.getCooperado().getCoopmatricula() + "?sucesso";
	    }

	    return "redirect:/erro";
	}

	
	//UPDATE COOPERADO
	@PreAuthorize("hasAnyRole('ADMIN','DEVELOPER')")
	@PostMapping(value="/editar-cooperado")
	public String updateCooperado(@Valid Cooperado cooperado,BindingResult result, RedirectAttributes attributes) {
		cooperado.setCoopnome(cooperado.getCoopnome().toUpperCase());
	    cooperado.setCoopnomeguerra(cooperado.getCoopnomeguerra().toUpperCase());
		cr.save(cooperado);
		attributes.addFlashAttribute("sucess","Cooperado alterado com sucesso");
		long codigoInt = cooperado.getCoopmatricula();
		String coopmatricula=""+codigoInt;
		return "redirect:/" +coopmatricula;
	}
	
	//MÉTODO PARA TRABALHAR COM ENCRIPTAÇÃO
	@Autowired
	@PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER')")
	private PasswordEncoder passwordEncorderBean() {
		return new BCryptPasswordEncoder();
	}
	
	//Para paginar todos os cooperados
	@GetMapping("/cooperados/{pageNo}/todos")
	@PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER')")
	public String findPaginated(@PathVariable (value = "pageNo")int pageNo, Model model) {
		int pageSize=20;
		
		log.debug("Paginando cooperados, página: {}", pageNo);
		
		Page<Cooperado> page = cs.findPaginated(pageNo, pageSize);
		List<Cooperado> listCooperados= page.getContent();		
		
		model.addAttribute("contextPath","todos");
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("totalItems",page.getTotalElements());
		model.addAttribute("cooperados", listCooperados);
		return "cooperado/listaCooperados";
		
		
	}
	
	//Para paginar os cooperados Ativos
	@GetMapping("/cooperados/{pageNo}/ativos")
	@PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER')")
	public String findPaginatedA(@PathVariable (value = "pageNo")int pageNo, Model model) {
		int pageSize=20;
		
		log.debug("Paginando cooperados, página: {}", pageNo);
		
		Page<Cooperado> page = cs.findPaginatedA(pageNo, pageSize);
		List<Cooperado> listCooperados= page.getContent();		
		
		model.addAttribute("contextPath","ativos");
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("totalItems",page.getTotalElements());
		model.addAttribute("cooperados", listCooperados);
		return "cooperado/listaCooperados";
		
		
	}
	
	//Para paginar os cooperados Inativos
	@GetMapping("/cooperados/{pageNo}/inativos")
	@PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER')")
	public String findPaginatedI(@PathVariable (value = "pageNo")int pageNo, Model model) {
		int pageSize=20;
		
		log.debug("Paginando cooperados, página: {}", pageNo);
		
		Page<Cooperado> page = cs.findPaginatedI(pageNo, pageSize);
		List<Cooperado> listCooperados= page.getContent();		
		
		model.addAttribute("contextPath","inativos");
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages",page.getTotalPages());
		model.addAttribute("totalItems",page.getTotalElements());
		model.addAttribute("cooperados", listCooperados);
		return "cooperado/listaCooperados";		
	}
	
	//Para ir para a página de Listar
		@GetMapping()
		@PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER')")
		public String ListarInicio() {
			
			return "redirect:/cooperados";		
		}
		
}
