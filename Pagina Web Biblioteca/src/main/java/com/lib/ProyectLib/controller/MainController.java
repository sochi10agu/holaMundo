package com.lib.ProyectLib.controller;

import com.lib.ProyectLib.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class MainController {
    
    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("")
    public String inicio() {
        return "index.html";
    }

    @GetMapping("/registro")
    public String registro_postulante() {
        return "Registro";
    }

    @GetMapping("/login")
    public String login(HttpSession session, Authentication usuario, Model modelo,
            @RequestParam(required = false) String error) {
        if (error != null) {
            modelo.addAttribute("error", "Nombre de usuario o contraseña incorrecta");
        }
        return "login";
    }
    
    @GetMapping("/inicio")
    public String iniciosesion(HttpSession session, Authentication usuario, Model modelo) {
        try {
			modelo.addAttribute("estudiante", usuarioServicio.buscaruserxmail(usuario.getName()));
			
			return "/inicio";
		} catch (Exception e) {
			modelo.addAttribute("error", e.getMessage());
			return "/error";
		}
    }
}
