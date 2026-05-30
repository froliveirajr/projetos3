package com.projetos3.edenred.model;

public class OportunidadeDTO {

    private final Empresa empresa;
    private final double score;
    private final String recomendacao;
    private final double co2EvitavelKg;

    public OportunidadeDTO(Empresa empresa, double score, String recomendacao, double co2EvitavelKg) {
        this.empresa = empresa;
        this.score = score;
        this.recomendacao = recomendacao;
        this.co2EvitavelKg = co2EvitavelKg;
    }

    public Empresa getEmpresa() { return empresa; }
    public double getScore() { return score; }
    public String getRecomendacao() { return recomendacao; }
    public double getCo2EvitavelKg() { return co2EvitavelKg; }

    public String getPrioridade() {
        if (score >= 1000) return "Alta";
        if (score >= 100) return "Média";
        return "Baixa";
    }

    public String getPrioridadeKey() {
        if (score >= 1000) return "alta";
        if (score >= 100) return "media";
        return "baixa";
    }
}
