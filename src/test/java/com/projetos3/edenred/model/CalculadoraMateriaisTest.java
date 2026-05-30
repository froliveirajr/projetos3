package com.projetos3.edenred.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CalculadoraMateriaisTest {

    @Test
    public void deveriaCalcularPlasticoCorretamente() {
        // 10 cartões × 7.5 g = 75 g
        double resultado = CalculadoraMateriais.calcularPlastico(10);
        assertEquals(75.0, resultado);
    }

    @Test
    public void deveriaRetornarZeroQuandoCartoesNegativos() {
        double resultado = CalculadoraMateriais.calcularPlastico(-5);
        assertEquals(0.0, resultado);
    }

    @Test
    public void deveriaCalcularPapelCorretamente() {
        // 100 transações × 2 g = 200 g
        double resultado = CalculadoraMateriais.calcularPapel(100);
        assertEquals(200.0, resultado);
    }



    @Test
    public void deveriaFormatarParaGramas() {
        String resultado = CalculadoraMateriais.formatarPeso(500.0);
        assertEquals("500,0 g", resultado);
    }

    @Test
    public void deveriaFormatarParaQuilos() {
        String resultado = CalculadoraMateriais.formatarPeso(2500.0);
        assertEquals("2,50 kg", resultado);
    }
}