package com.projetos3.edenred.controller;

import com.projetos3.edenred.model.Empresa;
import com.projetos3.edenred.model.DadosEmpresaException;
import com.projetos3.edenred.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String telaLogin(HttpSession session) {
        if (session.getAttribute("empresaLogada") != null) {
            return "redirect:/impacto";
        }
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
            return "redirect:/impacto";
        } catch (DadosEmpresaException e) {
            model.addAttribute("erro", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/admin/login")
    public String telaLoginAdmin(HttpSession session) {
        if (session.getAttribute("admin") != null && (Boolean) session.getAttribute("admin")) {
            return "redirect:/admin";
        }
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
            return "redirect:/admin";
        }

        model.addAttribute("erro", "CNPJ ou senha administrativa inválidos.");
        return "admin-login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
