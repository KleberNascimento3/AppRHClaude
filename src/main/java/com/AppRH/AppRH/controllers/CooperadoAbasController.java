package com.AppRH.AppRH.controllers;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AppRH.AppRH.models.Coopcadastro;
import com.AppRH.AppRH.models.Coopendereco;
import com.AppRH.AppRH.models.Cooperado;
import com.AppRH.AppRH.models.Cotaparte;
import com.AppRH.AppRH.models.Dividas;
import com.AppRH.AppRH.models.LogAlteracao;
import com.AppRH.AppRH.repository.CoopcadastroRepository;
import com.AppRH.AppRH.repository.CooperadoRepository;
import com.AppRH.AppRH.repository.CotaRepository;
import com.AppRH.AppRH.repository.DividasRepository;
import com.AppRH.AppRH.repository.EnderecoRepository;
import com.AppRH.AppRH.repository.LogAlteracaoRepository;

@Controller
@PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER', 'DESENVOLVEDOR')")
public class CooperadoAbasController {

    @Autowired
    private CooperadoRepository cooperadoRepository;

    @Autowired
    private CoopcadastroRepository coopcadastroRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private DividasRepository dividasRepository;

    @Autowired
    private CotaRepository cotaRepository;

    @Autowired
    private LogAlteracaoRepository logAlteracaoRepository;

    @PostMapping("/cooperado/{matricula}/dados-cadastrais")
    public String salvarDadosCadastrais(@PathVariable("matricula") int matricula,
            @RequestParam("coopnome") String coopnome,
            @RequestParam("coopnomeguerra") String coopnomeguerra,
            @RequestParam(value = "coopindexcod", required = false) Long cadastroId,
            @RequestParam(value = "coopdatacadastro", required = false) String dataCadastro,
            @RequestParam(value = "coopdataadmissao", required = false) String dataAdmissao,
            @RequestParam(value = "coopdatadesligamento", required = false) String dataDesligamento,
            @RequestParam(value = "coopcooperado", required = false) String status,
            @RequestParam(value = "coopmotivodesl", required = false) String motivoDesligamento,
            @RequestParam(value = "coopinformacoes", required = false) String informacoes,
            @RequestParam(value = "cooprestricao", required = false) String restricao,
            @RequestParam(value = "coopanotacoes", required = false) String anotacoes,
            RedirectAttributes attributes) {
        Cooperado cooperado = cooperadoRepository.findByCoopmatricula(matricula);
        if (cooperado == null) {
            attributes.addFlashAttribute("erro", "Cooperado nao encontrado.");
            return "redirect:/cooperados";
        }

        cooperado.setCoopnome(coopnome.toUpperCase());
        cooperado.setCoopnomeguerra(coopnomeguerra.toUpperCase());
        cooperadoRepository.save(cooperado);

        Coopcadastro cadastro = buscarOuCriarCadastro(cooperado, cadastroId);
        cadastro.setCoopdatacadastro(parseLocalDate(dataCadastro));
        cadastro.setCoopdataadmissao(parseLocalDate(dataAdmissao));
        cadastro.setCoopdatadesligamento(parseLocalDate(dataDesligamento));
        cadastro.setCoopcooperado(status);
        cadastro.setCoopmotivodesl(motivoDesligamento);
        cadastro.setCoopinformacoes(informacoes);
        cadastro.setCooprestricao(restricao);
        cadastro.setCoopanotacoes(anotacoes);
        coopcadastroRepository.save(cadastro);

        registrarLog("Cooperado/Dados cadastrais", "ALTERACAO", "Dados cadastrais atualizados", matricula);
        attributes.addFlashAttribute("mensagem", "Dados cadastrais salvos com sucesso.");
        return "redirect:/cooperado/" + matricula + "#dados-cadastrais";
    }

    @PostMapping("/cooperado/{matricula}/enderecos")
    public String criarEndereco(@PathVariable("matricula") int matricula,
            @RequestParam("coopendendereco") String endereco,
            @RequestParam(value = "coopbairro", required = false) String bairro,
            @RequestParam(value = "coopcidade", required = false) String cidade,
            @RequestParam(value = "coopestado", required = false) String estado,
            @RequestParam(value = "coopcep", required = false) String cep,
            @RequestParam(value = "cooppais", required = false) String pais,
            RedirectAttributes attributes) {
        Cooperado cooperado = cooperadoRepository.findByCoopmatricula(matricula);
        Coopendereco item = new Coopendereco();
        preencherEndereco(item, cooperado, endereco, bairro, cidade, estado, cep, pais);
        enderecoRepository.save(item);
        registrarLog("Endereco", "INCLUSAO", "Endereco incluido", matricula);
        attributes.addFlashAttribute("mensagem", "Endereco incluido com sucesso.");
        return "redirect:/cooperado/" + matricula + "#enderecos";
    }

