
package com.RitApp.web.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.RitApp.web.entidades.Empresa;
import com.RitApp.web.entidades.Usuario;
import com.RitApp.web.enums.Rol;
import com.RitApp.web.error.MyException;
import com.RitApp.web.repositorios.EmpresaRepositorio;
import com.RitApp.web.repositorios.UsuarioRepositorio;

@Service
public class EmpresaServicio {

	@Autowired
	private EmpresaRepositorio empresaRepositorio;
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	@Autowired
	private UsuarioServicio usuarioServicio;

	@Transactional
	public void crearEmpresa(String email, String contraseña1, String contraseña2, String nombre, String actividad)
			throws MyException {
		try {
			validarContraseña(email, contraseña1, contraseña2);
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			Empresa empresa = new Empresa();
			empresa.setEmail(email);
			empresa.setClave(encoder.encode(contraseña1));
			empresa.setNombre(nombre);
			empresa.setNombre_usuario(nombre);
			empresa.setActividad(actividad);
			empresa.setRol(Rol.EMPRESA);

			empresaRepositorio.save(empresa);

		} catch (MyException e) {
			throw new MyException(e.getMessage());
		}
	}

	@Transactional
	public void modificarEmpresa(String id, String email, String nombre, String actividad, String sitioWeb,
			String beneficios, String sobreNostros, String pais, MultipartFile logo) throws MyException {
		try {
			Empresa empresa = empresaRepositorio.getById(id);
			empresa.setEmail(email);
			empresa.setNombre(nombre);
			empresa.setActividad(actividad);
			empresa.setSitioWeb(sitioWeb);
			empresa.setBeneficios(beneficios);
			empresa.setSobreNosotros(sobreNostros);
			empresa.setPais(pais);

			if (logo != null) {
				empresa.setFoto(logo.getBytes());
			}

			empresaRepositorio.save(empresa);

		} catch (Exception e) {
			throw new MyException("Error al Modificar Empresa en EmpresaServicio");
		}
	}

	@Transactional
	public void eliminarEmpresa(String id) throws MyException {
		try {
			empresaRepositorio.deleteById(id);
		} catch (Exception e) {
			throw new MyException("Error al Eliminar Empresa en EmpresaServicio");
		}
	}

	@Transactional(readOnly = true)
	public List<Empresa> imprimirEmpresas() throws MyException {
		try {
			List<Empresa> lista = empresaRepositorio.findAll();
			return lista;
		} catch (Exception e) {
			throw new MyException("Error al imprimir todas las empresas");
		}

	}

	@Transactional
	public void validarContraseña(String email, String clave1, String clave2) throws MyException {

		if (clave1.isEmpty()) {
			throw new MyException("La contraseña esta vacia");
		}
		if (buscaUsuario(email) != null) {
			throw new MyException("El correo ingresado ya se encuentra registrado");

		}
		if (clave2.isEmpty()) {
			throw new MyException("La contraseña esta vacia");
		}
		if (clave1.equals(clave2)) {

		} else {
			throw new MyException("La contraseñas son diferentes");
		}

	}

	@Transactional(readOnly = true)
	public List<Empresa> buscarEmpresasXNombre(String nombre) throws MyException {
		try {
			List<Empresa> listado = empresaRepositorio.findAll();
			List<Empresa> empresas = new ArrayList<Empresa>();
			for (Empresa e : listado) {
				if (nombre.equals(e.getNombre())) {
					empresas.add(e);
				}
			}

			if (empresas != null) {
				return empresas;
			} else {
				// Aca se entrega una lista vacia, en caso de que no se encuentre ninguna
				// empresa
				return empresas;
			}

		} catch (Exception e) {
			throw new MyException("Error al buscar empresas por Nombre");
		}

	}

	public Empresa buscarxmail(String email) throws MyException {
		try {
			Usuario usuario = new Usuario();
			usuario = usuarioRepositorio.buscarPorEmail(email);
			return empresaRepositorio.getById(usuario.getId());
		} catch (Exception e) {
			throw new MyException("error al buscar empresa por mail");
		}
	}

	public Empresa buscarxid(String id) throws MyException {
		try {
			Empresa empresa = new Empresa();
			empresa = empresaRepositorio.getById(id);
			return empresa;
		} catch (Exception e) {
			throw new MyException("error al buscar empresa x id");
		}
	}

	public Usuario buscaUsuario(String email) throws MyException {
		try {

			return usuarioServicio.buscaruserxmail(email);
		} catch (Exception e) {
			throw new MyException("error al buscar empresa x email");
		}
	}

}
