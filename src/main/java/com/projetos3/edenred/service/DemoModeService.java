package com.projetos3.edenred.service;

import com.projetos3.edenred.dados.BancoEmMemoria;
import com.projetos3.edenred.model.Empresa;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DemoModeService {

    private volatile boolean empresaDemoEnabled;
    private volatile boolean adminDemoEnabled;

    public DemoModeService(
            @Value("${app.demo.empresa-enabled:true}") boolean empresaDemoEnabled,
            @Value("${app.demo.admin-enabled:true}") boolean adminDemoEnabled) {
        this.empresaDemoEnabled = empresaDemoEnabled;
        this.adminDemoEnabled = adminDemoEnabled;
    }

    public boolean isEmpresaDemoEnabled() {
        return empresaDemoEnabled;
    }

    public boolean isAdminDemoEnabled() {
        return adminDemoEnabled;
    }

    public void setEmpresaDemoEnabled(boolean empresaDemoEnabled) {
        this.empresaDemoEnabled = empresaDemoEnabled;
    }

    public void setAdminDemoEnabled(boolean adminDemoEnabled) {
        this.adminDemoEnabled = adminDemoEnabled;
    }

    public Empresa criarEmpresaDemo() {
        Empresa empresaBase = BancoEmMemoria.buscarPorCnpj("12.345.678/0001-99");
        if (empresaBase != null) {
            return copiarEmpresa(empresaBase);
        }

        Empresa fallback = new Empresa(
                "12.345.678/0001-99",
                "TechCorp Soluções",
                "demo",
                5000,
                3,
                true,
                5.0,
                0.15,
                0.10,
                8,
                20.0
        );
        fallback.setNomeFantasia("TechCorp");
        fallback.setCidade("São Paulo");
        fallback.setUf("SP");
        return fallback;
    }

    private Empresa copiarEmpresa(Empresa origem) {
        Empresa copia = new Empresa(
                origem.getCnpj(),
                origem.getNome(),
                origem.getSenha(),
                origem.getColaboradores(),
                origem.getNumeroBeneficios(),
                origem.isMultibeneficio(),
                origem.getVidaUtilCartaoAnos(),
                origem.getTaxaTurnover(),
                origem.getTaxaReemissao(),
                origem.getTransacoesMensais(),
                origem.getPorcentagemDigitalAtual()
        );
        copia.setNomeFantasia(origem.getNomeFantasia());
        copia.setCep(origem.getCep());
        copia.setLogradouro(origem.getLogradouro());
        copia.setNumeroEndereco(origem.getNumeroEndereco());
        copia.setBairro(origem.getBairro());
        copia.setCidade(origem.getCidade());
        copia.setUf(origem.getUf());
        copia.setSituacaoCadastral(origem.getSituacaoCadastral());
        return copia;
    }
}
