package com.projetos3.edenred.repository;

import com.projetos3.edenred.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, String> {
}
