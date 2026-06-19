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
        int cartoesFisicos = calcularQuantidadeFisicaRestante(totalCartoes, porcentagemDigital);

        double co2Total = CalculadoraCO2.calcularCO2PorCartoes(cartoesFisicos);
        double co2Material = CalculadoraCO2.calcularCO2Material(cartoesFisicos);
        double co2Fabricacao = CalculadoraCO2.calcularCO2Fabricacao(cartoesFisicos);
        double co2Transporte = CalculadoraCO2.calcularCO2Transporte(cartoesFisicos);
        double plastico = CalculadoraMateriais.calcularPlastico(cartoesFisicos);

        resposta.put("co2", CalculadoraMateriais.formatarPeso(co2Total));
        resposta.put("co2Material", CalculadoraMateriais.formatarPeso(co2Material));
        resposta.put("co2Fabricacao", CalculadoraMateriais.formatarPeso(co2Fabricacao));
        resposta.put("co2Transporte", CalculadoraMateriais.formatarPeso(co2Transporte));
        resposta.put("plastico", CalculadoraMateriais.formatarPeso(plastico));
        resposta.put("totalCartoes", CalculadoraMateriais.formatarNumero(cartoesFisicos));
        resposta.put("digitalAtual", String.valueOf(porcentagemDigital));
        resposta.put("condicaoRuim", String.valueOf(porcentagemDigital < 30));
        resposta.put("formulaCo2", montarFormulaCo2Atual(cartoesFisicos));
        resposta.put("formulaPlastico", montarFormulaPlasticoAtual(cartoesFisicos));
        resposta.put("formulaLogistica", montarFormulaLogisticaAtual(cartoesFisicos));

        return resposta;
    }

    public Map<String, Object> simularImpacto(Empresa empresa, Integer porcentagemAlvo) {
        Map<String, Object> resposta = new HashMap<>();

        double atual = empresa.getPorcentagemDigitalAtual();
        int alvoNormalizado = Math.max(0, Math.min(100, porcentagemAlvo));
        double diff = alvoNormalizado - atual;

        resposta.put("porcentagemAlvo", alvoNormalizado);
        resposta.put("porcentagemAtual", atual);
        resposta.put("piorou", diff < 0);

        double diffAbs = Math.abs(diff);
        int colaboradores = empresa.getColaboradores();
        int cartoesPorColab = empresa.getCartoesPorColaborador();

        int maxCartoes = colaboradores * cartoesPorColab;
        int transacoesAnuais = CalculadoraMateriais.calcularTransacoesAnuais(
                colaboradores, empresa.getTransacoesMensais());

        int cartoesDigitaisAtuais = calcularQuantidadePorPercentual(maxCartoes, atual);
        int cartoesDigitaisSimulados = calcularQuantidadePorPercentual(maxCartoes, alvoNormalizado);
        int transacoesDigitaisAtuais = calcularQuantidadePorPercentual(transacoesAnuais, atual);
        int transacoesDigitaisSimuladas = calcularQuantidadePorPercentual(transacoesAnuais, alvoNormalizado);
        int cartoesDigitaisMudanca = Math.abs(cartoesDigitaisSimulados - cartoesDigitaisAtuais);
        int transacoesDigitaisMudanca = Math.abs(transacoesDigitaisSimuladas - transacoesDigitaisAtuais);

        double co2AtualCartoes = CalculadoraCO2.calcularCO2PorCartoes(cartoesDigitaisAtuais);
        double plasticoAtual = CalculadoraMateriais.calcularPlastico(cartoesDigitaisAtuais);
        double co2AtualTrans = CalculadoraCO2.calcularCO2PorTransacoes(transacoesDigitaisAtuais);
        double papelAtual = CalculadoraMateriais.calcularPapel(transacoesDigitaisAtuais);

        double co2SimCartoes = CalculadoraCO2.calcularCO2PorCartoes(cartoesDigitaisSimulados);
        double plasticoSim = CalculadoraMateriais.calcularPlastico(cartoesDigitaisSimulados);
        double co2SimTrans = CalculadoraCO2.calcularCO2PorTransacoes(transacoesDigitaisSimuladas);
        double papelSim = CalculadoraMateriais.calcularPapel(transacoesDigitaisSimuladas);

        double co2Cartoes = Math.abs(co2SimCartoes - co2AtualCartoes);
        double plastico = Math.abs(plasticoSim - plasticoAtual);
        double co2Transacional = Math.abs(co2SimTrans - co2AtualTrans);
        double papel = Math.abs(papelSim - papelAtual);

        double co2TotalMudanca = co2Cartoes + co2Transacional;
        double residuosMudanca = plastico + papel;
        double co2TotalCenario = co2SimCartoes + co2SimTrans;
        double residuosCenario = plasticoSim + papelSim;
        int cartoesFisicosAtuais = calcularQuantidadeFisicaRestante(maxCartoes, atual);
        int transacoesFisicasAtuais = calcularQuantidadeFisicaRestante(transacoesAnuais, atual);
        double residuosFisicosAtuais = CalculadoraMateriais.calcularPlastico(cartoesFisicosAtuais)
                + CalculadoraMateriais.calcularPapel(transacoesFisicasAtuais);

        int cartoesMudanca = Math.abs(cartoesDigitaisSimulados - cartoesDigitaisAtuais);
        double fatorAnual = (1.0 / empresa.getVidaUtilCartaoAnos())
                + empresa.getTaxaTurnover()
                + empresa.getTaxaReemissao();
        double co2AnualTotal = CalculadoraCO2.calcularCO2PorCartoes(
                (int) Math.ceil(cartoesDigitaisSimulados * fatorAnual));

        int arvores = CalculadoraCO2.calcularArvoresEquivalentes(co2TotalMudanca);
        int kmCarro = CalculadoraCO2.calcularKmCarroEquivalente(co2TotalMudanca);

        resposta.put("co2", CalculadoraMateriais.formatarPeso(co2Cartoes));
        resposta.put("co2Transacional", CalculadoraMateriais.formatarPeso(co2Transacional));
        resposta.put("plastico", CalculadoraMateriais.formatarPeso(plastico));
        resposta.put("papel", CalculadoraMateriais.formatarPeso(papel));
        resposta.put("residuos", CalculadoraMateriais.formatarPeso(residuosMudanca));
        resposta.put("cardCo2", CalculadoraMateriais.formatarPeso(co2SimCartoes));
        resposta.put("cardCo2Transacional", CalculadoraMateriais.formatarPeso(co2SimTrans));
        resposta.put("cardPlastico", CalculadoraMateriais.formatarPeso(plasticoSim));
        resposta.put("cardPapel", CalculadoraMateriais.formatarPeso(papelSim));
        resposta.put("cardResiduos", CalculadoraMateriais.formatarPeso(residuosCenario));
        resposta.put("cardArvores", CalculadoraCO2.calcularArvoresEquivalentes(co2TotalCenario));
        resposta.put("cardKm", CalculadoraCO2.calcularKmCarroEquivalente(co2TotalCenario));
        resposta.put("impactoFisicoCartoes", CalculadoraMateriais.formatarNumero(cartoesFisicosAtuais));
        resposta.put("impactoFisicoCo2", CalculadoraMateriais.formatarPeso(
                CalculadoraCO2.calcularCO2PorCartoes(cartoesFisicosAtuais)));
        resposta.put("impactoFisicoPlastico", CalculadoraMateriais.formatarPeso(
                CalculadoraMateriais.calcularPlastico(cartoesFisicosAtuais)));
        resposta.put("impactoFisicoResiduos", CalculadoraMateriais.formatarPeso(residuosFisicosAtuais));
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
        resposta.put("formulaImpactoAtual", montarFormulaImpactoAtual(
                cartoesFisicosAtuais, transacoesFisicasAtuais));
        resposta.put("formulaImpactoEvitadoAtual", montarFormulaImpactoEvitadoAtual(
                cartoesDigitaisAtuais, transacoesDigitaisAtuais));
        resposta.put("formulaReducaoCo2", montarFormulaReducaoCo2(cartoesDigitaisMudanca));
        resposta.put("formulaReducaoResiduos", montarFormulaReducaoResiduos(
                cartoesDigitaisMudanca, transacoesDigitaisMudanca));

        return resposta;
    }

    private String montarFormulaCo2Atual(int cartoesFisicos) {
        return "CO2 emitido = " + cartoesFisicos + " cartões físicos x 150 gCO2e por cartão "
                + "(60 g de material + 50 g de fabricação + 40 g de transporte).";
    }

    private String montarFormulaPlasticoAtual(int cartoesFisicos) {
        return "Plástico = " + cartoesFisicos + " cartões físicos x 7,5 g de plástico por cartão.";
    }

    private String montarFormulaLogisticaAtual(int cartoesFisicos) {
        return "Emissões logísticas = " + cartoesFisicos + " cartões físicos x 40 gCO2e de transporte por cartão.";
    }

    private String montarFormulaImpactoAtual(int cartoesFisicosAtuais, int transacoesFisicasAtuais) {
        return "CO2e atual = " + cartoesFisicosAtuais + " cartões físicos x 150 gCO2e. "
                + "Resíduos atuais = (" + cartoesFisicosAtuais + " x 7,5 g de plástico) + ("
                + transacoesFisicasAtuais + " transações físicas x 2 g de papel).";
    }

    private String montarFormulaImpactoEvitadoAtual(int cartoesDigitaisAtuais, int transacoesDigitaisAtuais) {
        return "Impacto evitado atual = (" + cartoesDigitaisAtuais + " cartões digitais x 150 gCO2e) + ("
                + transacoesDigitaisAtuais + " transações digitais x 2 g de papel e 0,42 gCO2e por transação).";
    }

    private String montarFormulaReducaoCo2(int cartoesDigitaisMudanca) {
        return "Redução adicional de CO2e em cartões = " + cartoesDigitaisMudanca
                + " cartões equivalentes x 150 gCO2e por cartão.";
    }

    private String montarFormulaReducaoResiduos(int cartoesDigitaisMudanca, int transacoesDigitaisMudanca) {
        return "Redução adicional de resíduos = (" + cartoesDigitaisMudanca + " cartões x 7,5 g de plástico) + ("
                + transacoesDigitaisMudanca + " transações x 2 g de papel).";
    }

    private int calcularQuantidadePorPercentual(int total, double percentual) {
        if (total <= 0 || percentual <= 0) {
            return 0;
        }
        return (int) Math.ceil(total * (percentual / 100.0));
    }

    private int calcularQuantidadeFisicaRestante(int total, double percentualDigital) {
        if (total <= 0 || percentualDigital >= 100) {
            return 0;
        }
        if (percentualDigital <= 0) {
            return total;
        }
        return (int) Math.ceil(total * (1.0 - percentualDigital / 100.0));
    }
}
