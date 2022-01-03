
package com.RitApp.web.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RitApp.web.entidades.Perfil;

@Repository
public interface PerfilRepositorio extends JpaRepository<Perfil, String> {

}
