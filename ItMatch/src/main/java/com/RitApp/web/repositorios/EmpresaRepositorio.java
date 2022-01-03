
package com.RitApp.web.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RitApp.web.entidades.Empresa;

@Repository
public interface EmpresaRepositorio extends JpaRepository<Empresa, String> {

}
