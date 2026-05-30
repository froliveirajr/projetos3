package com.projetos3.edenred.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @Column(name = "cnpj", nullable = false, length = 18)
    private String cnpj;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "colaboradores", nullable = false)
    private int colaboradores;

    @Column(name = "numero_beneficios", nullable = false)
    private int numeroBeneficios;

    @Column(name = "multibeneficio", nullable = false)
    private boolean multibeneficio;

    @Column(name = "vida_util_cartao_anos", nullable = false)
    private double vidaUtilCartaoAnos;

    @Column(name = "taxa_turnover", nullable = false)
    private double taxaTurnover;

    @Column(name = "taxa_reemissao", nullable = false)
    private double taxaReemissao;

    @Column(name = "transacoes_mensais", nullable = false)
    private int transacoesMensais;

    @Column(name = "porcentagem_digital_atual", nullable = false)
    private double porcentagemDigitalAtual;

    public Empresa() {
    }

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

    public int getCartoesPorColaborador() {
        if (multibeneficio) {
            return 1;
        }
        return numeroBeneficios;
    }

    public int getTotalCartoesAtuais() {
        return colaboradores * getCartoesPorColaborador();
    }

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
}
