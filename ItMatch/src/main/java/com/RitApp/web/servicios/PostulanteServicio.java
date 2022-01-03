package com.RitApp.web.servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.RitApp.web.entidades.Perfil;
import com.RitApp.web.entidades.Postulante;
import com.RitApp.web.entidades.Usuario;
import com.RitApp.web.enums.Rol;
import com.RitApp.web.error.MyException;
import com.RitApp.web.repositorios.PostulanteRepositorio;
import com.RitApp.web.repositorios.UsuarioRepositorio;

/**
 * @author Emiliano
 *
 */
/**
 * @author Emiliano
 *
 */
@Service
public class PostulanteServicio {

	@Autowired
	private PostulanteRepositorio postulanteRepositorio;
	@Autowired
	private UsuarioServicio usuarioServicio;
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	@Autowired
	private PerfilServicio perfilServicio;

	@Transactional
	public void crearPostulante(String nombre, String apellido, String email, String contraseña, String contraseña1,
			String telefono) throws MyException {
		try {
			validar(nombre, apellido, email, contraseña, contraseña1, telefono);
			Postulante postulante;
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			postulante = new Postulante();
			postulante.setEmail(email);
			postulante.setNombre(nombre);
			postulante.setNombre_usuario(nombre);
			postulante.setApellido(apellido);
			postulante.setTelefono(telefono);
			postulante.setClave(encoder.encode(contraseña));
			postulante.setRol(Rol.POSTULANTE);
			Perfil perfil = perfilServicio.crearPerfilVacio();
			postulante.setPerfil(perfil);

			postulanteRepositorio.save(postulante);
		} catch (MyException e) {
			System.out.println(e.getMessage());
			throw new MyException(e.getMessage());
		}
	}

	@Transactional
	public void modificar(String id, String nombre, String apellido, String email, String telefono,
			Date fechaNacimiento, Integer edad, String dni, String genero, String pais, String direccion,
			String lenguaje, String seniority, String idioma, String estudios, String algoSobreMi, MultipartFile foto)
			throws MyException {
		try {

			Postulante postulante = postulanteRepositorio.getById(id);

			postulante.setNombre(nombre);
			postulante.setApellido(apellido);
			postulante.setEmail(email);
			postulante.setTelefono(telefono);
			postulante.setFechaNacimiento(fechaNacimiento);
			postulante.setEdad(edad);
			postulante.setDni(dni);
			postulante.setGenero(genero);
			postulante.setDireccion(direccion);
			postulante.setPais(pais);

			Perfil perfil = new Perfil();

			if (postulante.getPerfil() != null) {
				perfil = perfilServicio.modificarPerfil(postulante.getPerfil().getId(), lenguaje, seniority, idioma,
						estudios, algoSobreMi);
				postulante.setPerfil(perfil);
			} else {
				perfil = perfilServicio.crearPerfil(lenguaje, seniority, idioma, estudios, algoSobreMi);
				postulante.setPerfil(perfil);
			}

			if (foto != null) {
				postulante.setFoto(foto.getBytes());
			}

			postulanteRepositorio.saveAndFlush(postulante);
		} catch (Exception e) {
			throw new MyException("error modificar perfil servicio");
		}
	}

	@Transactional
	public void eliminar(String id) throws MyException {
		try {
			Postulante postulante = buscarXId(id);
			postulanteRepositorio.delete(postulante);
		} catch (Exception e) {
			throw new MyException("error al eliminar");
		}

	}

	public void validar(String nombre, String apellido, String email, String contraseña, String contraseña1,
			String telefono) throws MyException {

		if (email.isEmpty() || email == null) {
			throw new MyException("el email no pude ser nulo");
		}
		if (buscaUsuario(email) != null) {
			throw new MyException("El correo ingresado ya se encuentra registrado");

		}

		if (contraseña.isEmpty() || contraseña == null) {
			throw new MyException("la contraseña no pude ser nula");

		}
		if (!contraseña.equals(contraseña1)) {
			throw new MyException("las contraseñas deben coincidir");

		}
		if (nombre.isEmpty() || nombre == null) {
			throw new MyException("el nombre no pude ser nulo");
		}
		if (apellido.isEmpty() || apellido == null) {
			throw new MyException("el apellido no pude ser nulo");

		}
		if (telefono == null) {
			throw new MyException("el telefono no pude ser nulo");

		}
	}

	public Postulante buscarXId(String id) throws MyException {
		Optional<Postulante> respuesta = postulanteRepositorio.findById(id);
		if (respuesta.isPresent()) {
			return respuesta.get();
		} else {
			throw new MyException("no se encuentra ningun Postulante con el id");
		}
	}

	public List<Postulante> listar() throws MyException {
		try {
			return postulanteRepositorio.findAll();
		} catch (Exception e) {
			throw new MyException("error interno, al listar postulantes");
		}

	}

	public Postulante buscaxmail(String email) throws MyException {
		try {
			Usuario usuario = new Usuario();
			usuario = usuarioServicio.buscaruserxmail(email);
			return postulanteRepositorio.getById(usuario.getId());
		} catch (Exception e) {
			throw new MyException("error al buscar por mail");
		}
	}

	public Usuario buscaUsuario(String email) throws MyException {
		try {
			return usuarioServicio.buscaruserxmail(email);
		} catch (Exception e) {
			throw new MyException("error al buscar usuario");
		}
	}
}
