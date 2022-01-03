package com.RitApp.web.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.RitApp.web.entidades.Emparejado;
import com.RitApp.web.entidades.Empresa;
import com.RitApp.web.entidades.Postulante;
import com.RitApp.web.entidades.Trabajo;

@Repository
public interface EmparejadoRepositorio extends JpaRepository<Emparejado, String> {
	@Query("SELECT a from Emparejado a WHERE a.id LIKE :id")
	public Emparejado buscarPorid(@Param("id") String id);

	List<Emparejado> findByEmpresa(Empresa empresa);

	List<Emparejado> findByPostulante(Postulante postulante);

	List<Emparejado> findByTrabajo(Trabajo trabajo);

	List<Emparejado> findByPostulanteAndTrabajo(Postulante postulante, Trabajo trabajo);
}