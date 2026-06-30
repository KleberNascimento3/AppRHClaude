package com.AppRH.AppRH.controllers;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.AppRH.AppRH.models.LogAlteracao;
import com.AppRH.AppRH.repository.LogAlteracaoRepository;

@Controller
@PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER', 'DESENVOLVEDOR')")
public class AuditoriaController {

    @Autowired
    private LogAlteracaoRepository logAlteracaoRepository;

    @GetMapping("/admin/auditoria")
    public ModelAndView auditoria(
            @RequestParam(value = "usuario", required = false) String usuario,
            @RequestParam(value = "matricula", required = false) Integer matricula,
            @RequestParam(value = "tabela", required = false) String tabela,
            @RequestParam(value = "operacao", required = false) String operacao) {

        List<LogAlteracao> todosLogs = logAlteracaoRepository.findAllByOrderByDataDesc();
        List<LogAlteracao> logsFiltrados = todosLogs.stream()
                .filter(log -> !StringUtils.hasText(usuario) || usuario.equals(log.getUsuario()))
                .filter(log -> matricula == null || matricula.equals(log.getCoopmatricula()))
                .filter(log -> !StringUtils.hasText(tabela) || tabela.equals(log.getTabela()))
                .filter(log -> !StringUtils.hasText(operacao) || operacao.equals(log.getOperacao()))
                .collect(Collectors.toList());

        ModelAndView mv = new ModelAndView("admin/auditoria");
        mv.addObject("logs", logsFiltrados);
        mv.addObject("usuarios", valoresDistintos(todosLogs, "usuario"));
        mv.addObject("tabelas", valoresDistintos(todosLogs, "tabela"));
        mv.addObject("operacoes", valoresDistintos(todosLogs, "operacao"));
        mv.addObject("usuarioSelecionado", usuario);
        mv.addObject("matriculaSelecionada", matricula);
        mv.addObject("tabelaSelecionada", tabela);
        mv.addObject("operacaoSelecionada", operacao);
        return mv;
    }

    private List<String> valoresDistintos(List<LogAlteracao> logs, String campo) {
        return logs.stream()
                .map(log -> valorCampo(log, campo))
                .filter(Objects::nonNull)
                .filter(StringUtils::hasText)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private String valorCampo(LogAlteracao log, String campo) {
        if ("usuario".equals(campo)) {
            return log.getUsuario();
        }
        if ("tabela".equals(campo)) {
            return log.getTabela();
        }
        if ("operacao".equals(campo)) {
            return log.getOperacao();
        }
        return null;
    }
}
