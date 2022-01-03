package com.RitApp.web.servicios;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.RitApp.web.entidades.Emparejado;
import com.RitApp.web.entidades.Empresa;
import com.RitApp.web.entidades.Postulante;
import com.RitApp.web.entidades.Trabajo;
import com.RitApp.web.entidades.Usuario;
import com.RitApp.web.enums.Rol;
import com.RitApp.web.error.MyException;
import com.RitApp.web.repositorios.UsuarioRepositorio;

@Service
public class UsuarioServicio implements UserDetailsService {
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	@Autowired
	private EmpresaServicio empresaServicio;
	@Autowired
	private PostulanteServicio postulanteServicio;

	@Override

	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
			if (usuario != null) {
				List<GrantedAuthority> authorities = new ArrayList<>();
				GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
				authorities.add(p);
				ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
						.currentRequestAttributes();
				HttpSession session = attr.getRequest().getSession(true);

				return new User(usuario.getEmail(), usuario.getClave(), authorities);
			}
		} catch (UsernameNotFoundException e) {
			throw new UsernameNotFoundException(e.getMessage());

		}
		return null;
	}

	public void guardarUser(String email, String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		usuario.setClave(encoder.encode(password));
		usuario.setRol(Rol.POSTULANTE);
		usuarioRepositorio.save(usuario);

	}

	public Usuario buscaruserxmail(String email) throws MyException {
		try {
			return usuarioRepositorio.buscarPorEmail(email);
		} catch (Exception e) {
			throw new MyException("error al buscaruserxmail");
		}
	}

	public String obtenernombre(Authentication usuario) throws MyException {
		try {
			Usuario usuario1 = new Usuario();
			usuario1 = usuarioRepositorio.buscarPorEmail(usuario.getName());
			return usuario1.getNombre_usuario();
		} catch (Exception e) {
			throw new MyException("error interno, al obtener nombre de usuario en UsuarioService");
		}

	}

	public Usuario buscaruserxid(String id) throws MyException {
		try {
			return usuarioRepositorio.getById(id);
		} catch (Exception e) {
			throw new MyException("error interno, error al busacaruserxid");
		}
	}
}
