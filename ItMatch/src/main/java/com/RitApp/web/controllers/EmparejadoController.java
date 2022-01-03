package com.RitApp.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.RitApp.web.error.MyException;
import com.RitApp.web.servicios.EmparejadoService;

@Controller
@RequestMapping("/emparejado")
public class EmparejadoController {
	@Autowired
	EmparejadoService emparejadoService;

	@GetMapping("/listar")
	public String listar(Authentication usuario, Model modelo) {
		try {
			modelo.addAttribute("emparejados", emparejadoService.mostrarlikes(usuario.getName()));
		} catch (MyException e) {
			System.err.println("Ocurrió un error al listar emparejados");
			modelo.addAttribute("error", e.getMessage());
			return "/error";
		}

		return "listarlikes.html";
	}

	@GetMapping("/listaractivos")
	public String listaractivos(Authentication usuario, Model modelo) {
		try {
			modelo.addAttribute("emparejados", emparejadoService.mostrarlikeactivos(usuario.getName()));
		} catch (MyException e) {
			System.err.println("Ocurrió un error al listar activos emparejado controller");
			modelo.addAttribute("error", e.getMessage());
			return "/error";
		}
		return "listarlikes.html";
	}

	@PostMapping("/emparejarpostulante")
	public String emparejar(Authentication usuario, Model modelo, @RequestParam String id_trabajo) {
		System.out.print("entre");
		try {
			emparejadoService.emparejarPostulante(usuario.getName(), id_trabajo);
		} catch (MyException e) {
			System.err.println("Ocurrió un error" + "no se pudo emparejar");
			modelo.addAttribute("error", e.getMessage());
			return "/error";
		}
		return "redirect:/emparejado/listar";
	}

	@PostMapping("/emparejarempresa")
	public String emparejarempresa(Authentication usuario, @RequestParam String id_emparejado, Model modelo) {
		System.out.print("entre");
		try {
			emparejadoService.emparejarEmpresa(id_emparejado);
			return "redirect:/emparejado/listar";
		} catch (MyException e) {
			System.err.println("Ocurrió un error" + e.getMessage());
			modelo.addAttribute("error", e.getMessage());
			return "/error";
		}

	}
}
