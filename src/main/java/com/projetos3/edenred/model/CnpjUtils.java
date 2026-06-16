package com.projetos3.edenred.model;

public final class CnpjUtils {

    private CnpjUtils() {
    }

    public static String somenteDigitos(String cnpj) {
        if (cnpj == null) {
            return "";
        }
        return cnpj.replaceAll("\\D", "");
    }

    public static String formatar(String cnpj) {
        String digitos = somenteDigitos(cnpj);
        if (digitos.length() != 14) {
            return cnpj;
        }
        return digitos.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }

    public static boolean isValido(String cnpj) {
        String digitos = somenteDigitos(cnpj);
        if (digitos.length() != 14 || digitos.chars().distinct().count() == 1) {
            return false;
        }

        return calcularDigito(digitos, 12) == Character.getNumericValue(digitos.charAt(12))
                && calcularDigito(digitos, 13) == Character.getNumericValue(digitos.charAt(13));
    }

    private static int calcularDigito(String cnpj, int posicaoDigito) {
        int[] pesosPrimeiro = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesosSegundo = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos = posicaoDigito == 12 ? pesosPrimeiro : pesosSegundo;

        int soma = 0;
        for (int i = 0; i < posicaoDigito; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * pesos[i];
        }

        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}
