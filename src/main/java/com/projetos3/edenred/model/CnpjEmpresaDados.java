package com.projetos3.edenred.model;

public record CnpjEmpresaDados(
        String cnpj,
        String razaoSocial,
        String nomeFantasia,
        String cep,
        String logradouro,
        String numero,
        String bairro,
        String cidade,
        String uf,
        String situacaoCadastral) {
}
