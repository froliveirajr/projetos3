package com.projetos3.edenred.model;

public class CalculadoraCO2 {

    // ===== FATORES DE EMISSÃO - VALORES MAIORES =====
    // Total: 150 gCO₂e por cartão físico
    private static final double CO2_MATERIAL_POR_CARTAO = 60.0;    // gCO₂e - corpo/material
    private static final double CO2_FABRICACAO_POR_CARTAO = 50.0;   // gCO₂e - fabricação
    private static final double CO2_TRANSPORTE_POR_CARTAO = 40.0;   // gCO₂e - transporte, embalagem e outros
    private static final double CO2_TOTAL_POR_CARTAO = 150.0;       // gCO₂e - total por cartão

    // Economia por transação digital (JCB - valor mais favorável)
    private static final double CO2_ECONOMIA_POR_TRANSACAO = 0.42;  // gCO₂e por transação evitada

    // ===== CÁLCULOS DE IMPACTO IMEDIATO =====

    // CO₂e evitado pela eliminação de cartões físicos
    // CO₂e evitado = cartões evitados × 150 gCO₂e
    public static double calcularCO2PorCartoes(int cartoesEvitados) {
        if (cartoesEvitados <= 0) return 0.0;
        return cartoesEvitados * CO2_TOTAL_POR_CARTAO;
    }

    // Detalhamento: CO₂e do material
    public static double calcularCO2Material(int cartoesEvitados) {
        if (cartoesEvitados <= 0) return 0.0;
        return cartoesEvitados * CO2_MATERIAL_POR_CARTAO;
    }

    // Detalhamento: CO₂e da fabricação
    public static double calcularCO2Fabricacao(int cartoesEvitados) {
        if (cartoesEvitados <= 0) return 0.0;
        return cartoesEvitados * CO2_FABRICACAO_POR_CARTAO;
    }

    // Detalhamento: CO₂e do transporte e embalagem
    public static double calcularCO2Transporte(int cartoesEvitados) {
        if (cartoesEvitados <= 0) return 0.0;
        return cartoesEvitados * CO2_TRANSPORTE_POR_CARTAO;
    }

    // ===== CÁLCULOS DE IMPACTO POR TRANSAÇÃO =====

    // CO₂e evitado por transações digitais
    // CO₂e transacional evitado = transações anuais × 0.42 gCO₂e
    public static double calcularCO2PorTransacoes(int transacoesAnuais) {
        if (transacoesAnuais <= 0) return 0.0;
        return transacoesAnuais * CO2_ECONOMIA_POR_TRANSACAO;
    }

    // ===== FÓRMULA ANUAL ESG =====

    // Cartões evitados por ano = N × (C_atual - C_futuro) × (1/V + T + R)
    // V = vida útil em anos, T = turnover anual, R = reemissão anual
    public static int calcularCartoesEvitadosPorAno(int colaboradores,
                                                     int cartoesPorColaboradorAtual,
                                                     int cartoesPorColaboradorFuturo,
                                                     double vidaUtilAnos,
                                                     double taxaTurnover,
                                                     double taxaReemissao) {
        if (colaboradores <= 0 || vidaUtilAnos <= 0) return 0;

        int diferencaCartoes = cartoesPorColaboradorAtual - cartoesPorColaboradorFuturo;
        if (diferencaCartoes <= 0) return 0;

        double fatorAnual = (1.0 / vidaUtilAnos) + taxaTurnover + taxaReemissao;
        double cartoesEvitados = colaboradores * diferencaCartoes * fatorAnual;

        return (int) Math.ceil(cartoesEvitados);
    }

    // CO₂e anual evitado (para relatório ESG)
    public static double calcularCO2AnualEvitado(int cartoesEvitadosPorAno) {
        return calcularCO2PorCartoes(cartoesEvitadosPorAno);
    }

    // ===== EQUIVALÊNCIAS (para sensibilização) =====

    // 1 árvore absorve ~20 kgCO₂ por ano
    public static int calcularArvoresEquivalentes(double co2Gramas) {
        if (co2Gramas <= 0) return 0;
        double co2Kg = co2Gramas / 1000.0;
        return (int) Math.ceil(co2Kg / 20.0);
    }

    // 1 carro emite ~140 gCO₂ por km rodado
    public static int calcularKmCarroEquivalente(double co2Gramas) {
        if (co2Gramas <= 0) return 0;
        return (int) Math.ceil(co2Gramas / 140.0);
    }


}