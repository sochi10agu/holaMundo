package com.RitApp.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.RitApp.web.entidades.Empresa;
import com.RitApp.web.entidades.Usuario;
import com.RitApp.web.error.MyException;
import com.RitApp.web.servicios.EmpresaServicio;
import com.RitApp.web.servicios.UsuarioServicio;

@Controller
@RequestMapping("/empresa")
public class EmpresaController {

	@Autowired
	UsuarioServicio usuarioServicio;
	@Autowired
	EmpresaServicio empresaServicio;

	@GetMapping("/registrar")
	public String crear() {

		return "registro_empresa";
	}

	@PostMapping("/registrar")
	public String crear(Model model, @RequestParam String nombre, @RequestParam String actividad,
			@RequestParam String email, @RequestParam String password, @RequestParam String password1) {
		try {
			empresaServicio.crearEmpresa(email, password, password1, nombre, actividad);
		} catch (MyException e) {
			System.out.println(e.getMessage());
			model.addAttribute("error", e.getMessage());
			return "registro_empresa";
		}
		return "redirect:/";
	}

	@GetMapping("/verPerfil")
	public String verPerfil() {
		return "perfilEmpresa.html";
	}

	@GetMapping("/modificarEmpresa")
	public ModelAndView modificarDatosEmpresa(Authentication usuario) {
		try {
			ModelAndView mav = new ModelAndView("/perfilEmpresa");
			Usuario user = usuarioServicio.buscaruserxmail(usuario.getName());
			Empresa empresa = empresaServicio.buscarxid(user.getId());
			mav.addObject("empresa", empresa);
			return mav;
		} catch (MyException e) {
			ModelAndView mav = new ModelAndView("/perfilEmpresa");
			mav.addObject("error", e.getMessage());
			return mav;
		}

	}

	@PostMapping("/modificandoEmpresa")
	public RedirectView modificandoEmpresa(Model model, Authentication usuario, @RequestParam String email,
			@RequestParam String nombre, @RequestParam String actividad, @RequestParam String sitioWeb,
			@RequestParam String beneficios, @RequestParam String sobreNosotros, @RequestParam String pais,
			MultipartFile logo) {
		try {
			Empresa empresa = empresaServicio.buscarxmail(usuario.getName());
			if (logo.getSize() == 0) {
				empresaServicio.modificarEmpresa(empresa.getId(), email, nombre, actividad, sitioWeb, beneficios,
						sobreNosotros, pais, null);
			} else {
				empresaServicio.modificarEmpresa(empresa.getId(), email, nombre, actividad, sitioWeb, beneficios,
						sobreNosotros, pais, logo);
			}
			return new RedirectView("/pagina_inicio");
		} catch (MyException e) {
			model.addAttribute("error", e.getMessage());
			return new RedirectView("/error");
		}
	}

}
