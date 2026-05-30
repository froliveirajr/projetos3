package com.projetos3.edenred.service;

import com.projetos3.edenred.model.CalculadoraCO2;
import com.projetos3.edenred.model.CalculadoraMateriais;
import com.projetos3.edenred.model.Empresa;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ImpactoService {

    public Map<String, String> calcularImpactoAtual(Empresa empresa) {
        Map<String, String> resposta = new HashMap<>();

        int totalCartoes = empresa.getTotalCartoesAtuais();
        double porcentagemDigital = empresa.getPorcentagemDigitalAtual();

        double co2Total = CalculadoraCO2.calcularCO2PorCartoes(totalCartoes);
        double co2Material = CalculadoraCO2.calcularCO2Material(totalCartoes);
        double co2Fabricacao = CalculadoraCO2.calcularCO2Fabricacao(totalCartoes);
        double co2Transporte = CalculadoraCO2.calcularCO2Transporte(totalCartoes);
        double plastico = CalculadoraMateriais.calcularPlastico(totalCartoes);

        resposta.put("co2", CalculadoraMateriais.formatarPeso(co2Total));
        resposta.put("co2Material", CalculadoraMateriais.formatarPeso(co2Material));
        resposta.put("co2Fabricacao", CalculadoraMateriais.formatarPeso(co2Fabricacao));
        resposta.put("co2Transporte", CalculadoraMateriais.formatarPeso(co2Transporte));
        resposta.put("plastico", CalculadoraMateriais.formatarPeso(plastico));
        resposta.put("totalCartoes", CalculadoraMateriais.formatarNumero(totalCartoes));
        resposta.put("digitalAtual", String.valueOf(porcentagemDigital));
        resposta.put("condicaoRuim", String.valueOf(porcentagemDigital < 30));

        return resposta;
    }

    public Map<String, Object> simularImpacto(Empresa empresa, Integer porcentagemAlvo) {
        Map<String, Object> resposta = new HashMap<>();

        double atual = empresa.getPorcentagemDigitalAtual();
        double diff = porcentagemAlvo - atual;

        resposta.put("porcentagemAlvo", porcentagemAlvo);
        resposta.put("porcentagemAtual", atual);
        resposta.put("piorou", diff < 0);

        double diffAbs = Math.abs(diff);
        int colaboradores = empresa.getColaboradores();
        int cartoesPorColab = empresa.getCartoesPorColaborador();

        int cartoesMudanca = (int) Math.ceil((colaboradores * cartoesPorColab) * (diffAbs / 100.0));

        double co2Cartoes = CalculadoraCO2.calcularCO2PorCartoes(cartoesMudanca);
        double plastico = CalculadoraMateriais.calcularPlastico(cartoesMudanca);

        int transacoesAnuais = CalculadoraMateriais.calcularTransacoesAnuais(
                colaboradores, empresa.getTransacoesMensais());
        int transacoesMudanca = (int) Math.ceil(transacoesAnuais * (diffAbs / 100.0));
        double co2Transacional = CalculadoraCO2.calcularCO2PorTransacoes(transacoesMudanca);
        double papel = CalculadoraMateriais.calcularPapel(transacoesMudanca);

        double co2TotalMudanca = co2Cartoes + co2Transacional;
        double residuosMudanca = plastico + papel;

        int cartoesFuturoPorColab = (int) Math.ceil(cartoesPorColab * (1.0 - porcentagemAlvo / 100.0));
        int cartoesAnuaisNovoCenario = CalculadoraCO2.calcularCartoesEvitadosPorAno(
                colaboradores, cartoesPorColab, cartoesFuturoPorColab,
                empresa.getVidaUtilCartaoAnos(), empresa.getTaxaTurnover(), empresa.getTaxaReemissao());

        double co2AnualTotal = CalculadoraCO2.calcularCO2AnualEvitado(cartoesAnuaisNovoCenario);

        int arvores = CalculadoraCO2.calcularArvoresEquivalentes(co2TotalMudanca);
        int kmCarro = CalculadoraCO2.calcularKmCarroEquivalente(co2TotalMudanca);

        int maxCartoes = colaboradores * cartoesPorColab;

        int cartoesAtuais = (int) Math.ceil(maxCartoes * (1.0 - atual / 100.0));
        int transAtual = (int) Math.ceil(transacoesAnuais * (1.0 - atual / 100.0));
        double co2AtualCartoes = CalculadoraCO2.calcularCO2PorCartoes(cartoesAtuais);
        double plasticoAtual = CalculadoraMateriais.calcularPlastico(cartoesAtuais);
        double co2AtualTrans = CalculadoraCO2.calcularCO2PorTransacoes(transAtual);
        double papelAtual = CalculadoraMateriais.calcularPapel(transAtual);

        int cartoesSim = (int) Math.ceil(maxCartoes * (1.0 - porcentagemAlvo / 100.0));
        int transSim = (int) Math.ceil(transacoesAnuais * (1.0 - porcentagemAlvo / 100.0));
        double co2SimCartoes = CalculadoraCO2.calcularCO2PorCartoes(cartoesSim);
        double plasticoSim = CalculadoraMateriais.calcularPlastico(cartoesSim);
        double co2SimTrans = CalculadoraCO2.calcularCO2PorTransacoes(transSim);
        double papelSim = CalculadoraMateriais.calcularPapel(transSim);

        resposta.put("co2", CalculadoraMateriais.formatarPeso(co2Cartoes));
        resposta.put("co2Transacional", CalculadoraMateriais.formatarPeso(co2Transacional));
        resposta.put("plastico", CalculadoraMateriais.formatarPeso(plastico));
        resposta.put("papel", CalculadoraMateriais.formatarPeso(papel));
        resposta.put("residuos", CalculadoraMateriais.formatarPeso(residuosMudanca));
        resposta.put("cartoesAfetados", CalculadoraMateriais.formatarNumero(cartoesMudanca));
        resposta.put("tabCo2Atual", CalculadoraMateriais.formatarPeso(co2AtualCartoes));
        resposta.put("tabCo2Sim", CalculadoraMateriais.formatarPeso(co2SimCartoes));
        resposta.put("tabPlasAtual", CalculadoraMateriais.formatarPeso(plasticoAtual));
        resposta.put("tabPlasSim", CalculadoraMateriais.formatarPeso(plasticoSim));
        resposta.put("tabPapelAtual", CalculadoraMateriais.formatarPeso(papelAtual));
        resposta.put("tabPapelSim", CalculadoraMateriais.formatarPeso(papelSim));
        resposta.put("tabResAtual", CalculadoraMateriais.formatarPeso(plasticoAtual + papelAtual));
        resposta.put("tabResSim", CalculadoraMateriais.formatarPeso(plasticoSim + papelSim));
        resposta.put("tabLogAtual", CalculadoraMateriais.formatarPeso(co2AtualTrans));
        resposta.put("tabLogSim", CalculadoraMateriais.formatarPeso(co2SimTrans));
        resposta.put("co2AnualTotal", CalculadoraMateriais.formatarPeso(co2AnualTotal));
        resposta.put("arvores", arvores);
        resposta.put("km", kmCarro);

        return resposta;
    }
}
