package com.lib.ProyectLib.controller;

import com.lib.ProyectLib.entidades.Autor;
import com.lib.ProyectLib.entidades.Estudiante;
import com.lib.ProyectLib.entidades.Libro;
import com.lib.ProyectLib.entidades.Prestamo;
import com.lib.ProyectLib.entidades.Usuario;
import com.lib.ProyectLib.servicios.AutorServicio;
import com.lib.ProyectLib.servicios.LibroServicio;
import com.lib.ProyectLib.servicios.UsuarioServicio;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/libro")
public class LibroController {

    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/crear")
    public ModelAndView mostrarFormulario(HttpSession session, Authentication usuario, Model modelo) throws Exception {
        try {
            modelo.addAttribute("estudiante", usuarioServicio.buscaruserxmail(usuario.getName()));
            ModelAndView mav = new ModelAndView("libroNuevo");
            mav.addObject("autores", autorServicio.listarTodosAutores());

            return mav;
        } catch (Exception e) {
            throw new Exception("Error en LibroControlador - mostrarFormulario");
        }

    }

    @PostMapping("/guardar")
    public RedirectView guardarLibro(HttpSession session, Authentication usuario, Model modelo, @RequestParam String titulo, @RequestParam String autor, @RequestParam String editorial, @RequestParam Integer año, @RequestParam Integer totales, MultipartFile caratula) throws Exception {
        try {
            modelo.addAttribute("estudiante", usuarioServicio.buscaruserxmail(usuario.getName()));
            Autor autorObj = autorServicio.buscarUnAutorxId(autor);

            libroServicio.crearLibro(titulo, año, autorObj, editorial, totales, caratula);
            return new RedirectView("/inicio");
        } catch (Exception e) {
            throw new Exception("Error en LibroControlador - guardarLibro");
        }

    }

    @GetMapping("/verLibros")
    public ModelAndView buscarLibros(HttpSession session, Authentication usuario, Model modelo) throws Exception {
        try {
            modelo.addAttribute("estudiante", usuarioServicio.buscaruserxmail(usuario.getName()));
            ModelAndView mav = new ModelAndView("libros");

            List<Libro> libros = libroServicio.imprimirLibros();

            mav.addObject("libros", libros);
            return mav;
        } catch (Exception e) {
            throw new Exception("Error LibroControlador - buscarLibros");
        }

    }

    @GetMapping("/verLibros2")
    public ModelAndView buscarLibros2() throws Exception {
        try {

            ModelAndView mav = new ModelAndView("libros");

            List<Libro> libros = libroServicio.imprimirLibros();

            mav.addObject("libros", libros);
            return mav;
        } catch (Exception e) {
            throw new Exception("Error LibroControlador - buscarLibros");
        }

    }

    @PostMapping("/modificar")
    public String modificarDatosLibro(HttpSession session, Model model, Authentication usuario, String isbn) {
        try {
            Libro libro = libroServicio.buscarUnLibro(isbn);
            model.addAttribute("estudiante", usuarioServicio.buscaruserxmail(usuario.getName()));
            model.addAttribute("libro", libro);
            model.addAttribute("autores", autorServicio.listarTodosAutores());

            return "libroModificar";
        } catch (Exception e) {

            System.err.println("error " + "Controller get modificarDatosPostulante");
            model.addAttribute("error ", e.getMessage());
            return "/error";

        }

    }

    @PostMapping("/modificando")
    public RedirectView modificandoDatosLibros(HttpSession session, Model model, Authentication usuario, @RequestParam String isbn,
            @RequestParam String titulo, @RequestParam String editorial, @RequestParam String autor,
            @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer prestados,
            @RequestParam Integer disponibles, MultipartFile caratula) throws Exception {
        try {
            model.addAttribute("estudiante", usuarioServicio.buscaruserxmail(usuario.getName()));
            if (caratula.getSize() == 0) {
                libroServicio.modificandoLibro(titulo, anio, autor, editorial, ejemplares, isbn, prestados, disponibles, null);
            } else {
                libroServicio.modificandoLibro(titulo, anio, autor, editorial, ejemplares, isbn, prestados, disponibles, caratula);
            }
            return new RedirectView("/inicio");
        } catch (Exception e) {
            System.err.println("error " + "Controller POST modificandoDatosPostulantePerfil");
            model.addAttribute("error ", e.getMessage());
            return new RedirectView("/");

        }

    }

       
    @PostMapping("/alquilar")
    public String alquilarLibro(HttpSession session, Model model, Authentication usuario, @RequestParam String isbn) {
        try {
            model.addAttribute("estudiante", usuarioServicio.buscaruserxmail(usuario.getName()));
            model.addAttribute("isbn", isbn);
            
            return "prestamo";
        } catch (Exception e) {

            System.err.println("error " + "Controller get modificarDatosPostulante");
            model.addAttribute("error ", e.getMessage());
            return "/error";

        }

    }
    
