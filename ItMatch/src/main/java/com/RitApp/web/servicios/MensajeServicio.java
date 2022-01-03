package com.RitApp.web.servicios;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RitApp.web.entidades.Mensaje;
import com.RitApp.web.entidades.Usuario;
import com.RitApp.web.error.MyException;
import com.RitApp.web.repositorios.MensajeRepositorio;

@Service
public class MensajeServicio {
	@Autowired
	MensajeRepositorio mensajeRepositorio;

	public void guardarmensaje(Mensaje mensaje) throws MyException {
		try {
			mensajeRepositorio.save(mensaje);
		} catch (Exception e) {
			throw new MyException("error al guardar mensaje");
		}
	}

	public Mensaje crearMensaje(Usuario usuario, String mensaje_enviado) throws MyException {
		try {
			Mensaje mensaje = new Mensaje();
			mensaje.setUsuario(usuario);
			mensaje.setFecha_envio(Calendar.getInstance().getTime());
			mensaje.setMensaje_enviado(mensaje_enviado);
			return mensaje;
		} catch (Exception e) {
			throw new MyException("error al guardar mensaje");
		}
	}
}
