package com.AppRH.AppRH.controllers;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AppRH.AppRH.models.Avulso;
import com.AppRH.AppRH.repository.AvulsoRepository;

@Controller
public class AvulsoController {

    @Autowired
    private AvulsoRepository avulsoRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER','DESENVOLVEDOR')")
    @GetMapping("/funcionarios-empresa")
    public ModelAndView listar(@RequestParam(value = "buscar", required = false) String buscar) {
        ModelAndView mv = new ModelAndView("avulso/listaAvulsos");
        List<Avulso> avulsos = (buscar != null && !buscar.trim().isEmpty())
                ? avulsoRepository.buscar(buscar.trim())
                : avulsoRepository.findAllByOrderByNomeAsc();
        mv.addObject("avulsos", avulsos);
        mv.addObject("buscar", buscar);
        return mv;
    }

    @PostMapping("/funcionarios-empresa")
    public String buscar(@RequestParam(value = "buscar", required = false) String buscar) {
        return "redirect:/funcionarios-empresa" + (buscar != null && !buscar.trim().isEmpty() ? "?buscar=" + buscar.trim() : "");
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','DESENVOLVEDOR')")
    @GetMapping("/cadastrarFuncionarioEmpresa")
    public ModelAndView novo() {
        ModelAndView mv = new ModelAndView("avulso/formAvulso");
        Avulso avulso = new Avulso();
        avulso.setDatacadastro(LocalDate.now());
        mv.addObject("avulso", avulso);
        mv.addObject("modo", "novo");
        return mv;
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','DESENVOLVEDOR')")
    @PostMapping("/cadastrarFuncionarioEmpresa")
    public String salvar(@Valid @ModelAttribute Avulso avulso, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("erro", "Verifique os campos.");
            return "redirect:/cadastrarFuncionarioEmpresa";
        }
        preparar(avulso);
        avulsoRepository.save(avulso);
        attributes.addFlashAttribute("mensagem", "Funcionario da empresa cadastrado com sucesso!");
        return "redirect:/funcionario-empresa/" + avulso.getId();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER','DESENVOLVEDOR')")
    @GetMapping("/funcionario-empresa/{id}")
    public ModelAndView detalhes(@PathVariable Integer id, RedirectAttributes attributes) {
        Avulso avulso = avulsoRepository.findById(id).orElse(null);
        if (avulso == null) {
            attributes.addFlashAttribute("erro", "Funcionario da empresa nao encontrado.");
            return new ModelAndView("redirect:/funcionarios-empresa");
        }
        ModelAndView mv = new ModelAndView("avulso/detalhesAvulso");
        mv.addObject("avulso", avulso);
        return mv;
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','DESENVOLVEDOR')")
    @GetMapping("/funcionario-empresa/{id}/editar")
    public ModelAndView editar(@PathVariable Integer id, RedirectAttributes attributes) {
        Avulso avulso = avulsoRepository.findById(id).orElse(null);
        if (avulso == null) {
            attributes.addFlashAttribute("erro", "Funcionario da empresa nao encontrado.");
            return new ModelAndView("redirect:/funcionarios-empresa");
        }
        ModelAndView mv = new ModelAndView("avulso/formAvulso");
        mv.addObject("avulso", avulso);
        mv.addObject("modo", "editar");
        return mv;
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','DESENVOLVEDOR')")
    @PostMapping("/funcionario-empresa/{id}")
    public String atualizar(@PathVariable Integer id, @Valid @ModelAttribute Avulso avulso,
                            BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("erro", "Verifique os campos.");
            return "redirect:/funcionario-empresa/" + id + "/editar";
        }
        avulso.setId(id);
        preparar(avulso);
        avulsoRepository.save(avulso);
        attributes.addFlashAttribute("mensagem", "Funcionario da empresa atualizado com sucesso!");
        return "redirect:/funcionario-empresa/" + id;
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','DESENVOLVEDOR')")
    @PostMapping("/funcionario-empresa/{id}/excluir")
    public String excluir(@PathVariable Integer id, RedirectAttributes attributes) {
        if (avulsoRepository.existsById(id)) {
            avulsoRepository.deleteById(id);
            attributes.addFlashAttribute("mensagem", "Funcionario da empresa excluido com sucesso!");
        } else {
            attributes.addFlashAttribute("erro", "Funcionario da empresa nao encontrado.");
        }
        return "redirect:/funcionarios-empresa";
    }

    private void preparar(Avulso avulso) {
        if (avulso.getDatacadastro() == null) {
            avulso.setDatacadastro(LocalDate.now());
        }
        avulso.setNome(valor(avulso.getNome()).toUpperCase());
        avulso.setEstado(valor(avulso.getEstado()).toUpperCase());
    }

    private String valor(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
