package com.RitApp.web.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.RitApp.web.entidades.Trabajo;
import com.RitApp.web.error.MyException;
import com.RitApp.web.servicios.TrabajoServicio;

@Controller
@RequestMapping("/trabajo")
public class TrabajoController {

	@Autowired
	TrabajoServicio servicio;

	@GetMapping("/listarTrabajos")
	public String listar(Model model, Authentication usuario) {
		try {
			List<Trabajo> lista_trabajo = new ArrayList<Trabajo>();
			lista_trabajo = servicio.listarTrabajos(usuario.getName());
			System.out.println("xxx");
			if (lista_trabajo.isEmpty()) {
				model.addAttribute("mensaje", "NO HAY TRABAJOS NUEVOS");
			}
			model.addAttribute("trabajos", lista_trabajo);

			return "listarTrabajos.html";
		} catch (MyException e) {
			model.addAttribute("error", e.getMessage());
			return "/error";
		}
	}
// para mostrar trabajos en caso de no estar logueado

	@GetMapping("/listarTrabajos2")
	public String listar(Model model) {
		
			if (servicio.trabajos().isEmpty()) {
				model.addAttribute("mensaje", "NO HAY TRABAJOS NUEVOS");
			}
			model.addAttribute("trabajos", servicio.trabajos());

			return "listarTrabajos.html";
	
	}

	@GetMapping("/crearTrabajo")
	public String creando() {
		return "trabajoCrear.html";
	}

	@PostMapping("/crearTrabajo")
	public String crear(Authentication usuario, Model modelo, @RequestParam String puesto, @RequestParam String zona,
			@RequestParam String lenguaje, @RequestParam String modalidad) {
		try {
			servicio.crearTrabajo(usuario, puesto, zona, modalidad, lenguaje);

			return "redirect:/trabajo/listarTrabajos";
		} catch (MyException e) {
			modelo.addAttribute("error", e.getMessage());
			return "/error";
		}
	}

	@GetMapping("/eliminarTrabajo")
	public String eliminar(@RequestParam String id, Model model){
		try {
			if (id != null) {
				servicio.eliminar(id);
			} else {
				System.out.println("tipo null");
			}
		} catch (MyException e) {
			model.addAttribute("error", e.getMessage());
			return "/error";
		}
		return "redirect:/trabajo/listar";

	}

	/*
	 * @PostMapping("/modificarTrabajo") public String modificarTrabajo(Model
	 * modelo, @RequestParam String id) throws Exception { try { Trabajo trabajo =
	 * new Trabajo(); if(id!=null) { trabajo= servicio.buscarXTipo(id); }
	 * modelo.addAttribute("trabajo", trabajo); } catch (Exception e) {
	 * System.out.println("error " + e.getMessage()); modelo.addAttribute("error ",
	 * e.getMessage()); return "error.html"; } return "modificarTrabajo.html"; }
	 */

}