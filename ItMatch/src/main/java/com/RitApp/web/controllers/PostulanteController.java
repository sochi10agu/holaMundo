package com.RitApp.web.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.RitApp.web.entidades.Postulante;
import com.RitApp.web.entidades.Usuario;
import com.RitApp.web.error.MyException;
import com.RitApp.web.servicios.PerfilServicio;
import com.RitApp.web.servicios.PostulanteServicio;
import com.RitApp.web.servicios.TrabajoServicio;
import com.RitApp.web.servicios.UsuarioServicio;

@Controller
@RequestMapping("/postulante")
public class PostulanteController {

	@Autowired
	PostulanteServicio service;
	@Autowired
	TrabajoServicio trabajoServicio;
	@Autowired
	UsuarioServicio usuarioServicio;
	@Autowired
	PerfilServicio perfilServicio;
	@Autowired
	ErrorsController errorC;

	@GetMapping("/listar")

	public String listar(Model model) {
		try {

			model.addAttribute("usuarios", service.listar());
		} catch (MyException e) {
			System.err.println("error " + "error al listar Postulante");
			model.addAttribute("error ", e.getMessage());
			return "error";

		}
		return "listarUsuarios.html";
	}

	@PostMapping("/generico")
	public String generico() {

		return "/login";
	}

	@PostMapping("/crear")
	public String crear(Model modelo, @RequestParam String nombre, @RequestParam String apellido,
			@RequestParam String email, @RequestParam String contraseña, @RequestParam String contraseña1,
			@RequestParam String telefono, @RequestParam(required = false) String error) {
		try {
			service.crearPostulante(nombre, apellido, email, contraseña, contraseña1, telefono);
			return "index.html";
		} catch (MyException e) {
			System.err.println(e.getMessage());
			modelo.addAttribute("error", e.getMessage());
			return "registro_postulante";
		}

	}

	@GetMapping("/eliminar")
	public String eliminar(Model model, @RequestParam String id) {
		try {
			if (id != null) {
				service.eliminar(id);
			} else {
				throw new MyException("id null");
			}
		} catch (MyException e) {
			System.err.println("error " + " eliminar Postulante");
			model.addAttribute("error ", e.getMessage());
			return "error.html";
		}
		return "redirect:/usuario/listar";

	}

	@GetMapping("/darlike")
	public void darlike() {

	}

	@GetMapping("/verPerfil")
	public String verPerfil() {
		return "perfilPostulante.html";
	}

	@GetMapping("/modificarPerfil")
	public String modificarDatosPostulante(Model model, Authentication usuario) {
		try {

			Usuario user = usuarioServicio.buscaruserxmail(usuario.getName());
			System.out.println(usuario.getName());
			Postulante postulante = service.buscarXId(user.getId());

			model.addAttribute("postulante", postulante);

			return "perfilPostulante";
		} catch (MyException e) {

			System.err.println("error " + "Controller get modificarDatosPostulante");
			model.addAttribute("error ", e.getMessage());
			return "/error";

		}

	}

	@PostMapping("/modificandoPerfil")
	public RedirectView modificandoPostulantePerfil(Model model, Authentication usuario, @RequestParam String nombre,
			@RequestParam String apellido, @RequestParam String email, @RequestParam String telefono,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaNacimiento, @RequestParam Integer edad,
			@RequestParam String dni, @RequestParam String genero, @RequestParam String pais,
			@RequestParam String direccion, @RequestParam String lenguaje, @RequestParam String seniority,
			@RequestParam String idioma, @RequestParam String estudios, @RequestParam String algoSobreMi,
			MultipartFile foto) throws Exception {
		try {
			Postulante postu = service.buscaxmail(usuario.getName());
			if (foto.getSize() == 0) {
				service.modificar(postu.getId(), nombre, apellido, email, telefono, fechaNacimiento, edad, dni, genero,
						pais, direccion, lenguaje, seniority, idioma, estudios, algoSobreMi, null);
			} else {
				service.modificar(postu.getId(), nombre, apellido, email, telefono, fechaNacimiento, edad, dni, genero,
						pais, direccion, lenguaje, seniority, idioma, estudios, algoSobreMi, foto);
			}
			return new RedirectView("/pagina_inicio");
		} catch (MyException e) {
			System.err.println("error " + "Controller POST modificandoDatosPostulantePerfil");
			model.addAttribute("error ", e.getMessage());
			return new RedirectView("/error");

		}

	}
}
