package com.lib.ProyectLib.controller;

import com.lib.ProyectLib.entidades.Autor;
import com.lib.ProyectLib.servicios.AutorServicio;
import com.lib.ProyectLib.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
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
@RequestMapping("/autor")
public class AutorController {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private AutorServicio autorServicio;
    
    @GetMapping("/crear")
    public String crearAutor(HttpSession session, Authentication usuario, Model modelo) {
        try {
        modelo.addAttribute("estudiante", usuarioServicio.buscaruserxmail(usuario.getName()));
        return "autorCrear";
        } catch (Exception e) {
			modelo.addAttribute("error", e.getMessage());
			return "/error";
		}
    }
    
    @PostMapping("/creando")
    public String crear(Model modelo, @RequestParam String nombre, @RequestParam MultipartFile foto, @RequestParam(required = false) String error, HttpSession session, Authentication usuario) {
        try {
            modelo.addAttribute("estudiante", usuarioServicio.buscaruserxmail(usuario.getName()));
            autorServicio.crearAutor(nombre, foto);
            return "/inicio";
        } catch (Exception e) {
            System.err.println(e.getMessage());
            modelo.addAttribute("error", e.getMessage());
            return "index.html";
        }

    }
    
     @GetMapping("/listar")
    public String listarAutores(HttpSession session, Authentication usuario, Model modelo) throws Exception {
        try {
        modelo.addAttribute("estudiante", usuarioServicio.buscaruserxmail(usuario.getName()));
        modelo.addAttribute("lista", autorServicio.listarTodosAutores());
        return "listarAutores";
        } catch (Exception e) {
			throw new Exception("Error al listar Autor en AutorServicio");
		}
    }
    
    @GetMapping("/listar2")
    public String listarAutores2(Model modelo) throws Exception {
        try {
        
        modelo.addAttribute("lista", autorServicio.listarTodosAutores());
        return "listarAutores";
        } catch (Exception e) {
			throw new Exception("Error al listar Autor en AutorServicio");
		}
    }
    
    @PostMapping("/modificar")
    public String modificarDatosEstudiante(HttpSession session, Authentication usuario, Model model, String id) {
        try {
            model.addAttribute("estudiante", usuarioServicio.buscaruserxmail(usuario.getName()));
            Autor autor = autorServicio.buscarUnAutorxId(id);

            model.addAttribute("autor", autor);

            return "autorModificar";
        } catch (Exception e) {

            System.err.println("error " + "Controller get modificarDatosPostulante");
            model.addAttribute("error ", e.getMessage());
            return "/error";

        }

    }
    
    @PostMapping("/modificando")
	public RedirectView modificandoPerfilEstudiante(HttpSession session, Authentication usuario, Model model, @RequestParam String id, @RequestParam String nombre,
			MultipartFile foto) throws Exception {
		try {
                    model.addAttribute("estudiante", usuarioServicio.buscaruserxmail(usuario.getName()));
			if (foto.getSize() == 0) {
                            	autorServicio.modificarAutor(nombre, id, null);
			} else {
				autorServicio.modificarAutor(nombre, id, foto);
			}
			return new RedirectView("/inicio");
		} catch (Exception e) {
			System.err.println("error " + "Controller POST modificandoDatosPostulantePerfil");
			model.addAttribute("error ", e.getMessage());
			return new RedirectView("/");

		}

	}
}