    @PostMapping("/cooperado/{matricula}/enderecos/{id}")
    public String atualizarEndereco(@PathVariable("matricula") int matricula, @PathVariable("id") Long id,
            @RequestParam("coopendendereco") String endereco,
            @RequestParam(value = "coopbairro", required = false) String bairro,
            @RequestParam(value = "coopcidade", required = false) String cidade,
            @RequestParam(value = "coopestado", required = false) String estado,
            @RequestParam(value = "coopcep", required = false) String cep,
            @RequestParam(value = "cooppais", required = false) String pais,
            RedirectAttributes attributes) {
        Optional<Coopendereco> optional = enderecoRepository.findById(id);
        if (optional.isPresent()) {
            preencherEndereco(optional.get(), cooperadoRepository.findByCoopmatricula(matricula), endereco, bairro, cidade, estado, cep, pais);
            enderecoRepository.save(optional.get());
            registrarLog("Endereco", "ALTERACAO", "Endereco alterado", matricula);
            attributes.addFlashAttribute("mensagem", "Endereco alterado com sucesso.");
        }
        return "redirect:/cooperado/" + matricula + "#enderecos";
    }

    @PostMapping("/cooperado/{matricula}/enderecos/{id}/excluir")
    public String excluirEndereco(@PathVariable("matricula") int matricula, @PathVariable("id") Long id,
            RedirectAttributes attributes) {
        enderecoRepository.deleteById(id);
        registrarLog("Endereco", "EXCLUSAO", "Endereco excluido", matricula);
        attributes.addFlashAttribute("mensagem", "Endereco excluido com sucesso.");
        return "redirect:/cooperado/" + matricula + "#enderecos";
    }

    @PostMapping("/cooperado/{matricula}/dividas")
    public String criarDivida(@PathVariable("matricula") int matricula,
            @RequestParam(value = "coopdescricao", required = false) String descricao,
            @RequestParam(value = "coopvalor", required = false) Float valor,
            @RequestParam(value = "coopformapagto", required = false) String formaPagamento,
            @RequestParam(value = "coopdatavencimento", required = false) String dataVencimento,
            @RequestParam(value = "coopdatapagamento", required = false) String dataPagamento,
            RedirectAttributes attributes) {
        Dividas divida = new Dividas();
        preencherDivida(divida, cooperadoRepository.findByCoopmatricula(matricula), descricao, valor, formaPagamento, dataVencimento, dataPagamento);
        dividasRepository.save(divida);
        registrarLog("Dividas", "INCLUSAO", "Divida incluida", matricula);
        attributes.addFlashAttribute("mensagem", "Divida incluida com sucesso.");
        return "redirect:/cooperado/" + matricula + "#dividas";
    }

    @PostMapping("/cooperado/{matricula}/dividas/{id}")
    public String atualizarDivida(@PathVariable("matricula") int matricula, @PathVariable("id") Long id,
            @RequestParam(value = "coopdescricao", required = false) String descricao,
            @RequestParam(value = "coopvalor", required = false) Float valor,
            @RequestParam(value = "coopformapagto", required = false) String formaPagamento,
            @RequestParam(value = "coopdatavencimento", required = false) String dataVencimento,
            @RequestParam(value = "coopdatapagamento", required = false) String dataPagamento,
            RedirectAttributes attributes) {
        Optional<Dividas> optional = dividasRepository.findById(id);
        if (optional.isPresent()) {
            preencherDivida(optional.get(), cooperadoRepository.findByCoopmatricula(matricula), descricao, valor, formaPagamento, dataVencimento, dataPagamento);
            dividasRepository.save(optional.get());
            registrarLog("Dividas", "ALTERACAO", "Divida alterada", matricula);
            attributes.addFlashAttribute("mensagem", "Divida alterada com sucesso.");
        }
        return "redirect:/cooperado/" + matricula + "#dividas";
    }

    @PostMapping("/cooperado/{matricula}/dividas/{id}/excluir")
    public String excluirDivida(@PathVariable("matricula") int matricula, @PathVariable("id") Long id,
            RedirectAttributes attributes) {
        dividasRepository.deleteById(id);
        registrarLog("Dividas", "EXCLUSAO", "Divida excluida", matricula);
        attributes.addFlashAttribute("mensagem", "Divida excluida com sucesso.");
        return "redirect:/cooperado/" + matricula + "#dividas";
    }

    @PostMapping("/cooperado/{matricula}/cotas")
    public String criarCota(@PathVariable("matricula") int matricula,
            @RequestParam(value = "coopdescricao", required = false) String descricao,
            @RequestParam(value = "coopvalor", required = false) Float valor,
            @RequestParam(value = "coopformapagto", required = false) String formaPagamento,
            @RequestParam(value = "coopdatavencimento", required = false) String dataVencimento,
            @RequestParam(value = "coopdatapagamento", required = false) String dataPagamento,
            RedirectAttributes attributes) {
        Cotaparte cota = new Cotaparte();
        preencherCota(cota, cooperadoRepository.findByCoopmatricula(matricula), descricao, valor, formaPagamento, dataVencimento, dataPagamento);
        cotaRepository.save(cota);
        registrarLog("Cota parte", "INCLUSAO", "Cota parte incluida", matricula);
        attributes.addFlashAttribute("mensagem", "Cota parte incluida com sucesso.");
        return "redirect:/cooperado/" + matricula + "#cota-parte";
    }

