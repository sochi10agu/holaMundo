package com.lib.ProyectLib.controller;

import com.lib.ProyectLib.entidades.Estudiante;
import com.lib.ProyectLib.entidades.Usuario;
import com.lib.ProyectLib.servicios.EstudianteServicio;
import com.lib.ProyectLib.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    @Autowired
    private EstudianteServicio estudianteServicio;
    
    @Autowired
    private UsuarioServicio usuarioServicio;

    @PostMapping("/crear")
    public String crear(Model modelo, @RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String email, @RequestParam String password, @RequestParam String contraseña1,
            @RequestParam(required = false) String error) {
        try {
            estudianteServicio.crearEstudiante(nombre, apellido, password, contraseña1, email);
            return "index.html";
        } catch (Exception e) {
            System.err.println(e.getMessage());
            modelo.addAttribute("error", e.getMessage());
            return "index.html";
        }

    }

    @GetMapping("/perfil")
    public String modificarDatosEstudiante(Model model, Authentication usuario) {
        try {

            Usuario user = usuarioServicio.buscaruserxmail(usuario.getName());
            System.out.println(usuario.getName());
            Estudiante estudiante = estudianteServicio.buscarXId(user.getId());

            model.addAttribute("estudiante", estudiante);

            return "perfil";
        } catch (Exception e) {

            System.err.println("error " + "Controller get modificarDatosPostulante");
            model.addAttribute("error ", e.getMessage());
            return "/error";

        }

    }
    
    @PostMapping("/modificarPerfil")
	public RedirectView modificandoPerfilEstudiante(Model model, Authentication usuario, @RequestParam String nombre,
			@RequestParam String apellido, @RequestParam String email, @RequestParam String dni, @RequestParam String domicilio, @RequestParam Long telefono, MultipartFile foto) throws Exception {
		try {
			Estudiante estu = estudianteServicio.buscaxmail(usuario.getName());
			if (foto.getSize() == 0) {
                            	estudianteServicio.modificar(estu.getId(), nombre, apellido, email, telefono, dni, domicilio, null);
			} else {
				estudianteServicio.modificar(estu.getId(), nombre, apellido, email, telefono, dni, domicilio, foto);
			}
			return new RedirectView("/inicio");
		} catch (Exception e) {
			System.err.println("error " + "Controller POST modificandoDatosPostulantePerfil");
			model.addAttribute("error ", e.getMessage());
			return new RedirectView("/");

		}

	}
}
