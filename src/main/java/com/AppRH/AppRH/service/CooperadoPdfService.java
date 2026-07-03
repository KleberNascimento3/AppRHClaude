package com.AppRH.AppRH.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class CooperadoPdfService {

    private static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public byte[] gerarRelatorioCooperados(String status) {
        List<CooperadoRelatorio> cooperados = buscarCooperados(status, null);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 28, 28, 30, 28);
            PdfWriter.getInstance(document, out);
            document.open();

            Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLACK);
            Font subtitulo = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.DARK_GRAY);
            Font cabecalho = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE);
            Font texto = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.BLACK);

            Paragraph title = new Paragraph("Relatorio de cooperados - " + rotuloStatus(status), titulo);
            title.setSpacingAfter(4);
            document.add(title);

            Paragraph info = new Paragraph("Gerado em " + LocalDateTime.now().format(DATA_HORA)
                    + " | Total: " + cooperados.size() + " cooperado(s)", subtitulo);
            info.setSpacingAfter(12);
            document.add(info);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[] { 1.1f, 4.0f, 2.2f, 1.4f, 4.2f });
            adicionarCabecalho(table, cabecalho, "Matricula", "Nome", "Apelido", "Status", "Endereco");

            for (CooperadoRelatorio cooperado : cooperados) {
                adicionarCelula(table, texto, String.valueOf(cooperado.matricula));
                adicionarCelula(table, texto, cooperado.nome);
                adicionarCelula(table, texto, cooperado.apelido);
                adicionarCelula(table, texto, cooperado.status);
                adicionarCelula(table, texto, cooperado.enderecoFormatado());
            }

            document.add(table);
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Nao foi possivel gerar o relatorio de cooperados.", e);
        }
    }

    public byte[] gerarEtiquetasCooperados(String status) {
        return gerarEtiquetasCooperados(status, 20, null);
    }

    public byte[] gerarEtiquetasCooperados(String status, int etiquetasPorPagina) {
        return gerarEtiquetasCooperados(status, etiquetasPorPagina, null);
    }

    public byte[] gerarEtiquetasCooperados(String status, int etiquetasPorPagina, String matriculas) {
        List<CooperadoRelatorio> cooperados = buscarCooperados(status, matriculas);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int modelo = modeloEtiquetas(etiquetasPorPagina);
            float margemVertical = margemVerticalEtiqueta(modelo);
            float alturaEtiqueta = alturaEtiqueta(modelo);
            Document document = new Document(PageSize.A4, 22, 22, margemVertical, margemVertical);
            PdfWriter.getInstance(document, out);
            document.open();

            Font nome = FontFactory.getFont(FontFactory.HELVETICA_BOLD, tamanhoFonteNome(modelo), Color.BLACK);
            Font texto = FontFactory.getFont(FontFactory.HELVETICA, tamanhoFonteTexto(modelo), Color.BLACK);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[] { 1f, 1f });

            for (CooperadoRelatorio cooperado : cooperados) {
                table.addCell(criarEtiqueta(cooperado, nome, texto, alturaEtiqueta, modelo));
            }

            int sobras = cooperados.size() % modelo;
            if (sobras > 0 || cooperados.isEmpty()) {
                int inicio = cooperados.isEmpty() ? 0 : sobras;
                for (int i = inicio; i < modelo; i++) {
                    table.addCell(criarEtiquetaVazia(alturaEtiqueta));
                }
            }

            document.add(table);
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Nao foi possivel gerar as etiquetas de cooperados.", e);
        }
    }

    private List<CooperadoRelatorio> buscarCooperados(String status, String matriculas) {
        String filtro = "";
        List<Integer> matriculasSelecionadas = parseMatriculas(matriculas);

        if ("ativos".equalsIgnoreCase(status)) {
            filtro = " WHERE UPPER(COALESCE(cad.coop_cooperado, '')) = 'ATIVO'";
        } else if ("inativos".equalsIgnoreCase(status)) {
            filtro = " WHERE UPPER(COALESCE(cad.coop_cooperado, '')) IN ('NAO', 'INATIVO')";
        }

        if (!matriculasSelecionadas.isEmpty()) {
            filtro += filtro.isEmpty() ? " WHERE " : " AND ";
            filtro += "c.coop_matricula IN (" + csvMatriculas(matriculasSelecionadas) + ")";
        }

        String ordem = matriculasSelecionadas.isEmpty()
                ? " ORDER BY c.coop_nome ASC"
                : " ORDER BY FIELD(c.coop_matricula, " + csvMatriculas(matriculasSelecionadas) + ")";

        String sql = "SELECT c.coop_matricula, c.coop_nome, c.coop_nome_guerra, "
                + "COALESCE(cad.coop_cooperado, '') AS coop_status, "
                + "COALESCE(endr.coop_end_endereco, '') AS endereco, "
                + "COALESCE(endr.coop_bairro, '') AS bairro, "
                + "COALESCE(endr.coop_cidade, '') AS cidade, "
                + "COALESCE(endr.coop_estado, '') AS estado, "
                + "COALESCE(endr.coop_cep, '') AS cep "
                + "FROM coop c "
                + "LEFT JOIN coop_cadastro cad ON cad.coop_matricula = c.coop_matricula "
                + "LEFT JOIN coop_endereco endr ON endr.coop_end_index_cod = ("
                + "SELECT MIN(e2.coop_end_index_cod) FROM coop_endereco e2 WHERE e2.coop_matricula = c.coop_matricula)"
                + filtro
                + ordem;

        return jdbcTemplate.query(sql, new CooperadoRelatorioMapper());
    }

    private List<Integer> parseMatriculas(String matriculas) {
        List<Integer> numeros = new ArrayList<>();
        if (matriculas == null || matriculas.trim().isEmpty()) {
            return numeros;
        }
        String[] partes = matriculas.split("[^0-9]+");
        for (String parte : partes) {
            if (!parte.trim().isEmpty()) {
                try {
                    Integer numero = Integer.valueOf(parte.trim());
                    if (!numeros.contains(numero)) {
                        numeros.add(numero);
                    }
                } catch (NumberFormatException ignored) {
                    // Ignora trechos que nao sejam matriculas validas.
                }
            }
        }
        return numeros;
    }

    private String csvMatriculas(List<Integer> matriculas) {
        StringBuilder builder = new StringBuilder();
        for (Integer matricula : matriculas) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(matricula);
        }
        return builder.toString();
    }

    private PdfPCell criarEtiqueta(CooperadoRelatorio cooperado, Font nome, Font texto, float alturaEtiqueta, int modelo) {
        PdfPCell cell = new PdfPCell();
        cell.setFixedHeight(alturaEtiqueta);
        cell.setPadding(modelo == 14 ? 3f : 7f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);

        Paragraph conteudo = new Paragraph();
        conteudo.setAlignment(Element.ALIGN_CENTER);
        conteudo.setLeading(modelo == 14 ? 12f : 10f);
        conteudo.add(new Phrase(valor(cooperado.nome) + " - " + cooperado.matricula + "\n", nome));
        conteudo.add(new Phrase(cooperado.enderecoBairro() + "\n", texto));
        conteudo.add(new Phrase(cooperado.cidadeCep(), texto));
        cell.addElement(conteudo);
        return cell;
    }

    private PdfPCell criarEtiquetaVazia(float alturaEtiqueta) {
        PdfPCell cell = new PdfPCell(new Phrase(""));
        cell.setFixedHeight(alturaEtiqueta);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private int modeloEtiquetas(int etiquetasPorPagina) {
        return etiquetasPorPagina == 14 ? 14 : 20;
    }

    private float alturaEtiqueta(int etiquetasPorPagina) {
        int linhas = etiquetasPorPagina == 14 ? 7 : 10;
        float areaUtil = PageSize.A4.getHeight() - (margemVerticalEtiqueta(etiquetasPorPagina) * 2);
        if (etiquetasPorPagina == 14) {
            areaUtil -= 18f;
        }
        return areaUtil / linhas;
    }

    private float margemVerticalEtiqueta(int etiquetasPorPagina) {
        return etiquetasPorPagina == 14 ? 62f : 22f;
    }

    private float tamanhoFonteNome(int etiquetasPorPagina) {
        return etiquetasPorPagina == 14 ? 10f : 8f;
    }

    private float tamanhoFonteTexto(int etiquetasPorPagina) {
        return etiquetasPorPagina == 14 ? 8f : 7f;
    }

    private String rotuloStatus(String status) {
        if ("ativos".equalsIgnoreCase(status)) {
            return "ativos";
        }
        if ("inativos".equalsIgnoreCase(status)) {
            return "inativos";
        }
        return "todos";
    }

    private void adicionarCabecalho(PdfPTable table, Font font, String... titulos) {
        for (String titulo : titulos) {
            PdfPCell cell = new PdfPCell(new Phrase(titulo, font));
            cell.setBackgroundColor(new Color(45, 61, 85));
            cell.setPadding(6f);
            table.addCell(cell);
        }
    }

    private void adicionarCelula(PdfPTable table, Font font, String valor) {
        PdfPCell cell = new PdfPCell(new Phrase(valor(valor), font));
        cell.setPadding(5f);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        table.addCell(cell);
    }

    private String valor(String valor) {
        return valor == null || valor.trim().isEmpty() ? "-" : valor.trim();
    }

    private static class CooperadoRelatorio {
        private Integer matricula;
        private String nome;
        private String apelido;
        private String status;
        private String endereco;
        private String bairro;
        private String cidade;
        private String estado;
        private String cep;

        private String enderecoFormatado() {
            StringBuilder builder = new StringBuilder();
            append(builder, endereco);
            append(builder, bairro);
            append(builder, cidade);
            append(builder, estado);
            append(builder, cep);
            return builder.length() == 0 ? "Endereco nao cadastrado" : builder.toString();
        }

        private String enderecoBairro() {
            StringBuilder builder = new StringBuilder();
            append(builder, endereco);
            append(builder, bairro);
            return builder.length() == 0 ? "-" : builder.toString();
        }

        private String cidadeCep() {
            StringBuilder builder = new StringBuilder();
            append(builder, cidade);
            append(builder, estado);
            append(builder, cep);
            return builder.length() == 0 ? "-" : builder.toString();
        }

        private void append(StringBuilder builder, String valor) {
            if (valor != null && !valor.trim().isEmpty()) {
                if (builder.length() > 0) {
                    builder.append(" - ");
                }
                builder.append(valor.trim());
            }
        }
    }

    private static class CooperadoRelatorioMapper implements RowMapper<CooperadoRelatorio> {
        @Override
        public CooperadoRelatorio mapRow(ResultSet rs, int rowNum) throws SQLException {
            CooperadoRelatorio item = new CooperadoRelatorio();
            item.matricula = rs.getInt("coop_matricula");
            item.nome = rs.getString("coop_nome");
            item.apelido = rs.getString("coop_nome_guerra");
            item.status = rs.getString("coop_status");
            item.endereco = rs.getString("endereco");
            item.bairro = rs.getString("bairro");
            item.cidade = rs.getString("cidade");
            item.estado = rs.getString("estado");
            item.cep = rs.getString("cep");
            return item;
        }
    }
}