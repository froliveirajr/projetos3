package com.projetos3.edenred.service;

import com.projetos3.edenred.model.CnpjEmpresaDados;
import com.projetos3.edenred.model.CnpjUtils;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class CnpjConsultaService {

    private final RestClient restClient;

    public CnpjConsultaService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://brasilapi.com.br")
                .build();
    }

    public CnpjEmpresaDados consultar(String cnpj) {
        String digitos = CnpjUtils.somenteDigitos(cnpj);
        if (!CnpjUtils.isValido(digitos)) {
            throw new IllegalArgumentException("CNPJ inválido. Verifique os 14 dígitos informados.");
        }

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> resposta = restClient.get()
                    .uri("/api/cnpj/v1/{cnpj}", digitos)
                    .retrieve()
                    .body(Map.class);

            if (resposta == null || resposta.isEmpty()) {
                throw new IllegalStateException("Não foi possível obter dados para este CNPJ.");
            }

            return new CnpjEmpresaDados(
                    CnpjUtils.formatar(digitos),
                    valor(resposta.get("razao_social")),
                    valor(resposta.get("nome_fantasia")),
                    valor(resposta.get("cep")),
                    valor(resposta.get("logradouro")),
                    valor(resposta.get("numero")),
                    valor(resposta.get("bairro")),
                    valor(resposta.get("municipio")),
                    valor(resposta.get("uf")),
                    valor(resposta.get("descricao_situacao_cadastral")));
        } catch (RestClientException ex) {
            throw new IllegalStateException("Não foi possível consultar o CNPJ agora. Tente novamente em instantes.");
        }
    }

    private String valor(Object valor) {
        return valor == null ? "" : String.valueOf(valor).trim();
    }
}