    @PostMapping("/cooperado/{matricula}/cotas/{id}")
    public String atualizarCota(@PathVariable("matricula") int matricula, @PathVariable("id") Long id,
            @RequestParam(value = "coopdescricao", required = false) String descricao,
            @RequestParam(value = "coopvalor", required = false) Float valor,
            @RequestParam(value = "coopformapagto", required = false) String formaPagamento,
            @RequestParam(value = "coopdatavencimento", required = false) String dataVencimento,
            @RequestParam(value = "coopdatapagamento", required = false) String dataPagamento,
            RedirectAttributes attributes) {
        Optional<Cotaparte> optional = cotaRepository.findById(id);
        if (optional.isPresent()) {
            preencherCota(optional.get(), cooperadoRepository.findByCoopmatricula(matricula), descricao, valor, formaPagamento, dataVencimento, dataPagamento);
            cotaRepository.save(optional.get());
            registrarLog("Cota parte", "ALTERACAO", "Cota parte alterada", matricula);
            attributes.addFlashAttribute("mensagem", "Cota parte alterada com sucesso.");
        }
        return "redirect:/cooperado/" + matricula + "#cota-parte";
    }

    @PostMapping("/cooperado/{matricula}/cotas/{id}/excluir")
    public String excluirCota(@PathVariable("matricula") int matricula, @PathVariable("id") Long id,
            RedirectAttributes attributes) {
        cotaRepository.deleteById(id);
        registrarLog("Cota parte", "EXCLUSAO", "Cota parte excluida", matricula);
        attributes.addFlashAttribute("mensagem", "Cota parte excluida com sucesso.");
        return "redirect:/cooperado/" + matricula + "#cota-parte";
    }

    private Coopcadastro buscarOuCriarCadastro(Cooperado cooperado, Long cadastroId) {
        if (cadastroId != null) {
            Optional<Coopcadastro> optional = coopcadastroRepository.findById(cadastroId);
            if (optional.isPresent()) {
                return optional.get();
            }
        }
        Iterable<Coopcadastro> cadastros = coopcadastroRepository.findByCooperado(cooperado);
        if (cadastros.iterator().hasNext()) {
            return cadastros.iterator().next();
        }
        Coopcadastro cadastro = new Coopcadastro();
        cadastro.setCooperado(cooperado);
        return cadastro;
    }

    private void preencherEndereco(Coopendereco item, Cooperado cooperado, String endereco, String bairro, String cidade,
            String estado, String cep, String pais) {
        item.setCooperado(cooperado);
        item.setCoopendendereco(endereco);
        item.setCoopbairro(bairro);
        item.setCoopcidade(cidade);
        item.setCoopestado(estado);
        item.setCoopcep(cep);
        item.setCooppais(pais);
    }

    private void preencherDivida(Dividas divida, Cooperado cooperado, String descricao, Float valor, String formaPagamento,
            String dataVencimento, String dataPagamento) {
        divida.setCooperado(cooperado);
        divida.setCoopdescricao(descricao);
        divida.setCoopvalor(valor != null ? valor : 0F);
        divida.setCoopformapagto(formaPagamento);
        divida.setCoopdatavencimento(parseSqlDate(dataVencimento));
        divida.setCoopdatapagamento(parseSqlDate(dataPagamento));
        divida.setCoopflagcotaparte(0);
    }

    private void preencherCota(Cotaparte cota, Cooperado cooperado, String descricao, Float valor, String formaPagamento,
            String dataVencimento, String dataPagamento) {
        cota.setCooperado(cooperado);
        cota.setCoopdescricao(descricao);
        cota.setCoopvalor(valor != null ? valor : 0F);
        cota.setCoopformapagto(formaPagamento);
        cota.setCoopdatavencimento(parseSqlDate(dataVencimento));
        cota.setCoopdatapagamento(parseSqlDate(dataPagamento));
        cota.setCoopflagcotaparte(1);
    }

    private java.time.LocalDate parseLocalDate(String valor) {
        return StringUtils.hasText(valor) ? java.time.LocalDate.parse(valor) : null;
    }

    private Date parseSqlDate(String valor) {
        return StringUtils.hasText(valor) ? Date.valueOf(valor) : null;
    }

    private void registrarLog(String tabela, String operacao, String detalhes, int matricula) {
        LogAlteracao log = new LogAlteracao();
        log.setData(LocalDateTime.now());
        log.setTabela(tabela);
        log.setOperacao(operacao);
        log.setDetalhes(detalhes);
        log.setCoopmatricula(matricula);
        log.setUsuario(usuarioAtual());
        logAlteracaoRepository.save(log);
    }

    private String usuarioAtual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "sistema";
    }
}
