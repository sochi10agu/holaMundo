
package com.RitApp.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.RitApp.web.entidades.Usuario;
import com.RitApp.web.servicios.EmpresaServicio;
import com.RitApp.web.servicios.UsuarioServicio;

@Controller
@RequestMapping("/imagen")

public class ImagenController {
	@Autowired
	private EmpresaServicio empresaServicio;

	@Autowired
	private UsuarioServicio usuarioServicio;

	@GetMapping("/usuario/{id}")
	public ResponseEntity<byte[]> fotoEmpresa(@PathVariable String id) {
		try {
			Usuario usuario = usuarioServicio.buscaruserxid(id);
			if (usuario.getFoto() == null) {
				throw new Exception("El usuario no tiene foto asignada");
			}

			byte[] foto = usuario.getFoto();

			HttpHeaders headers = new HttpHeaders();
			if (usuario.getFoto().equals("image/jpeg")) {
				headers.setContentType(MediaType.IMAGE_JPEG);
			}
			if (usuario.getFoto().equals("image/png")) {
				headers.setContentType(MediaType.IMAGE_PNG);
			}

			return new ResponseEntity<>(foto, headers, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}
}
