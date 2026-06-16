package com.projetos3.edenred.model;

public class Empresa {

    private String cnpj;
    private String nome;
    private String senha;
    private int colaboradores;
    private int numeroBeneficios;       // quantos tipos de benefício (VA, VR, VT, combustível...)
    private boolean multibeneficio;     // true = 1 cartão para todos os benefícios
    private double vidaUtilCartaoAnos;  // V (ex: 3.5 anos)
    private double taxaTurnover;        // T (ex: 0.20 = 20% ao ano)
    private double taxaReemissao;       // R (ex: 0.05 = 5% ao ano)
    private int transacoesMensais;      // média de transações por colaborador por mês
    private double porcentagemDigitalAtual; // Novo campo: 0 a 100
    private String nomeFantasia;
    private String cep;
    private String logradouro;
    private String numeroEndereco;
    private String bairro;
    private String cidade;
    private String uf;
    private String situacaoCadastral;

    // Construtor vazio
    public Empresa() {
    }

    // Construtor completo
    public Empresa(String cnpj, String nome, String senha, int colaboradores,
                   int numeroBeneficios, boolean multibeneficio,
                   double vidaUtilCartaoAnos, double taxaTurnover,
                   double taxaReemissao, int transacoesMensais,
                   double porcentagemDigitalAtual) {
        this.cnpj = cnpj;
        this.nome = nome;
        this.senha = senha;
        this.colaboradores = colaboradores;
        this.numeroBeneficios = numeroBeneficios;
        this.multibeneficio = multibeneficio;
        this.vidaUtilCartaoAnos = vidaUtilCartaoAnos;
        this.taxaTurnover = taxaTurnover;
        this.taxaReemissao = taxaReemissao;
        this.transacoesMensais = transacoesMensais;
        this.porcentagemDigitalAtual = porcentagemDigitalAtual;
    }

    // Retorna quantos cartões físicos cada colaborador tem HOJE
    // Se multibenefício = true → 1 cartão por colaborador
    // Se multibenefício = false → 1 cartão por benefício
    public int getCartoesPorColaborador() {
        if (multibeneficio) {
            return 1;
        } else {
            return numeroBeneficios;
        }
    }

    // Retorna o total de cartões físicos atuais da empresa
    public int getTotalCartoesAtuais() {
        return colaboradores * getCartoesPorColaborador();
    }

    // Getters e Setters

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getColaboradores() {
        return colaboradores;
    }

    public void setColaboradores(int colaboradores) {
        this.colaboradores = colaboradores;
    }

    public int getNumeroBeneficios() {
        return numeroBeneficios;
    }

    public void setNumeroBeneficios(int numeroBeneficios) {
        this.numeroBeneficios = numeroBeneficios;
    }

    public boolean isMultibeneficio() {
        return multibeneficio;
    }

    public void setMultibeneficio(boolean multibeneficio) {
        this.multibeneficio = multibeneficio;
    }

    public double getVidaUtilCartaoAnos() {
        return vidaUtilCartaoAnos;
    }

    public void setVidaUtilCartaoAnos(double vidaUtilCartaoAnos) {
        this.vidaUtilCartaoAnos = vidaUtilCartaoAnos;
    }

    public double getTaxaTurnover() {
        return taxaTurnover;
    }

    public void setTaxaTurnover(double taxaTurnover) {
        this.taxaTurnover = taxaTurnover;
    }

    public double getTaxaReemissao() {
        return taxaReemissao;
    }

    public void setTaxaReemissao(double taxaReemissao) {
        this.taxaReemissao = taxaReemissao;
    }

    public int getTransacoesMensais() {
        return transacoesMensais;
    }

    public void setTransacoesMensais(int transacoesMensais) {
        this.transacoesMensais = transacoesMensais;
    }

    public double getPorcentagemDigitalAtual() {
        return porcentagemDigitalAtual;
    }

    public void setPorcentagemDigitalAtual(double porcentagemDigitalAtual) {
        this.porcentagemDigitalAtual = porcentagemDigitalAtual;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumeroEndereco() {
        return numeroEndereco;
    }

    public void setNumeroEndereco(String numeroEndereco) {
        this.numeroEndereco = numeroEndereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getSituacaoCadastral() {
        return situacaoCadastral;
    }

    public void setSituacaoCadastral(String situacaoCadastral) {
        this.situacaoCadastral = situacaoCadastral;
    }

    public String getEnderecoResumo() {
        StringBuilder endereco = new StringBuilder();
        appendParte(endereco, logradouro);
        appendParte(endereco, numeroEndereco);
        appendParte(endereco, bairro);
        appendParte(endereco, cidade);
        appendParte(endereco, uf);
        return endereco.toString();
    }

    public String getLocalidadeResumo() {
        StringBuilder localidade = new StringBuilder();
        appendParte(localidade, cidade);
        if (uf != null && !uf.isBlank()) {
            if (localidade.isEmpty()) {
                localidade.append(uf);
            } else {
                localidade.append(" / ").append(uf);
            }
        }
        return localidade.isEmpty() ? "-" : localidade.toString();
    }

    private void appendParte(StringBuilder builder, String valor) {
        if (valor == null || valor.isBlank()) {
            return;
        }
        if (!builder.isEmpty()) {
            builder.append(" - ");
        }
        builder.append(valor);
    }
}
