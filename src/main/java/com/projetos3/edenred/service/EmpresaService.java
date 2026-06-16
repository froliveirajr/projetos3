package com.projetos3.edenred.service;

import com.projetos3.edenred.dados.BancoEmMemoria;
import com.projetos3.edenred.model.CnpjEmpresaDados;
import com.projetos3.edenred.model.CnpjUtils;
import com.projetos3.edenred.model.DadosEmpresaException;
import com.projetos3.edenred.model.Empresa;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmpresaService {

    private final int transacoesPadrao;
    private final double vidaUtilPadrao;
    private final double turnoverPadrao;
    private final double reemissaoPadrao;
    private final String senhaTemporariaPrefixo;

    public EmpresaService(
            @Value("${app.prospeccao.transacoes-padrao:13}") int transacoesPadrao,
            @Value("${app.prospeccao.vida-util-cartao-padrao:3.5}") double vidaUtilPadrao,
            @Value("${app.prospeccao.turnover-padrao:0.20}") double turnoverPadrao,
            @Value("${app.prospeccao.reemissao-padrao:0.05}") double reemissaoPadrao,
            @Value("${app.prospeccao.senha-temporaria-prefixo:eden}") String senhaTemporariaPrefixo) {
        this.transacoesPadrao = transacoesPadrao;
        this.vidaUtilPadrao = vidaUtilPadrao;
        this.turnoverPadrao = turnoverPadrao;
        this.reemissaoPadrao = reemissaoPadrao;
        this.senhaTemporariaPrefixo = senhaTemporariaPrefixo;
    }

    public List<Empresa> listarEmpresas() {
        return BancoEmMemoria.listarEmpresas();
    }

    public long contarEmpresas() {
        return listarEmpresas().size();
    }

    public long contarColaboradores() {
        return listarEmpresas().stream()
                .mapToLong(Empresa::getColaboradores)
                .sum();
    }

    public void cadastrarEmpresa(String cnpj,
                                 String nome,
                                 String senha,
                                 int colaboradores,
                                 int numeroBeneficios,
                                 boolean multibeneficio,
                                 double vidaUtilCartaoAnos,
                                 double taxaTurnover,
                                 double taxaReemissao,
                                 int transacoesMensais,
                                 double porcentagemDigitalAtual) throws DadosEmpresaException {

        Empresa empresa = new Empresa(
                cnpj, nome, senha, colaboradores,
                numeroBeneficios, multibeneficio,
                vidaUtilCartaoAnos, taxaTurnover,
                taxaReemissao, transacoesMensais,
                porcentagemDigitalAtual
        );

        BancoEmMemoria.cadastrarEmpresa(empresa);
    }

    public Empresa salvarProspeccao(String cnpj,
                                    String nome,
                                    String nomeFantasia,
                                    String cep,
                                    String logradouro,
                                    String numeroEndereco,
                                    String bairro,
                                    String cidade,
                                    String uf,
                                    String situacaoCadastral,
                                    int colaboradores,
                                    int numeroBeneficios,
                                    boolean multibeneficio,
                                    int transacoesMensais,
                                    double vidaUtilCartaoAnos,
                                    double taxaTurnover,
                                    double taxaReemissao,
                                    double porcentagemDigitalAtual) throws DadosEmpresaException {

        String cnpjFormatado = CnpjUtils.formatar(cnpj);
        Empresa empresa = new Empresa(
                cnpjFormatado,
                nome,
                gerarSenhaTemporaria(cnpjFormatado),
                colaboradores,
                numeroBeneficios,
                multibeneficio,
                vidaUtilCartaoAnos,
                taxaTurnover,
                taxaReemissao,
                transacoesMensais,
                porcentagemDigitalAtual
        );

        empresa.setNomeFantasia(nomeFantasia);
        empresa.setCep(cep);
        empresa.setLogradouro(logradouro);
        empresa.setNumeroEndereco(numeroEndereco);
        empresa.setBairro(bairro);
        empresa.setCidade(cidade);
        empresa.setUf(uf);
        empresa.setSituacaoCadastral(situacaoCadastral);

        Empresa existente = BancoEmMemoria.buscarPorCnpj(cnpjFormatado);
        if (existente == null) {
            BancoEmMemoria.cadastrarEmpresa(empresa);
            return empresa;
        }

        empresa.setSenha(existente.getSenha());
        BancoEmMemoria.atualizarEmpresa(existente.getCnpj(), empresa);
        return BancoEmMemoria.buscarPorCnpj(cnpjFormatado);
    }

    public Empresa salvarProspeccao(CnpjEmpresaDados dadosCnpj,
                                    int colaboradores,
                                    int numeroBeneficios,
                                    boolean multibeneficio,
                                    double porcentagemDigitalAtual) throws DadosEmpresaException {
        return salvarProspeccao(
                dadosCnpj.cnpj(),
                dadosCnpj.razaoSocial(),
                dadosCnpj.nomeFantasia(),
                dadosCnpj.cep(),
                dadosCnpj.logradouro(),
                dadosCnpj.numero(),
                dadosCnpj.bairro(),
                dadosCnpj.cidade(),
                dadosCnpj.uf(),
                dadosCnpj.situacaoCadastral(),
                colaboradores,
                numeroBeneficios,
                multibeneficio,
                transacoesPadrao,
                vidaUtilPadrao,
                turnoverPadrao,
                reemissaoPadrao,
                porcentagemDigitalAtual
        );
    }

    public void removerEmpresa(String cnpj) {
        BancoEmMemoria.removerEmpresa(cnpj);
    }

    public int getTransacoesPadrao() {
        return transacoesPadrao;
    }

    public double getVidaUtilPadrao() {
        return vidaUtilPadrao;
    }

    public double getTurnoverPadrao() {
        return turnoverPadrao;
    }

    public double getReemissaoPadrao() {
        return reemissaoPadrao;
    }

    private String gerarSenhaTemporaria(String cnpj) {
        String digitos = CnpjUtils.somenteDigitos(cnpj);
        String sufixo = digitos.length() >= 4 ? digitos.substring(digitos.length() - 4) : digitos;
        return senhaTemporariaPrefixo + sufixo;
    }
}
