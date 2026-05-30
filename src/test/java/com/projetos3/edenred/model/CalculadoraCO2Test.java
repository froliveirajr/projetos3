package com.projetos3.edenred.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CalculadoraCO2Test {

    @Test
    public void deveriaCalcularCO2PorCartoesCorretamente() {
        // 2 cartões × 150 gCO₂e = 300 gCO₂e
        double resultado = CalculadoraCO2.calcularCO2PorCartoes(2);
        assertEquals(300.0, resultado);
    }

    @Test
    public void deveriaCalcularCO2MaterialCorretamente() {
        // 10 cartões × 60 gCO₂e = 600 gCO₂e
        double resultado = CalculadoraCO2.calcularCO2Material(10);
        assertEquals(600.0, resultado);
    }

    @Test
    public void deveriaCalcularCO2FabricacaoCorretamente() {
        // 10 cartões × 50 gCO₂e = 500 gCO₂e
        double resultado = CalculadoraCO2.calcularCO2Fabricacao(10);
        assertEquals(500.0, resultado);
    }

    @Test
    public void deveriaCalcularCO2TransporteCorretamente() {
        // 5 cartões × 40 gCO₂e = 200 gCO₂e
        double resultado = CalculadoraCO2.calcularCO2Transporte(5);
        assertEquals(200.0, resultado);
    }



    @Test
    public void deveriaCalcularCO2PorTransacoes() {
        // 1000 transações × 0.42 gCO₂e = 420 gCO₂e
        double resultado = CalculadoraCO2.calcularCO2PorTransacoes(1000);
        assertEquals(420.0, resultado);
    }

    @Test
    public void deveriaRetornarZeroParaValoresNegativos() {
        assertEquals(0.0, CalculadoraCO2.calcularCO2PorCartoes(-1));
        assertEquals(0.0, CalculadoraCO2.calcularCO2PorTransacoes(0));
    }
}