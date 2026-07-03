package com.AppRH.AppRH.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.AppRH.AppRH.service.CooperadoPdfService;

@Controller
public class JasperController {

    private static final Logger log = LoggerFactory.getLogger(JasperController.class);

    @Autowired
    private CooperadoPdfService cooperadoPdfService;

    @GetMapping("/relatorio/pdf/jr1")
    public void exibirRelatorio01(@RequestParam("code") String code,
                                  @RequestParam("acao") String acao,
                                  HttpServletResponse response) throws IOException {
        gerarPdfLegado(code, acao, response);
    }

    @GetMapping("/relatorio/pdf/jr2")
    public void exibirRelatorio02(@RequestParam("code") String code,
                                  @RequestParam("acao") String acao,
                                  HttpServletResponse response) throws IOException {
        gerarPdfLegado(code, acao, response);
    }

    @GetMapping("/relatorio/pdf/jr3")
    public void exibirRelatorio03(@RequestParam("code") String code,
                                  @RequestParam("acao") String acao,
                                  HttpServletResponse response) throws IOException {
        gerarPdfLegado(code, acao, response);
    }

    @GetMapping("/relatorio/pdf/jr4")
    public void exibirRelatorio04(@RequestParam("code") String code,
                                  @RequestParam("acao") String acao,
                                  HttpServletResponse response) throws IOException {
        gerarPdfLegado(code, acao, response);
    }

    @GetMapping("/relatorio/pdf/jr5")
    public void exibirRelatorio05(@RequestParam("code") String code,
                                  @RequestParam("acao") String acao,
                                  HttpServletResponse response) throws IOException {
        gerarPdfLegado(code, acao, response);
    }

    private void gerarPdfLegado(String code, String acao, HttpServletResponse response) throws IOException {
        try {
            String status = statusPorCodigo(code);
            byte[] bytes;
            String nomeArquivo;

            if ("04".equals(code) || "05".equals(code)) {
                bytes = cooperadoPdfService.gerarEtiquetasCooperados("ativos", 20);
                nomeArquivo = "etiquetas-cooperados-ativos.pdf";
            } else {
                bytes = cooperadoPdfService.gerarRelatorioCooperados(status);
                nomeArquivo = "relatorio-cooperados-" + status + ".pdf";
            }

            escreverPdf(response, bytes, nomeArquivo, acao);
        } catch (Exception e) {
            log.error("Erro ao gerar relatorio legado Jasper code {}", code, e);
            escreverErro(response, "Nao foi possivel gerar o PDF. Use a tela Cooperados > Relatorios e tente novamente.");
        }
    }

    private String statusPorCodigo(String code) {
        if ("02".equals(code)) {
            return "ativos";
        }
        if ("03".equals(code)) {
            return "inativos";
        }
        return "todos";
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
                + "</body></html>");
        response.getWriter().flush();
    }
}
