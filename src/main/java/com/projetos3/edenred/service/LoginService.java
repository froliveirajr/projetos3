package com.projetos3.edenred.service;

import com.projetos3.edenred.model.DadosEmpresaException;
import com.projetos3.edenred.model.Empresa;
import com.projetos3.edenred.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

    private final EmpresaRepository empresaRepository;
    private final String adminCnpj;
    private final String adminSenha;

    public LoginService(EmpresaRepository empresaRepository,
                        @Value("${app.admin.cnpj}") String adminCnpj,
                        @Value("${app.admin.senha}") String adminSenha) {
        this.empresaRepository = empresaRepository;
        this.adminCnpj = adminCnpj;
        this.adminSenha = adminSenha;
    }

    public boolean autenticarAdmin(String cnpj, String senha) {
        return adminCnpj.equals(cnpj) && adminSenha.equals(senha);
    }

    @Transactional(readOnly = true)
    public Empresa autenticarEmpresa(String cnpj, String senha) throws DadosEmpresaException {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new DadosEmpresaException("CNPJ e obrigatorio.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new DadosEmpresaException("Senha e obrigatoria.");
        }

        Empresa empresa = empresaRepository.findById(cnpj)
                .orElseThrow(() -> new DadosEmpresaException("Empresa nao encontrada. Verifique o CNPJ."));

        if (!empresa.getSenha().equals(senha)) {
            throw new DadosEmpresaException("Senha incorreta.");
        }

        return empresa;
    }
}
