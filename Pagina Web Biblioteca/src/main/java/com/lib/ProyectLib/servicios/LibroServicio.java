package com.lib.ProyectLib.servicios;

import com.lib.ProyectLib.entidades.Autor;
import com.lib.ProyectLib.entidades.Estudiante;
import com.lib.ProyectLib.entidades.Libro;
import com.lib.ProyectLib.entidades.Prestamo;
import com.lib.ProyectLib.entidades.Usuario;
import com.lib.ProyectLib.repositorios.EstudianteRepositorio;
import com.lib.ProyectLib.repositorios.LibroRepositorio;
import com.lib.ProyectLib.repositorios.PrestamoRepositorio;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LibroServicio {
    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private PrestamoRepositorio prestamoRepositorio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private EstudianteRepositorio estudianteRepositorio;
    
    @Transactional
    public void crearLibro(String titulo, Integer año, Autor autor, String editorial, Integer ejemplares, MultipartFile caratula) throws Exception {
        try {
            Libro lib = new Libro();
            lib.setTitulo(titulo);
            lib.setAnio(año);
            lib.setAutor(autor);
            lib.setEditorial(editorial);
            lib.setEjemplares(ejemplares);
            lib.setPrestados(0);
            lib.setCaratula(caratula.getBytes());
            lib.setDisponibles(ejemplares);
            libroRepositorio.save(lib);
        } catch (Exception e) {
            throw new Exception("Error al crear un Libro");
        }

    }

    @Transactional
    public List<Libro> imprimirLibros() throws Exception {
        try {
            List<Libro> libros = libroRepositorio.findAll();
            return libros;
        } catch (Exception e) {
            throw new Exception("Error al imprimir todos los libros");
        }

    }

    @Transactional
    public void eliminarLibro(String isbn) throws Exception {
        try {
            libroRepositorio.deleteById(isbn);
        } catch (Exception e) {
            throw new Exception("Error al eliminar Libro por ID");
        }

    }

    @Transactional
    public Libro buscarUnLibro(String isbn) throws Exception {
        try {
            Optional<Libro> bbb = libroRepositorio.findById(isbn);
            if (bbb.isPresent()) {
                Libro lib = bbb.get();
                return lib;
            } else {
                Libro vacio = new Libro();
                return vacio;
            }
        } catch (Exception e) {
            throw new Exception("");
        }

    }

    @Transactional
    public Libro buscarLibroXNombre(String titulo) throws Exception {
        try {
            List<Libro> libros = libroRepositorio.findAll();
            Libro libro = new Libro();
            for (Libro lib : libros) {
                if (lib.getTitulo().equals(titulo)) {
                    libro = lib;
                }
            }

            if (libro != null) {
                return libro;
            } else {
                throw new Exception("No existe un libro con ese titulo");
            }

        } catch (Exception e) {
            throw new Exception("Error al buscar libro por Nombre");
        }

    }

    @Transactional
    public void modificandoLibro(String titulo, Integer año, String autor, String editorial, Integer ejemplares, String isbn, Integer prestados, Integer disponibles, MultipartFile caratula) throws Exception {
        try {
            Libro lib = libroRepositorio.getById(isbn);
            Autor autorObj = autorServicio.buscarUnAutorxId(autor);
            lib.setTitulo(titulo);
            lib.setAnio(año);
            lib.setAutor(autorObj);
            lib.setEditorial(editorial);
            lib.setEjemplares(ejemplares);
            lib.setPrestados(prestados);
            lib.setDisponibles(disponibles);
            if (caratula != null) {
                lib.setCaratula(caratula.getBytes());
            }
            libroRepositorio.saveAndFlush(lib);
        } catch (Exception e) {
            throw new Exception("Error al modificar libro");
        }

    }

    @Transactional
    public void prestarLibro(String isbn) throws Exception {
        try {
            Libro libro = libroRepositorio.getById(isbn);
            libro.setPrestados(libro.getPrestados() + 1);
            libro.setDisponibles(libro.getEjemplares() - libro.getPrestados());
            libroRepositorio.saveAndFlush(libro);
        } catch (Exception e) {
            throw new Exception("Error al prestar un libro");
        }

    }

    @Transactional
    public void libroDevuelto(String isbn) throws Exception {
        try {
            Libro libro = libroRepositorio.getById(isbn);
            libro.setPrestados(libro.getPrestados() - 1);
            libro.setDisponibles(libro.getEjemplares() - libro.getPrestados());
            libroRepositorio.saveAndFlush(libro);
        } catch (Exception e) {
            throw new Exception("Error al devolver libro");
        }

    }
    
    @Transactional
    public void crearPrestamo(String idUsuario, String idLibro, Date fechaPrestamo) throws Exception {
        try {
            Prestamo prestamo = new Prestamo();
            Estudiante est = estudianteRepositorio.getById(idUsuario);
            Libro libro = libroRepositorio.getById(idLibro);
            prestamo.setEstudiante(est);
            prestamo.setLibro(libro);
            prestamo.setFechaPrestamo(fechaPrestamo);
            prestarLibro(libro.getIsbn());
            prestamoRepositorio.save(prestamo);
        } catch (Exception e) {
            throw new Exception("Error al crear un prestamo");
        }

    }
    
    @Transactional
    public List<Prestamo> buscarPrestamosPendientes() throws Exception {
        try {
            List<Prestamo> todos = prestamoRepositorio.findAll();
            List<Prestamo> prestamos = new ArrayList();
            for (Prestamo t : todos) {
                if (t.getFechaDevolucion() == null) {
                    prestamos.add(t);
                }
            }
            return prestamos;
        } catch (Exception e) {
            throw new Exception("Error al buscar prestamos pendientes");
        }

    }
    
     @Transactional
    public List<Prestamo> buscarPrestamosFinalizados() throws Exception {
        try {
            List<Prestamo> todos = prestamoRepositorio.findAll();
            List<Prestamo> prestamos = new ArrayList();
            for (Prestamo t : todos) {
                if (t.getFechaDevolucion() != null) {
                    prestamos.add(t);
                }
            }
            return prestamos;
        } catch (Exception e) {
            throw new Exception("Error al buscar prestamos finalizados");
        }

    }
    
    @Transactional
    public List<Prestamo> buscarPrestamosPendientesEstudiente(String id) throws Exception {
        try {
            List<Prestamo> todos = prestamoRepositorio.findAll();
            List<Prestamo> prestamos = new ArrayList();
            for (Prestamo t : todos) {
                if (t.getEstudiante().getId().equals(id)) {
                    if (t.getFechaDevolucion() == null) {
                        prestamos.add(t);
                    }      
                }
            }
            return prestamos;
        } catch (Exception e) {
            throw new Exception("Error al buscar prestamos pendientes");
        }

    }
    
    @Transactional
    public void cancelarPrestamo(Integer id, Date fechaDevolucion) throws Exception {
        try {
            Prestamo prestamo = prestamoRepositorio.getById(id);
            prestamo.setFechaDevolucion(fechaDevolucion);
            libroDevuelto(prestamo.getLibro().getIsbn());
            prestamoRepositorio.saveAndFlush(prestamo);
        } catch (Exception e) {
            throw new Exception("Error al cancelar prestamo");
        }

    }
       
}
