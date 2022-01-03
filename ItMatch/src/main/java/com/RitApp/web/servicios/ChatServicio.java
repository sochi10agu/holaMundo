package com.RitApp.web.servicios;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.RitApp.web.entidades.Chat;
import com.RitApp.web.entidades.Emparejado;
import com.RitApp.web.entidades.Mensaje;
import com.RitApp.web.error.MyException;
import com.RitApp.web.repositorios.ChatRepositorio;
import com.RitApp.web.repositorios.EmparejadoRepositorio;

@Service
public class ChatServicio {
	@Autowired
	ChatRepositorio chatRepositorio;
	@Autowired
	EmparejadoRepositorio emparejadoRepositorio;
	@Autowired
	MensajeServicio mensajeServicio;

	public void guardar_chat(Chat chat) throws MyException {
		try {
			chatRepositorio.save(chat);
		} catch (Exception e) {
			throw new MyException("error al guardar chat");
		}
	}

	public Chat buscarchatxEmparejado(String id_emparejado) throws MyException {
		try {
			Emparejado emparejado = emparejadoRepositorio.buscarPorid(id_emparejado);
			List<Chat> listchat = chatRepositorio.findByEmparejado(emparejado);
			Chat chat = new Chat();
			for (Chat chat1 : listchat) {
				chat = chat1;
			}
			return chat;
		} catch (Exception e) {
			throw new MyException("error al buscar chat");
		}
	}

	public void enviar_mensaje(Chat chat, Authentication usuario, String texto_mensaje) throws MyException {
		try {
			List<Mensaje> mensajes_nuevo = new ArrayList<Mensaje>();
			Mensaje mensaje = new Mensaje();
			GrantedAuthority p = new SimpleGrantedAuthority("ROLE_EMPRESA");
			if (usuario.getAuthorities().contains(p)) {
				mensaje.setUsuario(chat.getEmparejado().getEmpresa());
				mensaje.setNombre(chat.getEmparejado().getNombre_empresa());
				mensaje.setTipo("EMPRESA");
			} else {
				mensaje.setUsuario(chat.getEmparejado().getPostulante());
				mensaje.setNombre(chat.getEmparejado().getNombre_postulante());
				mensaje.setTipo("POSTULANTE");
			}
			mensaje.setFecha_envio(Calendar.getInstance().getTime());
			mensaje.setMensaje_enviado(texto_mensaje);
			mensajes_nuevo = chat.getMensajes();
			mensajes_nuevo.add(mensaje);
			chat.setMensajes(mensajes_nuevo);
			chatRepositorio.save(chat);
		} catch (Exception e) {
			throw new MyException("error al enviar mensaje");
		}
	}

	public List<Mensaje> listadeMensajes(String id_chat) throws MyException {
		try {
			Chat chat = chatRepositorio.buscarPorid(id_chat);
			Collections.sort(chat.getMensajes());
			return chat.getMensajes();
		} catch (Exception e) {
			throw new MyException("error al listar mensajes");
		}
	}

	public Chat primer_mensaje(Chat chat) throws MyException {
		try {
			Mensaje mensaje = new Mensaje();
			mensaje.setUsuario(chat.getEmparejado().getEmpresa());
			mensaje.setNombre(chat.getEmparejado().getNombre_empresa());
			mensaje.setTipo("EMPRESA");
			mensaje.setFecha_envio(Calendar.getInstance(TimeZone.getTimeZone("GMT-3:00")).getTime());
			mensaje.setMensaje_enviado("Tienes un nuevo match!🙌🙌<br/>\r\n"
					+ "IMPORTANTE❗❗ Recordar que el proceso de seleccion es anonimo😊😊.<br/>\r\n"
					+ "No realizaremos preguntas sobre:<br/>\r\n" + "  🚫Estado Civil.<br/>\r\n" + "  🚫Edad.<br/>\r\n"
					+ "  🚫Sexo.<br/>\r\n" + "  🚫Ni ningun tipo de preguntas personales.<br/>" + "");
			List<Mensaje> mensajes_nuevo = new ArrayList<Mensaje>();
			mensajes_nuevo.add(mensaje);
			chat.setMensajes(mensajes_nuevo);
			return chat;
		} catch (Exception e) {
			throw new MyException("error en primer mensaje");
		}
	}

	public Chat buscarchatxid(String id)throws MyException{
		try {
		return chatRepositorio.buscarPorid(id);
		} catch (Exception e) {
			throw new MyException("error en primer mensaje");
		}
	}

	public String generar_Mensaje(String usuario, String mensaje) {
		return "Mensaje de " + usuario + "/n" + mensaje;
	}

	public String Tipologeado(Authentication usuario) {
		GrantedAuthority p = new SimpleGrantedAuthority("ROLE_EMPRESA");
		if (usuario.getAuthorities().contains(p)) {
			return "EMPRESA";
		} else {
			return "POSTULANTE";
		}
	}

	public Model perfil_Destinario(Model modelo, Authentication usuario, Chat chat) {
		String tipologeado = Tipologeado(usuario);
		if (tipologeado.equals("EMPRESA")) {
			modelo.addAttribute("destinatario", chat.getEmparejado().getEmpresa());
		} else {
			modelo.addAttribute("destinatario", chat.getEmparejado().getEstado_postulante());
		}
		return modelo;
	}
}
