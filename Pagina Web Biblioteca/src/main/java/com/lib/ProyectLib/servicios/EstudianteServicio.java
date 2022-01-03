package com.lib.ProyectLib.servicios;

import com.lib.ProyectLib.entidades.Estudiante;
import com.lib.ProyectLib.entidades.Usuario;
import com.lib.ProyectLib.enums.Rol;
import com.lib.ProyectLib.repositorios.EstudianteRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EstudianteServicio {

    @Autowired
    private EstudianteRepositorio estudianteRepositorio;
    
    @Autowired
    private UsuarioServicio usuarioServicio;

    @Transactional
    public void crearEstudiante(String nombre, String apellido, String contraseña, String contraseña1,
            String email) throws Exception {
        try {
            validar(nombre, apellido, email, contraseña, contraseña1);
            Estudiante estudiante;
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            estudiante = new Estudiante();
            estudiante.setEmail(email);
            estudiante.setNombre(nombre);
            estudiante.setApellido(apellido);
            estudiante.setClave(encoder.encode(contraseña));
            estudiante.setRol(Rol.ESTUDIANTE);

            estudianteRepositorio.save(estudiante);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }

    }
    
    @Transactional
	public void modificar(String id, String nombre, String apellido, String email, Long telefono, String dni, String domicilio, MultipartFile foto)
			throws Exception {
		try {

			Estudiante estudiante = estudianteRepositorio.getById(id);

			estudiante.setNombre(nombre);
			estudiante.setApellido(apellido);
			estudiante.setEmail(email);
			estudiante.setTelefono(telefono);
			estudiante.setDni(dni);
			estudiante.setDomicilio(domicilio);
						
			if (foto != null) {
                            estudiante.setFoto(foto.getBytes());
			}

			estudianteRepositorio.saveAndFlush(estudiante);
		} catch (Exception e) {
			throw new Exception("error modificar perfil servicio");
		}
	}

    public void validar(String nombre, String apellido, String email, String contraseña, String contraseña1) throws Exception {

        if (email.isEmpty() || email == null) {
            throw new Exception("el email no pude ser nulo");
        }
        if (buscaUsuario(email) != null) {
            throw new Exception("El correo ingresado ya se encuentra registrado");
        }

        if (contraseña.isEmpty() || contraseña == null) {
            throw new Exception("la contraseña no pude ser nula");
        }
        if (!contraseña.equals(contraseña1)) {
            throw new Exception("las contraseñas deben coincidir");
        }
        if (nombre.isEmpty() || nombre == null) {
            throw new Exception("el nombre no pude ser nulo");
        }
        if (apellido.isEmpty() || apellido == null) {
            throw new Exception("el apellido no pude ser nulo");
        }
        
    }
    
    public Estudiante buscarXId(String id) throws Exception {
		Optional<Estudiante> respuesta = estudianteRepositorio.findById(id);
		if (respuesta.isPresent()) {
			return respuesta.get();
		} else {
			throw new Exception("no se encuentra ningun Postulante con el id");
		}
	}

	public List<Estudiante> listar() throws Exception {
		try {
			return estudianteRepositorio.findAll();
		} catch (Exception e) {
			throw new Exception("error interno, al listar postulantes");
		}

	}

	public Estudiante buscaxmail(String email) throws Exception {
		try {
			Usuario usuario = new Usuario();
			usuario = usuarioServicio.buscaruserxmail(email);
			return estudianteRepositorio.getById(usuario.getId());
		} catch (Exception e) {
			throw new Exception("error al buscar por mail");
		}
	}

	public Usuario buscaUsuario(String email) throws Exception {
		try {
			return usuarioServicio.buscaruserxmail(email);
		} catch (Exception e) {
			throw new Exception("error al buscar usuario");
		}
	}
}