    @PostMapping("/crearPrestamo")
    public RedirectView crearPrestamo(HttpSession session, Model model, Authentication usuario, @RequestParam String idLibro, @RequestParam String idUsuario, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaPrestamo) {
        try {
            model.addAttribute("estudiante", usuarioServicio.buscaruserxmail(usuario.getName()));
            libroServicio.crearPrestamo(idUsuario, idLibro, fechaPrestamo);
                        
            return new RedirectView("/libro/misPrestamos");
        } catch (Exception e) {

            System.err.println("error " + "Controller get modificarDatosPostulante");
            model.addAttribute("error ", e.getMessage());
            return new RedirectView("/");

        }

    }
    
    @GetMapping("/misPrestamos")
    public ModelAndView misPrestamos(HttpSession session, Model model, Authentication usuario) throws Exception {
        try {
            model.addAttribute("estudiante", usuarioServicio.buscaruserxmail(usuario.getName()));
            ModelAndView mav = new ModelAndView("listarMisPrestamos");
            Usuario user = usuarioServicio.buscaruserxmail(usuario.getName());
            List<Prestamo> prestamos = libroServicio.buscarPrestamosPendientesEstudiente(user.getId());

            mav.addObject("prestamos", prestamos);
            return mav;
        } catch (Exception e) {
            throw new Exception("Error LibroControlador - buscarLibros");
        }

    }
    
    @GetMapping("/prestamosPendientes")
    public ModelAndView prestamosPendientes(HttpSession session, Model model, Authentication usuario) throws Exception {
        try {
            model.addAttribute("estudiante", usuarioServicio.buscaruserxmail(usuario.getName()));
            ModelAndView mav = new ModelAndView("prestamosPendientes");

            List<Prestamo> prestamos = libroServicio.buscarPrestamosPendientes();

            mav.addObject("prestamos", prestamos);
            return mav;
        } catch (Exception e) {
            throw new Exception("Error LibroControlador - buscarLibros");
        }

    }
    
    @GetMapping("/prestamosFinalizados")
    public ModelAndView prestamosFinalizados(HttpSession session, Model model, Authentication usuario) throws Exception {
        try {
            model.addAttribute("estudiante", usuarioServicio.buscaruserxmail(usuario.getName()));
            ModelAndView mav = new ModelAndView("prestamosPendientes");

            List<Prestamo> prestamos = libroServicio.buscarPrestamosFinalizados();

            mav.addObject("prestamos", prestamos);
            return mav;
        } catch (Exception e) {
            throw new Exception("Error LibroControlador - buscarLibros");
        }

    }
    
    @PostMapping("/cancelarPrestamo")
    public String cancelarPrestamo(HttpSession session, Model model, Authentication usuario, @RequestParam Integer idPrest) {
        try {
            model.addAttribute("estudiante", usuarioServicio.buscaruserxmail(usuario.getName()));
            model.addAttribute("idPrest", idPrest);
                        
            return "cancelarPrestamo";
        } catch (Exception e) {

            System.err.println("error " + "Controller get modificarDatosPostulante");
            model.addAttribute("error ", e.getMessage());
            return "/error";

        }

    }
    
    @PostMapping("/cancelandoPrestamo")
    public RedirectView cancelandoPrestamo(HttpSession session, Model model, Authentication usuario, @RequestParam Integer idPrest, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDevolucion) {
        try {
            model.addAttribute("estudiante", usuarioServicio.buscaruserxmail(usuario.getName()));
            libroServicio.cancelarPrestamo(idPrest, fechaDevolucion);
                        
            return new RedirectView("/libro/prestamosFinalizados");
        } catch (Exception e) {

            System.err.println("error " + "Controller get modificarDatosPostulante");
            model.addAttribute("error ", e.getMessage());
            return new RedirectView("/");

        }

    }
}
