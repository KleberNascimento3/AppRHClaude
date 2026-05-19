/*package com.AppRH.AppRH.controllers;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Controller
public class RelatorioController {

    @RequestMapping("/relatorio")
    public void gerarRelatorio(HttpServletResponse response) throws JRException, IOException {
        // Carregar o modelo do relatório (.jrxml)
        InputStream reportStream = getClass().getResourceAsStream("/caminho/para/o/seu/relatorio.jrxml");

        // Compilar o modelo do relatório
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Preencher os dados do relatório
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JREmptyDataSource());

        // Exportar o relatório para o formato desejado (PDF)
        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        // Definir os cabeçalhos da resposta HTTP
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=relatorio.pdf");

        // Enviar o relatório como resposta
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(pdfBytes);
        outputStream.flush();
        outputStream.close();
    }

}
*/