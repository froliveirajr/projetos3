package com.projetos3.edenred.service;

import com.projetos3.edenred.model.DadosEmpresaException;
import com.projetos3.edenred.model.Empresa;
import com.projetos3.edenred.repository.EmpresaRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Transactional(readOnly = true)
    public List<Empresa> listarEmpresas() {
        return empresaRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    @Transactional(readOnly = true)
    public long contarEmpresas() {
        return empresaRepository.count();
    }

    @Transactional(readOnly = true)
    public long contarColaboradores() {
        return listarEmpresas().stream()
                .mapToLong(Empresa::getColaboradores)
                .sum();
    }

    @Transactional
    public void cadastrarEmpresa(String cnpj,
                                  String nome,
                                  String senha,
                                  int colaboradores,
                                  int numeroBeneficios,
                                  boolean multibeneficio,
                                  double vidaUtilCartaoAnos,
                                  double taxaTurnover,
                                  double taxaReemissao,
                                  int transacoesMensais,
                                  double porcentagemDigitalAtual) throws DadosEmpresaException {

        validarDadosEmpresa(cnpj, nome, senha, colaboradores, numeroBeneficios,
                vidaUtilCartaoAnos, taxaTurnover, taxaReemissao, transacoesMensais,
                porcentagemDigitalAtual);

        if (empresaRepository.existsById(cnpj)) {
            throw new DadosEmpresaException("CNPJ ja cadastrado no sistema.");
        }

        Empresa empresa = new Empresa(
                cnpj, nome, senha, colaboradores,
                numeroBeneficios, multibeneficio,
                vidaUtilCartaoAnos, taxaTurnover,
                taxaReemissao, transacoesMensais,
                porcentagemDigitalAtual
        );

        empresaRepository.save(empresa);
    }

    @Transactional
    public void removerEmpresa(String cnpj) {
        empresaRepository.deleteById(cnpj);
    }

    private void validarDadosEmpresa(String cnpj,
                                      String nome,
                                      String senha,
                                      int colaboradores,
                                      int numeroBeneficios,
                                      double vidaUtilCartaoAnos,
                                      double taxaTurnover,
                                      double taxaReemissao,
                                      int transacoesMensais,
                                      double porcentagemDigitalAtual) throws DadosEmpresaException {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new DadosEmpresaException("CNPJ e obrigatorio.");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new DadosEmpresaException("Nome da empresa e obrigatorio.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new DadosEmpresaException("Senha e obrigatoria.");
        }
        if (colaboradores <= 0) {
            throw new DadosEmpresaException("Numero de colaboradores deve ser maior que zero.");
        }
        if (numeroBeneficios <= 0) {
            throw new DadosEmpresaException("Numero de beneficios deve ser maior que zero.");
        }
        if (vidaUtilCartaoAnos <= 0) {
            throw new DadosEmpresaException("Vida util do cartao deve ser maior que zero.");
        }
        if (taxaTurnover < 0 || taxaReemissao < 0) {
            throw new DadosEmpresaException("Taxas nao podem ser negativas.");
        }
        if (transacoesMensais < 0) {
            throw new DadosEmpresaException("Transacoes mensais nao podem ser negativas.");
        }
        if (porcentagemDigitalAtual < 0 || porcentagemDigitalAtual > 100) {
            throw new DadosEmpresaException("Porcentagem digital deve estar entre 0 e 100.");
        }
    }
}
