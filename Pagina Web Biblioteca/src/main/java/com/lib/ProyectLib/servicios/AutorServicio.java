package com.lib.ProyectLib.servicios;

import com.lib.ProyectLib.entidades.Autor;
import com.lib.ProyectLib.repositorios.AutorRepositorio;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional
    public void crearAutor(String nombre, MultipartFile foto) throws Exception {
        try {
            Autor aut = new Autor();
            aut.setNombre(nombre);
            aut.setFoto(foto.getBytes());

            autorRepositorio.save(aut);
        } catch (Exception e) {
            throw new Exception("Error al Crear Autor en AutorServicio");
        }

    }

//    @Transactional(readOnly = true)
//    public List<Autor> buscarAutores() throws Exception {
//        try {
//            List<Autor> autores = autorRepositorio.findAll();
//            return autores;
//        } catch (Exception e) {
//            throw new Exception("Error al buscar lista de Autores en AutorServicio");
//
//        }
//
//    }
    @Transactional
    public void eliminarAutor(String idAutor) throws Exception {
        try {
            autorRepositorio.deleteById(idAutor);
        } catch (Exception e) {
            throw new Exception("Error al eliminar Autor en AutorServicio");
        }

    }

    @Transactional
    public Autor buscarUnAutorxId(String idAutor) throws Exception {
        try {
            Autor ccc = autorRepositorio.getById(idAutor);
            return ccc;
        } catch (Exception e) {
            throw new Exception("Error al Buscar Un Autor por ID en AutorServicio");
        }

    }

    @Transactional
    public List<Autor> listarTodosAutores() throws Exception {
        try {
            List<Autor> lista = autorRepositorio.findAll();
            return lista;
        } catch (Exception e) {
            throw new Exception("Error al listar todos los autores en AutorServicio");
        }
    }

    @Transactional
    public Autor buscarAutorXNombre(String nombre) throws Exception {
        try {

            List<Autor> autores = autorRepositorio.findAll();
            Autor autor = new Autor();
            for (Autor a : autores) {
                if (a.getNombre().equals(nombre)) {
                    autor = a;
                }
            }

            if (autor != null) {
                return autor;
            } else {
                throw new Exception("No hay ningun autor con ese nombre");

            }

        } catch (Exception e) {
            throw new Exception("Error al Buscar Un Autor por Nombre en AutorServicio");
        }

    }

    @Transactional
    public void modificarAutor(String nombre, String idAutor, MultipartFile foto) throws Exception {
        try {
            Autor autor = autorRepositorio.getById(idAutor);
            autor.setNombre(nombre);
            if (foto != null) {
                autor.setFoto(foto.getBytes());
            }
            autorRepositorio.saveAndFlush(autor);
        } catch (Exception e) {
            throw new Exception("Error al modificar Autor en AutorServicio");
        }

    }

    @Transactional
    public void eliminarAutorPorNombre(String nombre) throws Exception {
        try {
            List<Autor> autores = autorRepositorio.findAll();
            Autor autor = new Autor();
            for (Autor a : autores) {
                if (a.getNombre().equals(nombre)) {
                    autor = a;
                }
            }

            if (autor != null) {
                autorRepositorio.deleteById(autor.getId());
            } else {
                throw new Exception("No hay ningun autor con ese nombre");
            }
        } catch (Exception e) {
            throw new Exception("Error al eliminar Autor por Nombre en AutorServicio");
        }

    }
}
