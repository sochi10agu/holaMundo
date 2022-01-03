package com.RitApp.web.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RitApp.web.entidades.Mensaje;

@Repository
public interface MensajeRepositorio extends JpaRepository<Mensaje, String> {

}
