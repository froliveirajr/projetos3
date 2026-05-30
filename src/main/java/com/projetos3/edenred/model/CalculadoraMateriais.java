package com.projetos3.edenred.model;

public class CalculadoraMateriais {

    // ===== FATORES DE REFERÊNCIA - VALORES MAIORES (sensibilização) =====
    // Usando 7.5g (cenário ampliado dos estudos) em vez de 5g conservador
    private static final double PESO_CARTAO_PLASTICO_GRAMAS = 7.5;
    private static final double PESO_COMPROVANTE_PAPEL_GRAMAS = 2.0;

    // ===== CÁLCULOS DE MATERIAIS =====

    // Plástico evitado = cartões evitados × 7.5 g
    public static double calcularPlastico(int cartoesEvitados) {
        if (cartoesEvitados <= 0) return 0.0;
        return cartoesEvitados * PESO_CARTAO_PLASTICO_GRAMAS;
    }

    // Papel evitado = transações digitais × 2 g (comprovantes que não são impressos)
    public static double calcularPapel(int transacoesDigitais) {
        if (transacoesDigitais <= 0) return 0.0;
        return transacoesDigitais * PESO_COMPROVANTE_PAPEL_GRAMAS;
    }

    // Total de resíduos evitados (plástico + papel)
    public static double calcularResiduosTotal(int cartoesEvitados, int transacoesDigitais) {
        return calcularPlastico(cartoesEvitados) + calcularPapel(transacoesDigitais);
    }

    // ===== CÁLCULO DE TRANSAÇÕES ANUAIS =====

    // Transações anuais = N × transações mensais por colaborador × 12
    public static int calcularTransacoesAnuais(int colaboradores, int transacoesMensais) {
        return colaboradores * transacoesMensais * 12;
    }

    // ===== FORMATAÇÃO =====

    public static String formatarPeso(double gramas) {
        if (gramas >= 1000000) {
            return String.format("%.2f t", gramas / 1000000);
        }
        if (gramas >= 1000) {
            return String.format("%.2f kg", gramas / 1000);
        }
        return String.format("%.1f g", gramas);
    }

    public static String formatarNumero(int numero) {
        if (numero >= 1000) {
            return String.format("%,d", numero).replace(",", ".");
        }
        return String.valueOf(numero);
    }
}