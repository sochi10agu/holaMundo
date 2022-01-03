
package com.RitApp.web.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RitApp.web.entidades.Postulante;

@Repository
public interface PostulanteRepositorio extends JpaRepository<Postulante, String> {

}
