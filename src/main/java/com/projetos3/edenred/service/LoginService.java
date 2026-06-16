package com.projetos3.edenred.service;

import com.projetos3.edenred.dados.BancoEmMemoria;
import com.projetos3.edenred.model.DadosEmpresaException;
import com.projetos3.edenred.model.Empresa;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final String adminCnpj;
    private final String adminSenha;

    public LoginService(@Value("${app.admin.cnpj}") String adminCnpj,
                        @Value("${app.admin.senha}") String adminSenha) {
        this.adminCnpj = adminCnpj;
        this.adminSenha = adminSenha;
    }

    public boolean autenticarAdmin(String cnpj, String senha) {
        String loginInformado = cnpj == null ? "" : cnpj.trim();
        String senhaInformada = senha == null ? "" : senha.trim();
        return adminCnpj.equals(loginInformado) && adminSenha.equals(senhaInformada);
    }

    public Empresa autenticarEmpresa(String cnpj, String senha) throws DadosEmpresaException {
        return BancoEmMemoria.autenticar(cnpj, senha);
    }
}
