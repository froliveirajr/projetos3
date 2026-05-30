package com.projetos3.edenred.controller;

import com.projetos3.edenred.model.CalculadoraCO2;
import com.projetos3.edenred.model.DadosEmpresaException;
import com.projetos3.edenred.model.Empresa;
import com.projetos3.edenred.model.OportunidadeDTO;
import com.projetos3.edenred.service.EmpresaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    private final EmpresaService empresaService;

    public AdminController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    private boolean isAdmin(HttpSession session) {
        Boolean admin = (Boolean) session.getAttribute("admin");
        return admin != null && admin;
    }

    @GetMapping("/admin")
    public String telaAdmin(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        popularDadosAdmin(model);
        return "admin";
    }

    @GetMapping("/admin/cadastrar")
    public String telaCadastro(HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        return "admin-cadastro";
    }

    private void popularDadosAdmin(Model model) {
        List<Empresa> empresas = empresaService.listarEmpresas();
        model.addAttribute("empresas", empresas);
        model.addAttribute("totalEmpresas", empresas.size());
        model.addAttribute("totalColaboradores", empresas.stream().mapToInt(Empresa::getColaboradores).sum());

        double digitalizacaoMedia = empresas.isEmpty() ? 0
                : empresas.stream().mapToDouble(Empresa::getPorcentagemDigitalAtual).average().orElse(0);
        model.addAttribute("digitalizacaoMedia", digitalizacaoMedia);

        int cartoesFisicosTotais = empresas.stream()
                .mapToInt(e -> (int) Math.round(e.getTotalCartoesAtuais()
                        * (1 - e.getPorcentagemDigitalAtual() / 100.0)))
                .sum();
        model.addAttribute("cartoesFisicosTotais", cartoesFisicosTotais);

        List<OportunidadeDTO> oportunidades = empresas.stream()
                .map(this::montarOportunidade)
                .sorted(Comparator.comparingDouble(OportunidadeDTO::getScore).reversed())
                .limit(5)
                .collect(Collectors.toList());
        model.addAttribute("oportunidades", oportunidades);

        int[] histograma = new int[4];
        for (Empresa e : empresas) {
            int bucket = Math.min((int) (e.getPorcentagemDigitalAtual() / 25), 3);
            histograma[bucket]++;
        }
        int histogramaMax = 0;
        for (int v : histograma) if (v > histogramaMax) histogramaMax = v;
        model.addAttribute("histograma", histograma);
        model.addAttribute("histogramaMax", Math.max(histogramaMax, 1));
    }

    private OportunidadeDTO montarOportunidade(Empresa e) {
        double lacunaDigital = 1 - e.getPorcentagemDigitalAtual() / 100.0;
        double bonus = (e.getNumeroBeneficios() < 3 ? 0.3 : 0)
                     + (!e.isMultibeneficio() ? 0.2 : 0);
        double score = e.getColaboradores() * lacunaDigital * (1 + bonus);

        String recomendacao;
        if (e.getPorcentagemDigitalAtual() < 30) {
            recomendacao = "Migrar " + (int) Math.round(100 - e.getPorcentagemDigitalAtual())
                    + "% restantes para digital";
        } else if (!e.isMultibeneficio()) {
            recomendacao = "Adotar cartão multibenefício";
        } else if (e.getNumeroBeneficios() < 3) {
            recomendacao = "Expandir portfólio (atualmente " + e.getNumeroBeneficios() + " benefícios)";
        } else {
            recomendacao = "Concluir migração para digital";
        }

        int colabsMigrando = (int) (e.getColaboradores() * lacunaDigital);
        int cartoesEvitadosPorAno = CalculadoraCO2.calcularCartoesEvitadosPorAno(
                colabsMigrando,
                e.getCartoesPorColaborador(),
                0,
                e.getVidaUtilCartaoAnos(),
                e.getTaxaTurnover(),
                e.getTaxaReemissao()
        );
        double co2EvitavelKg = CalculadoraCO2.calcularCO2AnualEvitado(cartoesEvitadosPorAno) / 1000.0;

        return new OportunidadeDTO(e, score, recomendacao, co2EvitavelKg);
    }

    @PostMapping("/admin/cadastrar")
    public String cadastrarEmpresa(@RequestParam String cnpj,
                                   @RequestParam String nome,
                                   @RequestParam String senha,
                                   @RequestParam int colaboradores,
                                   @RequestParam int numeroBeneficios,
                                   @RequestParam(required = false, defaultValue = "false") boolean multibeneficio,
                                   @RequestParam double vidaUtilCartaoAnos,
                                   @RequestParam double taxaTurnover,
                                   @RequestParam double taxaReemissao,
                                   @RequestParam int transacoesMensais,
                                   @RequestParam double porcentagemDigitalAtual,
                                   HttpSession session,
                                   Model model) {

        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }

        try {
            empresaService.cadastrarEmpresa(
                    cnpj, nome, senha, colaboradores,
                    numeroBeneficios, multibeneficio,
                    vidaUtilCartaoAnos, taxaTurnover,
                    taxaReemissao, transacoesMensais,
                    porcentagemDigitalAtual
            );
            return "redirect:/admin?cadastrado=" + java.net.URLEncoder.encode(nome, java.nio.charset.StandardCharsets.UTF_8);
        } catch (DadosEmpresaException e) {
            model.addAttribute("erro", e.getMessage());
            return "admin-cadastro";
        }
    }

    @PostMapping("/admin/remover")
    public String removerEmpresa(@RequestParam String cnpj, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }

        empresaService.removerEmpresa(cnpj);
        model.addAttribute("sucesso", "Empresa removida com sucesso!");

        popularDadosAdmin(model);
        return "admin";
    }
}
