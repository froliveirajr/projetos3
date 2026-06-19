package com.projetos3.edenred.controller;

import com.projetos3.edenred.model.DadosEmpresaException;
import com.projetos3.edenred.model.Empresa;
import com.projetos3.edenred.service.DemoModeService;
import com.projetos3.edenred.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final LoginService loginService;
    private final DemoModeService demoModeService;

    public LoginController(LoginService loginService, DemoModeService demoModeService) {
        this.loginService = loginService;
        this.demoModeService = demoModeService;
    }

    @GetMapping("/login")
    public String telaLogin(HttpSession session, Model model) {
        if (session.getAttribute("empresaLogada") != null) {
            return "redirect:/impacto";
        }

        model.addAttribute("empresaDemoEnabled", demoModeService.isEmpresaDemoEnabled());
        model.addAttribute("adminDemoEnabled", demoModeService.isAdminDemoEnabled());
        return "login";
    }

    @PostMapping("/login")
    public String fazerLogin(@RequestParam String cnpj,
                             @RequestParam String senha,
                             HttpSession session,
                             Model model) {

        try {
            Empresa empresa = loginService.autenticarEmpresa(cnpj, senha);
            session.setAttribute("empresaLogada", empresa);
            session.setAttribute("admin", false);
            session.setAttribute("demoMode", false);
            session.setAttribute("demoScope", null);
            return "redirect:/impacto";
        } catch (DadosEmpresaException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("empresaDemoEnabled", demoModeService.isEmpresaDemoEnabled());
            model.addAttribute("adminDemoEnabled", demoModeService.isAdminDemoEnabled());
            return "login";
        }
    }

    @PostMapping("/login/demo")
    public String fazerLoginDemoEmpresa(HttpSession session, Model model) {
        if (!demoModeService.isEmpresaDemoEnabled()) {
            model.addAttribute("erro", "A demonstração para empresas está desativada no momento.");
            model.addAttribute("empresaDemoEnabled", false);
            model.addAttribute("adminDemoEnabled", demoModeService.isAdminDemoEnabled());
            return "login";
        }

        session.setAttribute("empresaLogada", demoModeService.criarEmpresaDemo());
        session.setAttribute("admin", false);
        session.setAttribute("demoMode", true);
        session.setAttribute("demoScope", "empresa");
        return "redirect:/impacto";
    }

    @GetMapping("/admin/login")
    public String telaLoginAdmin(HttpSession session, Model model) {
        if (session.getAttribute("admin") != null && (Boolean) session.getAttribute("admin")) {
            return "redirect:/admin";
        }

        model.addAttribute("adminDemoEnabled", demoModeService.isAdminDemoEnabled());
        return "admin-login";
    }

    @PostMapping("/admin/login")
    public String fazerLoginAdmin(@RequestParam String cnpj,
                                  @RequestParam String senha,
                                  HttpSession session,
                                  Model model) {

        if (loginService.autenticarAdmin(cnpj, senha)) {
            session.setAttribute("admin", true);
            session.setAttribute("empresaLogada", null);
            session.setAttribute("demoMode", false);
            session.setAttribute("demoScope", null);
            return "redirect:/admin";
        }

        model.addAttribute("erro", "CNPJ ou senha administrativa inválidos.");
        model.addAttribute("adminDemoEnabled", demoModeService.isAdminDemoEnabled());
        return "admin-login";
    }

    @PostMapping("/admin/login/demo")
    public String fazerLoginAdminDemo(HttpSession session, Model model) {
        if (!demoModeService.isAdminDemoEnabled()) {
            model.addAttribute("erro", "A demonstração administrativa está desativada no momento.");
            model.addAttribute("adminDemoEnabled", false);
            return "admin-login";
        }

        session.setAttribute("admin", true);
        session.setAttribute("empresaLogada", null);
        session.setAttribute("demoMode", true);
        session.setAttribute("demoScope", "admin");
        return "redirect:/admin";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
