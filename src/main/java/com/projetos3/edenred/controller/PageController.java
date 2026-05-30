package com.projetos3.edenred.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.projetos3.edenred.model.Empresa;
import com.projetos3.edenred.service.ImpactoService;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PageController {

    private final ImpactoService impactoService;

    public PageController(ImpactoService impactoService) {
        this.impactoService = impactoService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    // Verifica se empresa está logada na sessão
    private Empresa getEmpresaLogada(HttpSession session) {
        return (Empresa) session.getAttribute("empresaLogada");
    }

    @GetMapping("/impacto")
    public String impacto(HttpSession session, Model model) {
        Empresa empresa = getEmpresaLogada(session);
        if (empresa == null) {
            return "redirect:/login";
        }

        // Passa os dados da empresa para o template
        model.addAttribute("empresa", empresa);
        model.addAttribute("nomeEmpresa", empresa.getNome());
        model.addAttribute("colaboradores", empresa.getColaboradores());
        model.addAttribute("numeroBeneficios", empresa.getNumeroBeneficios());
        model.addAttribute("multibeneficio", empresa.isMultibeneficio());
        model.addAttribute("cartoesPorColaborador", empresa.getCartoesPorColaborador());
        model.addAttribute("totalCartoes", empresa.getTotalCartoesAtuais());
        model.addAttribute("digitalAtual", empresa.getPorcentagemDigitalAtual());

        return "impacto";
    }

    // Cálculo AJAX para a tela de impacto atual
    @PostMapping("/calcular-ajax")
    @ResponseBody
    public Map<String, String> calcularAjax(HttpSession session) {
        Map<String, String> resposta = new HashMap<>();
        try {
            Empresa empresa = getEmpresaLogada(session);
            if (empresa == null) {
                resposta.put("erro", "Sessão expirada. Faça login novamente.");
                return resposta;
            }

            return impactoService.calcularImpactoAtual(empresa);
        } catch (Exception e) {
            resposta.put("erro", "Erro no cálculo: " + e.getMessage());
            return resposta;
        }
    }

    @GetMapping("/simulacao")
    public String simulacao(HttpSession session, Model model) {
        Empresa empresa = getEmpresaLogada(session);
        if (empresa == null) {
            return "redirect:/login";
        }

        model.addAttribute("empresa", empresa);
        model.addAttribute("nomeEmpresa", empresa.getNome());
        model.addAttribute("digitalAtual", empresa.getPorcentagemDigitalAtual());

        
        return "simulacao";
    }

    @PostMapping("/simular-ajax")
    @ResponseBody
    public Map<String, Object> simularAjax(
            @RequestParam(required = false, defaultValue = "0") Integer porcentagemAlvo,
            HttpSession session) {

        Map<String, Object> resposta = new HashMap<>();

        try {
            Empresa empresa = getEmpresaLogada(session);
            if (empresa == null) {
                resposta.put("erro", "Sessão expirada. Faça login novamente.");
                return resposta;
            }

            return impactoService.simularImpacto(empresa, porcentagemAlvo);

        } catch (Exception e) {
            resposta.put("erro", "Erro no cálculo: " + e.getMessage());
            return resposta;
        }
    }
}
