package com.lib.ProyectLib.repositorios;

import com.lib.ProyectLib.entidades.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteRepositorio extends JpaRepository<Estudiante, String> {
    
}
