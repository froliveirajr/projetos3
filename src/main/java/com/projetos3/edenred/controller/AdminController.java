package com.projetos3.edenred.controller;

import com.projetos3.edenred.model.CalculadoraCO2;
import com.projetos3.edenred.model.CnpjEmpresaDados;
import com.projetos3.edenred.model.DadosEmpresaException;
import com.projetos3.edenred.model.Empresa;
import com.projetos3.edenred.model.OportunidadeDTO;
import com.projetos3.edenred.model.RelatorioDigitalizacao;
import com.projetos3.edenred.repository.RelatorioDigitalizacaoRepository;
import com.projetos3.edenred.service.CnpjConsultaService;
import com.projetos3.edenred.service.EmpresaService;
import jakarta.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminController {

    private final EmpresaService empresaService;
    private final RelatorioDigitalizacaoRepository relatorioRepository;
    private final CnpjConsultaService cnpjConsultaService;

    public AdminController(EmpresaService empresaService,
                           RelatorioDigitalizacaoRepository relatorioRepository,
                           CnpjConsultaService cnpjConsultaService) {
        this.empresaService = empresaService;
        this.relatorioRepository = relatorioRepository;
        this.cnpjConsultaService = cnpjConsultaService;
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
    public String telaCadastro(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        popularParametrosPadrao(model);
        popularFormularioCadastro(
                model,
                "", "", "", "", "", "", "", "", "", "",
                0, 0, true,
                empresaService.getTransacoesPadrao(),
                empresaService.getVidaUtilPadrao(),
                empresaService.getTurnoverPadrao(),
                empresaService.getReemissaoPadrao(),
                0
        );
        return "admin-cadastro";
    }

    @GetMapping("/admin/cnpj-consulta")
    @ResponseBody
    public ResponseEntity<?> consultarCnpj(@RequestParam String cnpj, HttpSession session) {
        if (!isAdmin(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("erro", "Sessao administrativa expirada."));
        }

        try {
            CnpjEmpresaDados dados = cnpjConsultaService.consultar(cnpj);
            Map<String, Object> resposta = new LinkedHashMap<>();
            resposta.put("cnpj", dados.cnpj());
            resposta.put("razaoSocial", dados.razaoSocial());
            resposta.put("nomeFantasia", dados.nomeFantasia());
            resposta.put("cep", dados.cep());
            resposta.put("logradouro", dados.logradouro());
            resposta.put("numero", dados.numero());
            resposta.put("bairro", dados.bairro());
            resposta.put("cidade", dados.cidade());
            resposta.put("uf", dados.uf());
            resposta.put("situacaoCadastral", dados.situacaoCadastral());
            return ResponseEntity.ok(resposta);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(Map.of("erro", ex.getMessage()));
        }
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
        for (int v : histograma) {
            if (v > histogramaMax) {
                histogramaMax = v;
            }
        }
        model.addAttribute("histograma", histograma);
        model.addAttribute("histogramaMax", Math.max(histogramaMax, 1));

        List<RelatorioDigitalizacao> relatoriosRecentes = relatorioRepository.findTop10ByOrderByDataGeracaoDesc();
        model.addAttribute("relatoriosRecentes", relatoriosRecentes);
        model.addAttribute("totalRelatorios", relatorioRepository.count());
    }

    private void popularParametrosPadrao(Model model) {
        model.addAttribute("transacoesPadrao", empresaService.getTransacoesPadrao());
        model.addAttribute("vidaUtilPadrao", empresaService.getVidaUtilPadrao());
        model.addAttribute("turnoverPadrao", empresaService.getTurnoverPadrao());
        model.addAttribute("reemissaoPadrao", empresaService.getReemissaoPadrao());
    }

    private void popularFormularioCadastro(Model model,
                                           String cnpj,
                                           String nome,
                                           String nomeFantasia,
                                           String cep,
                                           String logradouro,
                                           String numeroEndereco,
                                           String bairro,
                                           String cidade,
                                           String uf,
                                           String situacaoCadastral,
                                           int colaboradores,
                                           int numeroBeneficios,
                                           boolean multibeneficio,
                                           int transacoesMensais,
                                           double vidaUtilCartaoAnos,
                                           double taxaTurnover,
                                           double taxaReemissao,
                                           double porcentagemDigitalAtual) {
        model.addAttribute("cnpj", cnpj);
        model.addAttribute("nome", nome);
        model.addAttribute("nomeFantasia", nomeFantasia);
        model.addAttribute("cep", cep);
        model.addAttribute("logradouro", logradouro);
        model.addAttribute("numeroEndereco", numeroEndereco);
        model.addAttribute("bairro", bairro);
        model.addAttribute("cidade", cidade);
        model.addAttribute("uf", uf);
        model.addAttribute("situacaoCadastral", situacaoCadastral);
        model.addAttribute("enderecoResumo", montarResumo(" - ", logradouro, numeroEndereco, bairro));
        model.addAttribute("cidadeUfResumo", montarResumo(" / ", cidade, uf));
        model.addAttribute("colaboradores", colaboradores > 0 ? colaboradores : null);
        model.addAttribute("numeroBeneficios", numeroBeneficios > 0 ? numeroBeneficios : null);
        model.addAttribute("multibeneficio", multibeneficio);
        model.addAttribute("transacoesMensais", transacoesMensais);
        model.addAttribute("vidaUtilCartaoAnos", vidaUtilCartaoAnos);
        model.addAttribute("taxaTurnover", taxaTurnover);
        model.addAttribute("taxaReemissao", taxaReemissao);
        model.addAttribute("porcentagemDigitalAtual", porcentagemDigitalAtual);
    }

    private String montarResumo(String separador, String... valores) {
        return java.util.Arrays.stream(valores)
                .filter(valor -> valor != null && !valor.isBlank())
                .collect(Collectors.joining(separador));
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
            recomendacao = "Adotar cartao multibeneficio";
        } else if (e.getNumeroBeneficios() < 3) {
            recomendacao = "Expandir portfolio (atualmente " + e.getNumeroBeneficios() + " beneficios)";
        } else {
            recomendacao = "Concluir migracao para digital";
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
                                   @RequestParam(required = false, defaultValue = "") String nomeFantasia,
                                   @RequestParam(required = false, defaultValue = "") String cep,
                                   @RequestParam(required = false, defaultValue = "") String logradouro,
                                   @RequestParam(required = false, defaultValue = "") String numeroEndereco,
                                   @RequestParam(required = false, defaultValue = "") String bairro,
                                   @RequestParam(required = false, defaultValue = "") String cidade,
                                   @RequestParam(required = false, defaultValue = "") String uf,
                                   @RequestParam(required = false, defaultValue = "") String situacaoCadastral,
                                   @RequestParam int colaboradores,
                                   @RequestParam int numeroBeneficios,
                                   @RequestParam(required = false, defaultValue = "false") boolean multibeneficio,
                                   @RequestParam int transacoesMensais,
                                   @RequestParam double vidaUtilCartaoAnos,
                                   @RequestParam double taxaTurnover,
                                   @RequestParam double taxaReemissao,
                                   @RequestParam double porcentagemDigitalAtual,
                                   @RequestParam(defaultValue = "salvar") String acao,
                                   HttpSession session,
                                   Model model) {

        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }

        try {
            Empresa empresa = empresaService.salvarProspeccao(
                    cnpj,
                    nome,
                    nomeFantasia,
                    cep,
                    logradouro,
                    numeroEndereco,
                    bairro,
                    cidade,
                    uf,
                    situacaoCadastral,
                    colaboradores,
                    numeroBeneficios,
                    multibeneficio,
                    transacoesMensais,
                    vidaUtilCartaoAnos,
                    taxaTurnover,
                    taxaReemissao,
                    porcentagemDigitalAtual
            );

            if ("simular".equals(acao)) {
                session.setAttribute("empresaLogada", empresa);
                session.setAttribute("admin", true);
                return "redirect:/impacto";
            }

            return "redirect:/admin?cadastrado="
                    + java.net.URLEncoder.encode(nome, StandardCharsets.UTF_8);
        } catch (DadosEmpresaException e) {
            popularParametrosPadrao(model);
            popularFormularioCadastro(
                    model,
                    cnpj,
                    nome,
                    nomeFantasia,
                    cep,
                    logradouro,
                    numeroEndereco,
                    bairro,
                    cidade,
                    uf,
                    situacaoCadastral,
                    colaboradores,
                    numeroBeneficios,
                    multibeneficio,
                    transacoesMensais,
                    vidaUtilCartaoAnos,
                    taxaTurnover,
                    taxaReemissao,
                    porcentagemDigitalAtual
            );
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
