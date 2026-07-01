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
import com.AppRH.AppRH.models.Coopdadospessoais;
import com.AppRH.AppRH.models.Coopdocumentos;
import com.AppRH.AppRH.models.Coopendereco;
import com.AppRH.AppRH.models.Cooperado;
import com.AppRH.AppRH.models.Cotaparte;
import com.AppRH.AppRH.models.Dividas;
import com.AppRH.AppRH.models.LogAlteracao;
import com.AppRH.AppRH.repository.CoopcadastroRepository;
import com.AppRH.AppRH.repository.DadosPessoaisRepository;
import com.AppRH.AppRH.repository.DocumentosRepository;
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
    private DadosPessoaisRepository dadosPessoaisRepository;

    @Autowired
    private DocumentosRepository documentosRepository;

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


    @PostMapping("/cooperado/{matricula}/dados-pessoais")
    public String criarDadosPessoais(@PathVariable("matricula") int matricula,
            @RequestParam(value = "cooppai", required = false) String pai,
            @RequestParam(value = "coopmae", required = false) String mae,
            @RequestParam(value = "coopconjuge", required = false) String conjuge,
            @RequestParam(value = "coopnumfilhos", required = false) Integer numFilhos,
            @RequestParam(value = "cooplocalnasc", required = false) String localNascimento,
            @RequestParam(value = "coopdatanasc", required = false) String dataNascimento,
            @RequestParam(value = "coopescolaridade", required = false) String escolaridade,
            @RequestParam(value = "coopestadocivil", required = false) String estadoCivil,
            @RequestParam(value = "coopsexo", required = false) String sexo,
            @RequestParam(value = "coopnacionalidade", required = false) String nacionalidade,
            @RequestParam(value = "coopemail", required = false) String email,
            @RequestParam(value = "coopnrdep", required = false) Integer nrDep,
            @RequestParam(value = "coopaposentado", required = false) String aposentado,
            @RequestParam(value = "coopbeneficio", required = false) String beneficio,
            @RequestParam(value = "coopdepir", required = false) Integer depIr,
            @RequestParam(value = "coopissqn", required = false) String issqn,
            RedirectAttributes attributes) {
        Coopdadospessoais dados = new Coopdadospessoais();
        preencherDadosPessoais(dados, cooperadoRepository.findByCoopmatricula(matricula), pai, mae, conjuge, numFilhos,
                localNascimento, dataNascimento, escolaridade, estadoCivil, sexo, nacionalidade, email, nrDep,
                aposentado, beneficio, depIr, issqn);
        dadosPessoaisRepository.save(dados);
        registrarLog("Dados pessoais", "INCLUSAO", "Dados pessoais incluidos", matricula);
        attributes.addFlashAttribute("mensagem", "Dados pessoais incluidos com sucesso.");
        return "redirect:/cooperado/" + matricula + "#dados-pessoais";
    }

    @PostMapping("/cooperado/{matricula}/dados-pessoais/{id}")
    public String atualizarDadosPessoais(@PathVariable("matricula") int matricula, @PathVariable("id") Long id,
            @RequestParam(value = "cooppai", required = false) String pai,
            @RequestParam(value = "coopmae", required = false) String mae,
            @RequestParam(value = "coopconjuge", required = false) String conjuge,
            @RequestParam(value = "coopnumfilhos", required = false) Integer numFilhos,
            @RequestParam(value = "cooplocalnasc", required = false) String localNascimento,
            @RequestParam(value = "coopdatanasc", required = false) String dataNascimento,
            @RequestParam(value = "coopescolaridade", required = false) String escolaridade,
            @RequestParam(value = "coopestadocivil", required = false) String estadoCivil,
            @RequestParam(value = "coopsexo", required = false) String sexo,
            @RequestParam(value = "coopnacionalidade", required = false) String nacionalidade,
            @RequestParam(value = "coopemail", required = false) String email,
            @RequestParam(value = "coopnrdep", required = false) Integer nrDep,
            @RequestParam(value = "coopaposentado", required = false) String aposentado,
            @RequestParam(value = "coopbeneficio", required = false) String beneficio,
            @RequestParam(value = "coopdepir", required = false) Integer depIr,
            @RequestParam(value = "coopissqn", required = false) String issqn,
            RedirectAttributes attributes) {
        Optional<Coopdadospessoais> optional = dadosPessoaisRepository.findById(id);
        if (optional.isPresent()) {
            preencherDadosPessoais(optional.get(), cooperadoRepository.findByCoopmatricula(matricula), pai, mae, conjuge,
                    numFilhos, localNascimento, dataNascimento, escolaridade, estadoCivil, sexo, nacionalidade, email,
                    nrDep, aposentado, beneficio, depIr, issqn);
            dadosPessoaisRepository.save(optional.get());
            registrarLog("Dados pessoais", "ALTERACAO", "Dados pessoais alterados", matricula);
            attributes.addFlashAttribute("mensagem", "Dados pessoais alterados com sucesso.");
        }
        return "redirect:/cooperado/" + matricula + "#dados-pessoais";
    }

    @PostMapping("/cooperado/{matricula}/dados-pessoais/{id}/excluir")
    public String excluirDadosPessoais(@PathVariable("matricula") int matricula, @PathVariable("id") Long id,
            RedirectAttributes attributes) {
        dadosPessoaisRepository.deleteById(id);
        registrarLog("Dados pessoais", "EXCLUSAO", "Dados pessoais excluidos", matricula);
        attributes.addFlashAttribute("mensagem", "Dados pessoais excluidos com sucesso.");
        return "redirect:/cooperado/" + matricula + "#dados-pessoais";
    }

    @PostMapping("/cooperado/{matricula}/documentos")
    public String criarDocumento(@PathVariable("matricula") int matricula,
            @RequestParam(value = "cooprg", required = false) String rg,
            @RequestParam(value = "cooprgoem", required = false) String rgOem,
            @RequestParam(value = "cooprgemis", required = false) String rgEmissao,
            @RequestParam(value = "coopcpf", required = false) String cpf,
            @RequestParam(value = "coopinss", required = false) Integer inss,
            @RequestParam(value = "cooppassaporte", required = false) String passaporte,
            @RequestParam(value = "coopdataexpedicao", required = false) String dataExpedicao,
            @RequestParam(value = "coopdtvenctopass", required = false) String vencimentoPassaporte,
            @RequestParam(value = "coopoempass", required = false) String oemPassaporte,
            @RequestParam(value = "coopnrnie", required = false) String nrNie,
            @RequestParam(value = "coopnievencto", required = false) String vencimentoNie,
            @RequestParam(value = "coopeb2", required = false) String eb2,
            @RequestParam(value = "coopeb2dtinicio", required = false) String eb2Inicio,
            @RequestParam(value = "coopeb2dtvencto", required = false) String eb2Vencimento,
            RedirectAttributes attributes) {
        Coopdocumentos documento = new Coopdocumentos();
        preencherDocumento(documento, cooperadoRepository.findByCoopmatricula(matricula), rg, rgOem, rgEmissao, cpf, inss,
                passaporte, dataExpedicao, vencimentoPassaporte, oemPassaporte, nrNie, vencimentoNie, eb2, eb2Inicio,
                eb2Vencimento);
        documentosRepository.save(documento);
        registrarLog("Documentos", "INCLUSAO", "Documento incluido", matricula);
        attributes.addFlashAttribute("mensagem", "Documento incluido com sucesso.");
        return "redirect:/cooperado/" + matricula + "#documentos";
    }

    @PostMapping("/cooperado/{matricula}/documentos/{id}")
    public String atualizarDocumento(@PathVariable("matricula") int matricula, @PathVariable("id") Long id,
            @RequestParam(value = "cooprg", required = false) String rg,
            @RequestParam(value = "cooprgoem", required = false) String rgOem,
            @RequestParam(value = "cooprgemis", required = false) String rgEmissao,
            @RequestParam(value = "coopcpf", required = false) String cpf,
            @RequestParam(value = "coopinss", required = false) Integer inss,
            @RequestParam(value = "cooppassaporte", required = false) String passaporte,
            @RequestParam(value = "coopdataexpedicao", required = false) String dataExpedicao,
            @RequestParam(value = "coopdtvenctopass", required = false) String vencimentoPassaporte,
            @RequestParam(value = "coopoempass", required = false) String oemPassaporte,
            @RequestParam(value = "coopnrnie", required = false) String nrNie,
            @RequestParam(value = "coopnievencto", required = false) String vencimentoNie,
            @RequestParam(value = "coopeb2", required = false) String eb2,
            @RequestParam(value = "coopeb2dtinicio", required = false) String eb2Inicio,
            @RequestParam(value = "coopeb2dtvencto", required = false) String eb2Vencimento,
            RedirectAttributes attributes) {
        Optional<Coopdocumentos> optional = documentosRepository.findById(id);
        if (optional.isPresent()) {
            preencherDocumento(optional.get(), cooperadoRepository.findByCoopmatricula(matricula), rg, rgOem, rgEmissao,
                    cpf, inss, passaporte, dataExpedicao, vencimentoPassaporte, oemPassaporte, nrNie, vencimentoNie,
                    eb2, eb2Inicio, eb2Vencimento);
            documentosRepository.save(optional.get());
            registrarLog("Documentos", "ALTERACAO", "Documento alterado", matricula);
            attributes.addFlashAttribute("mensagem", "Documento alterado com sucesso.");
        }
        return "redirect:/cooperado/" + matricula + "#documentos";
    }

    @PostMapping("/cooperado/{matricula}/documentos/{id}/excluir")
    public String excluirDocumento(@PathVariable("matricula") int matricula, @PathVariable("id") Long id,
            RedirectAttributes attributes) {
        documentosRepository.deleteById(id);
        registrarLog("Documentos", "EXCLUSAO", "Documento excluido", matricula);
        attributes.addFlashAttribute("mensagem", "Documento excluido com sucesso.");
        return "redirect:/cooperado/" + matricula + "#documentos";
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
    private void preencherDadosPessoais(Coopdadospessoais dados, Cooperado cooperado, String pai, String mae,
            String conjuge, Integer numFilhos, String localNascimento, String dataNascimento, String escolaridade,
            String estadoCivil, String sexo, String nacionalidade, String email, Integer nrDep, String aposentado,
            String beneficio, Integer depIr, String issqn) {
        dados.setCooperado(cooperado);
        dados.setCooppai(pai);
        dados.setCoopmae(mae);
        dados.setCoopconjuge(conjuge);
        dados.setCoopnumfilhos(numFilhos);
        dados.setCooplocalnasc(localNascimento);
        dados.setCoopdatanasc(parseSqlDate(dataNascimento));
        dados.setCoopescolaridade(escolaridade);
        dados.setCoopestadocivil(estadoCivil);
        dados.setCoopsexo(sexo);
        dados.setCoopnacionalidade(nacionalidade);
        dados.setCoopemail(email);
        dados.setCoopnrdep(nrDep);
        dados.setCoopaposentado(aposentado);
        dados.setCoopbeneficio(beneficio);
        dados.setCoopdepir(depIr);
        dados.setCoopissqn(issqn);
    }

    private void preencherDocumento(Coopdocumentos documento, Cooperado cooperado, String rg, String rgOem,
            String rgEmissao, String cpf, Integer inss, String passaporte, String dataExpedicao,
            String vencimentoPassaporte, String oemPassaporte, String nrNie, String vencimentoNie, String eb2,
            String eb2Inicio, String eb2Vencimento) {
        documento.setCooperado(cooperado);
        documento.setCooprg(rg);
        documento.setCooprgoem(rgOem);
        documento.setCooprgemis(parseSqlDate(rgEmissao));
        documento.setCoopcpf(cpf);
        documento.setCoopinss(inss);
        documento.setCooppassaporte(passaporte);
        documento.setCoopdataexpedicao(parseSqlDate(dataExpedicao));
        documento.setCoopdtvenctopass(parseSqlDate(vencimentoPassaporte));
        documento.setCoopoempass(oemPassaporte);
        documento.setCoopnrnie(nrNie);
        documento.setCoopnievencto(parseSqlDate(vencimentoNie));
        documento.setCoopeb2(eb2);
        documento.setCoopeb2dtinicio(parseSqlDate(eb2Inicio));
        documento.setCoopeb2dtvencto(parseSqlDate(eb2Vencimento));
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
