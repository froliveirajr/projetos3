package com.projetos3.edenred.controller;

import com.projetos3.edenred.model.DadosEmpresaException;
import com.projetos3.edenred.service.EmpresaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    private final EmpresaService empresaService;

    public AdminController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    // Verifica se o usuário é admin
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

    private void popularDadosAdmin(Model model) {
        model.addAttribute("empresas", empresaService.listarEmpresas());
        model.addAttribute("totalEmpresas", empresaService.contarEmpresas());
        model.addAttribute("totalColaboradores", empresaService.contarColaboradores());
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
            model.addAttribute("sucesso", "Empresa '" + nome + "' cadastrada com sucesso!");
            popularDadosAdmin(model);
        } catch (DadosEmpresaException e) {
            model.addAttribute("erro", e.getMessage());
            popularDadosAdmin(model);
        }

        return "admin";
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
