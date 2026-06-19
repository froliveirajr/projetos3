package com.projetos3.edenred.dados;

import com.projetos3.edenred.model.CnpjUtils;
import com.projetos3.edenred.model.DadosEmpresaException;
import com.projetos3.edenred.model.Empresa;
import java.util.ArrayList;

public class BancoEmMemoria {

    private static final ArrayList<Empresa> empresas = new ArrayList<>();

    static {
        empresas.add(new Empresa(
                "12.345.678/0001-99", "TechCorp Soluções", "senha123",
                5000, 3, true,
                5.0, 0.15, 0.10, 8, 20.0
        ));

        empresas.add(new Empresa(
                "98.765.432/0001-00", "Logística Avançada", "senha456",
                850, 2, false,
                4.0, 0.10, 0.05, 5, 65.0
        ));

        empresas.add(new Empresa(
                "11.222.333/0001-44", "Eco Start", "senha789",
                120, 1, false,
                5.0, 0.05, 0.02, 4, 92.0
        ));
    }

    private static final String ADMIN_CNPJ = "00.000.000/0001-00";
    private static final String ADMIN_SENHA = "admin123";

    public static void cadastrarEmpresa(Empresa empresa) throws DadosEmpresaException {
        if (empresa.getCnpj() == null || empresa.getCnpj().trim().isEmpty()) {
            throw new DadosEmpresaException("CNPJ é obrigatório.");
        }
        if (!CnpjUtils.isValido(empresa.getCnpj())) {
            throw new DadosEmpresaException("CNPJ inválido.");
        }
        if (empresa.getNome() == null || empresa.getNome().trim().isEmpty()) {
            throw new DadosEmpresaException("Nome da empresa é obrigatório.");
        }
        if (empresa.getSenha() == null || empresa.getSenha().trim().isEmpty()) {
            throw new DadosEmpresaException("Senha é obrigatória.");
        }
        if (empresa.getColaboradores() <= 0) {
            throw new DadosEmpresaException("Número de colaboradores deve ser maior que zero.");
        }
        if (empresa.getNumeroBeneficios() <= 0) {
            throw new DadosEmpresaException("Número de benefícios deve ser maior que zero.");
        }
        if (empresa.getVidaUtilCartaoAnos() <= 0) {
            throw new DadosEmpresaException("Vida útil do cartão deve ser maior que zero.");
        }

        empresa.setCnpj(CnpjUtils.formatar(empresa.getCnpj()));

        for (Empresa e : empresas) {
            if (CnpjUtils.somenteDigitos(e.getCnpj()).equals(CnpjUtils.somenteDigitos(empresa.getCnpj()))) {
                throw new DadosEmpresaException("CNPJ já cadastrado no sistema.");
            }
        }

        empresas.add(empresa);
    }

    public static Empresa buscarPorCnpj(String cnpj) {
        for (Empresa e : empresas) {
            if (CnpjUtils.somenteDigitos(e.getCnpj()).equals(CnpjUtils.somenteDigitos(cnpj))) {
                return e;
            }
        }
        return null;
    }

    public static Empresa autenticar(String cnpj, String senha) throws DadosEmpresaException {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new DadosEmpresaException("CNPJ é obrigatório.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new DadosEmpresaException("Senha é obrigatória.");
        }

        Empresa empresa = buscarPorCnpj(cnpj);
        if (empresa == null) {
            throw new DadosEmpresaException("Empresa não encontrada. Verifique o CNPJ.");
        }
        if (!empresa.getSenha().equals(senha)) {
            throw new DadosEmpresaException("Senha incorreta.");
        }

        return empresa;
    }

    public static boolean autenticarAdmin(String cnpj, String senha) {
        return ADMIN_CNPJ.equals(cnpj) && ADMIN_SENHA.equals(senha);
    }

    public static ArrayList<Empresa> listarEmpresas() {
        return empresas;
    }

    public static void removerEmpresa(String cnpj) {
        empresas.removeIf(e -> CnpjUtils.somenteDigitos(e.getCnpj()).equals(CnpjUtils.somenteDigitos(cnpj)));
    }

    public static void atualizarEmpresa(String cnpjAntigo, Empresa novosDados) throws DadosEmpresaException {
        String cnpjAntigoNormalizado = CnpjUtils.somenteDigitos(cnpjAntigo);
        novosDados.setCnpj(CnpjUtils.formatar(novosDados.getCnpj()));

        for (int i = 0; i < empresas.size(); i++) {
            Empresa atual = empresas.get(i);
            if (!CnpjUtils.somenteDigitos(atual.getCnpj()).equals(cnpjAntigoNormalizado)) {
                continue;
            }

            if (!cnpjAntigoNormalizado.equals(CnpjUtils.somenteDigitos(novosDados.getCnpj()))
                    && buscarPorCnpj(novosDados.getCnpj()) != null) {
                throw new DadosEmpresaException("Novo CNPJ já cadastrado.");
            }

            empresas.set(i, novosDados);
            return;
        }

        throw new DadosEmpresaException("Empresa não encontrada para atualização.");
    }

    public static String getAdminCnpj() {
        return ADMIN_CNPJ;
    }
}
