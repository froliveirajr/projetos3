package com.projetos3.edenred.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CnpjUtilsTest {

    @Test
    public void deveriaValidarCnpjConhecido() {
        assertTrue(CnpjUtils.isValido("11.222.333/0001-81"));
        assertTrue(CnpjUtils.isValido("11222333000181"));
    }

    @Test
    public void deveriaRecusarCnpjComDigitosInvalidos() {
        assertFalse(CnpjUtils.isValido("12.345.678/0001-99"));
        assertFalse(CnpjUtils.isValido("11.111.111/1111-11"));
    }

    @Test
    public void deveriaFormatarDigitosDoCnpj() {
        assertEquals("11.222.333/0001-44", CnpjUtils.formatar("11222333000144"));
    }
}
