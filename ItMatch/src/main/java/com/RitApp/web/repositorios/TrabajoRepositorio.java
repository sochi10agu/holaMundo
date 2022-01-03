
package com.RitApp.web.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.RitApp.web.entidades.Trabajo;

@Repository
public interface TrabajoRepositorio extends JpaRepository<Trabajo, String> {
	@Query("SELECT a from Trabajo a WHERE a.id LIKE :id")
	public Trabajo buscarPorid(@Param("id") String id);
}
