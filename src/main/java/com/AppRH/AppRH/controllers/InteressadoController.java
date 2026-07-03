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

import com.AppRH.AppRH.models.Interessado;
import com.AppRH.AppRH.repository.InteressadoRepository;

@Controller
public class InteressadoController {

    @Autowired
    private InteressadoRepository ir;

    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER','DESENVOLVEDOR')")
    @GetMapping("/interessados")
    public ModelAndView listaInteressados(@RequestParam(value = "buscar", required = false) String buscar) {
        ModelAndView mv = new ModelAndView("interessado/listaInteressados");
        List<Interessado> interessados = (buscar != null && !buscar.trim().isEmpty())
                ? ir.buscar(buscar.trim())
                : ir.findAllByOrderByFuncpronomeAsc();
        mv.addObject("interessados", interessados);
        mv.addObject("buscar", buscar);
        return mv;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER','DESENVOLVEDOR')")
    @PostMapping("/interessados")
    public String buscarInteressados(@RequestParam(value = "buscar", required = false) String buscar) {
        return "redirect:/interessados" + (buscar != null && !buscar.trim().isEmpty() ? "?buscar=" + buscar.trim() : "");
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','DESENVOLVEDOR')")
    @GetMapping("/cadastrarInteressado")
    public ModelAndView novoInteressado() {
        ModelAndView mv = new ModelAndView("interessado/formInteressado");
        Interessado interessado = new Interessado();
        interessado.setDatacadastro(LocalDate.now());
        mv.addObject("interessado", interessado);
        mv.addObject("modo", "novo");
        return mv;
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','DESENVOLVEDOR')")
    @PostMapping("/cadastrarInteressado")
    public String salvarInteressado(@Valid @ModelAttribute Interessado interessado, BindingResult result,
                                    RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("erro", "Verifique os campos obrigatorios.");
            return "redirect:/cadastrarInteressado";
        }
        prepararInteressado(interessado);
        ir.save(interessado);
        attributes.addFlashAttribute("mensagem", "Interessado cadastrado com sucesso!");
        return "redirect:/interessado/" + interessado.getFuncproindex();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO','DEVELOPER','DESENVOLVEDOR')")
    @GetMapping("/interessado/{id}")
    public ModelAndView detalhesInteressado(@PathVariable Integer id, RedirectAttributes attributes) {
        Interessado interessado = ir.findById(id).orElse(null);
        if (interessado == null) {
            attributes.addFlashAttribute("erro", "Interessado nao encontrado.");
            return new ModelAndView("redirect:/interessados");
        }
        ModelAndView mv = new ModelAndView("interessado/detalhesInteressado");
        mv.addObject("interessado", interessado);
        return mv;
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','DESENVOLVEDOR')")
    @GetMapping("/interessado/{id}/editar")
    public ModelAndView editarInteressado(@PathVariable Integer id, RedirectAttributes attributes) {
        Interessado interessado = ir.findById(id).orElse(null);
        if (interessado == null) {
            attributes.addFlashAttribute("erro", "Interessado nao encontrado.");
            return new ModelAndView("redirect:/interessados");
        }
        ModelAndView mv = new ModelAndView("interessado/formInteressado");
        mv.addObject("interessado", interessado);
        mv.addObject("modo", "editar");
        return mv;
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','DESENVOLVEDOR')")
    @PostMapping("/interessado/{id}")
    public String atualizarInteressado(@PathVariable Integer id, @Valid @ModelAttribute Interessado interessado,
                                       BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("erro", "Verifique os campos obrigatorios.");
            return "redirect:/interessado/" + id + "/editar";
        }
        interessado.setFuncproindex(id);
        prepararInteressado(interessado);
        ir.save(interessado);
        attributes.addFlashAttribute("mensagem", "Interessado atualizado com sucesso!");
        return "redirect:/interessado/" + id;
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEVELOPER','DESENVOLVEDOR')")
    @PostMapping("/interessado/{id}/excluir")
    public String excluirInteressado(@PathVariable Integer id, RedirectAttributes attributes) {
        if (ir.existsById(id)) {
            ir.deleteById(id);
            attributes.addFlashAttribute("mensagem", "Interessado excluido com sucesso!");
        } else {
            attributes.addFlashAttribute("erro", "Interessado nao encontrado.");
        }
        return "redirect:/interessados";
    }

    private void prepararInteressado(Interessado interessado) {
        if (interessado.getDatacadastro() == null) {
            interessado.setDatacadastro(LocalDate.now());
        }
        interessado.setFuncpronome(valor(interessado.getFuncpronome()).toUpperCase());
        interessado.setFuncproendereco(valor(interessado.getFuncproendereco()));
        interessado.setBairro(valor(interessado.getBairro()));
        interessado.setCidade(valor(interessado.getCidade()));
        interessado.setEstado(valor(interessado.getEstado()).toUpperCase());
        interessado.setCep(valor(interessado.getCep()));
    }

    private String valor(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
