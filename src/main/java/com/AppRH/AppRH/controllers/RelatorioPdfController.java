package com.AppRH.AppRH.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.AppRH.AppRH.service.CooperadoPdfService;

@Controller
public class RelatorioPdfController {

    private static final Logger log = LoggerFactory.getLogger(RelatorioPdfController.class);

    @Autowired
    private CooperadoPdfService cooperadoPdfService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'DEVELOPER', 'DESENVOLVEDOR')")
    @GetMapping("/relatorios/pdf/cooperados")
    public void relatorioCooperados(@RequestParam(value = "status", defaultValue = "todos") String status,
                                    @RequestParam(value = "acao", defaultValue = "v") String acao,
                                    @RequestParam(value = "ordem", defaultValue = "nome") String ordem,
                                    HttpServletResponse response) throws IOException {
        try {
            byte[] bytes = cooperadoPdfService.gerarRelatorioCooperados(status, ordem);
            escreverPdf(response, bytes, "relatorio-cooperados-" + normalizarStatus(status) + ".pdf", acao);
        } catch (Exception e) {
            log.error("Erro ao gerar relatorio de cooperados com status {}", status, e);
            escreverErro(response, "Nao foi possivel gerar o relatorio. Verifique os dados dos cooperados e tente novamente.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'DEVELOPER', 'DESENVOLVEDOR')")
    @GetMapping("/relatorios/pdf/etiquetas")
    public void etiquetasCooperados(@RequestParam(value = "status", defaultValue = "ativos") String status,
                                    @RequestParam(value = "acao", defaultValue = "v") String acao,
                                    @RequestParam(value = "modelo", defaultValue = "20") int modelo,
                                    @RequestParam(value = "matriculas", required = false) String matriculas,
                                    @RequestParam(value = "ordem", defaultValue = "nome") String ordem,
                                    HttpServletResponse response) throws IOException {
        try {
            int modeloNormalizado = modeloEtiquetas(modelo);
            byte[] bytes = cooperadoPdfService.gerarEtiquetasCooperados(status, modeloNormalizado, matriculas, ordem);
            escreverPdf(response, bytes, "etiquetas-" + modeloNormalizado + "-cooperados-" + normalizarStatus(status) + ".pdf", acao);
        } catch (Exception e) {
            log.error("Erro ao gerar etiquetas de cooperados com status {}", status, e);
            escreverErro(response, "Nao foi possivel gerar as etiquetas. Verifique as matriculas e tente novamente.");
        }
    }

    private void escreverPdf(HttpServletResponse response, byte[] bytes, String nomeArquivo, String acao) throws IOException {
        response.reset();
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", ("d".equalsIgnoreCase(acao) ? "attachment" : "inline") + "; filename=\"" + nomeArquivo + "\"");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentLength(bytes.length);
        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();
    }

    private void escreverErro(HttpServletResponse response, String mensagem) throws IOException {
        response.reset();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("<html><body style='font-family:Arial,sans-serif;padding:24px'>"
                + "<h2>Erro ao gerar PDF</h2><p>" + mensagem + "</p>"
                + "<p>Se o problema continuar, envie a tela de erro para verificarmos.</p></body></html>");
        response.getWriter().flush();
    }

    private int modeloEtiquetas(int modelo) {
        return modelo == 14 ? 14 : 20;
    }

    private String normalizarStatus(String status) {
        if ("ativos".equalsIgnoreCase(status)) {
            return "ativos";
        }
        if ("inativos".equalsIgnoreCase(status)) {
            return "inativos";
        }
        return "todos";
    }
}