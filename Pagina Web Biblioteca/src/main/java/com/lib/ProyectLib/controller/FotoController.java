package com.lib.ProyectLib.controller;

import com.lib.ProyectLib.entidades.Autor;
import com.lib.ProyectLib.entidades.Libro;
import com.lib.ProyectLib.entidades.Usuario;
import com.lib.ProyectLib.servicios.AutorServicio;
import com.lib.ProyectLib.servicios.LibroServicio;
import com.lib.ProyectLib.servicios.UsuarioServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/imagen")
public class FotoController {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private LibroServicio libroServicio;
    
    @GetMapping("/usuario/{id}")
	public ResponseEntity<byte[]> fotoEstudiante(@PathVariable String id) {
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
        
        @GetMapping("/autor/{id}")
	public ResponseEntity<byte[]> fotoAutor(@PathVariable String id) {
		try {
			Autor autor = autorServicio.buscarUnAutorxId(id);
			if (autor.getFoto() == null) {
				throw new Exception("El usuario no tiene foto asignada");
			}

			byte[] foto = autor.getFoto();

			HttpHeaders headers = new HttpHeaders();
			if (autor.getFoto().equals("image/jpeg")) {
				headers.setContentType(MediaType.IMAGE_JPEG);
			}
			if (autor.getFoto().equals("image/png")) {
				headers.setContentType(MediaType.IMAGE_PNG);
			}

			return new ResponseEntity<>(foto, headers, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}
        
        @GetMapping("/libro/{id}")
	public ResponseEntity<byte[]> caratulaLibro(@PathVariable String id) {
		try {
			Libro libro = libroServicio.buscarUnLibro(id);
			if (libro.getCaratula() == null) {
				throw new Exception("El libro no tiene caratula asignada");
			}

			byte[] caratula = libro.getCaratula();

			HttpHeaders headers = new HttpHeaders();
			if (libro.getCaratula().equals("image/jpeg")) {
				headers.setContentType(MediaType.IMAGE_JPEG);
			}
			if (libro.getCaratula().equals("image/png")) {
				headers.setContentType(MediaType.IMAGE_PNG);
			}

			return new ResponseEntity<>(caratula, headers, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}
}
