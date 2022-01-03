package com.RitApp.web.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RitApp.web.entidades.Chat;
import com.RitApp.web.entidades.Emparejado;
import com.RitApp.web.entidades.Empresa;
import com.RitApp.web.entidades.Postulante;
import com.RitApp.web.entidades.Trabajo;
import com.RitApp.web.entidades.Usuario;
import com.RitApp.web.enums.Rol;
import com.RitApp.web.error.MyException;
import com.RitApp.web.repositorios.EmparejadoRepositorio;

@Service
public class EmparejadoService {
	@Autowired
	EmparejadoRepositorio emparejadoRepositorio;
	@Autowired
	UsuarioServicio usuarioServicio;
	@Autowired
	TrabajoServicio trabajoServicio;
	@Autowired
	EmpresaServicio empresaServicio;
	@Autowired
	PostulanteServicio postulanteServicio;
	@Autowired
	ChatServicio chatServicio;

	public void emparejarPostulante(String email, String id) throws MyException {
		try {
			Postulante postulante = new Postulante();
			Trabajo trabajo = new Trabajo();
			Empresa empresa = new Empresa();
			Emparejado emparejado = new Emparejado();
			postulante = postulanteServicio.buscaxmail(email);
			trabajo = trabajoServicio.buscarXId(id);
			if (comprobar(postulante, trabajo)) {
				System.out.println("ya existe");
			} else {
				System.out.println(trabajo.getEmpresa().getId());
				empresa = trabajo.getEmpresa();
				emparejado.setEmpresa(empresa);
				emparejado.setNombre_empresa(empresa.getNombre());
				emparejado.setPostulante(postulante);
				emparejado.setNombre_postulante(postulante.getNombre());
				emparejado.setTrabajo(trabajo);
				emparejado.setNombre_puesto(trabajo.getPuesto());
				emparejado.setEstado_postulante(true);
				emparejado.setEstado_empresa(false);
				emparejado.setEstado_activo(false);
				emparejadoRepositorio.save(emparejado);
			}
		} catch (Exception e) {
			throw new MyException("error al emparejar postulante");
		}
	}

	public void emparejarEmpresa(String emparejado_id) throws MyException {
		try {
			Emparejado emparejado = new Emparejado();
			emparejado = emparejadoRepositorio.getById(emparejado_id);
			emparejado.setEstado_empresa(true);
			emparejado.setEstado_activo(true);
			Chat chat = new Chat();
			chat.setEmparejado(emparejado);
			chat = chatServicio.primer_mensaje(chat);
			chatServicio.guardar_chat(chat);
			emparejadoRepositorio.save(emparejado);
		} catch (Exception e) {
			throw new MyException("error al emparejar empresa");
		}
	}

	public List<Emparejado> mostrarlikesnuevos(String email) throws MyException {
		try {
			List<Emparejado> listaEmparejados = new ArrayList<Emparejado>();
			List<Emparejado> listaEmparejadosanteriores = new ArrayList<Emparejado>();
			listaEmparejados = mostraremparejadosxempresa(email);
			for (Emparejado emparejado : listaEmparejados) {
				if (emparejado.getEstado_empresa()) {
					listaEmparejadosanteriores.add(emparejado);
					System.out.println(emparejado.getEstado_empresa());
				}
			}
			listaEmparejados.removeAll(listaEmparejadosanteriores);
			return listaEmparejados;
		} catch (Exception e) {
			throw new MyException("error al mostrar emparejados por empresa");
		}
	}

	public List<Emparejado> mostrarlikes(String email) throws MyException {
		try {
			Usuario usuario = new Usuario();
			usuario = usuarioServicio.buscaruserxmail(email);
			if (usuario.getRol().equals(Rol.POSTULANTE)) {
				return mostraremparejadosxpostulante(email);
			} else {
				return mostrarlikesnuevos(email);
			}
		} catch (Exception e) {
			throw new MyException("error al mostrar likes");
		}
	}

	public List<Emparejado> mostraremparejadosxempresa(String email) throws MyException {
		try {
			Empresa empresa = new Empresa();
			empresa = empresaServicio.buscarxmail(email);
			return emparejadoRepositorio.findByEmpresa(empresa);
		} catch (Exception e) {
			throw new MyException("error al mostrar emparejados por empresa");

		}
	}

	public List<Emparejado> mostraremparejadosxpostulante(String email) throws MyException {
		try {
			Postulante postulante = new Postulante();
			postulante = postulanteServicio.buscaxmail(email);
			return emparejadoRepositorio.findByPostulante(postulante);
		} catch (Exception e) {
			throw new MyException("error al mostrar emparejados por postulante");
		}
	}

	public Boolean comprobar(Postulante postulante, Trabajo trabajo) throws MyException {
		try {
			if (!emparejadoRepositorio.findByPostulanteAndTrabajo(postulante, trabajo).isEmpty()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new MyException("error al comprobar emparejado");
		}
	}

	public List<Emparejado> mostrarlikeactivos(String email) throws MyException {
		try {
			List<Emparejado> listaEmparejados = new ArrayList<Emparejado>();
			List<Emparejado> listaEmparejadosactivos = new ArrayList<Emparejado>();
			listaEmparejados = mostraremparejadosxempresa(email);
			for (Emparejado emparejado : listaEmparejados) {
				if (emparejado.getEstado_activo()) {
					listaEmparejadosactivos.add(emparejado);
					System.out.println(emparejado.getEstado_empresa());
				}
			}
			return listaEmparejadosactivos;
		} catch (Exception e) {
			throw new MyException("error al mostrar likes activos");
		}
	}

}