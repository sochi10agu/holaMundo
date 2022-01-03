package com.RitApp.web.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.RitApp.web.entidades.Chat;
import com.RitApp.web.entidades.Emparejado;

@Repository
public interface ChatRepositorio extends JpaRepository<Chat, String> {
	@Query("SELECT a from Chat a WHERE a.id LIKE :id")
	public Chat buscarPorid(@Param("id") String id);

	List<Chat> findByEmparejado(Emparejado emparejado);
}
