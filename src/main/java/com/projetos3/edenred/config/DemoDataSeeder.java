package com.projetos3.edenred.config;

import com.projetos3.edenred.model.Empresa;
import com.projetos3.edenred.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoDataSeeder implements CommandLineRunner {

    private final EmpresaRepository empresaRepository;
    private final boolean seedDemoData;

    public DemoDataSeeder(EmpresaRepository empresaRepository,
                          @Value("${app.seed-demo-data}") boolean seedDemoData) {
        this.empresaRepository = empresaRepository;
        this.seedDemoData = seedDemoData;
    }

    @Override
    public void run(String... args) {
        if (!seedDemoData || empresaRepository.count() > 0) {
            return;
        }

        empresaRepository.save(new Empresa(
                "12.345.678/0001-99", "TechCorp Solucoes", "senha123",
                5000, 3, true,
                5.0, 0.15, 0.10, 8, 20.0
        ));
        empresaRepository.save(new Empresa(
                "98.765.432/0001-00", "Logistica Avancada", "senha456",
                850, 2, false,
                4.0, 0.10, 0.05, 5, 65.0
        ));
        empresaRepository.save(new Empresa(
                "11.222.333/0001-44", "Eco Start", "senha789",
                120, 1, false,
                5.0, 0.05, 0.02, 4, 92.0
        ));
    }
}
