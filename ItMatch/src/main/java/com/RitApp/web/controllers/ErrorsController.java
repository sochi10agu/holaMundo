package com.RitApp.web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorsController implements ErrorController {

	@RequestMapping(value = "/error", method = { RequestMethod.GET, RequestMethod.POST })
	public String error(Model model, HttpServletRequest httpServletRequest,
			@RequestParam(required = false) String error) {
		System.out.println(error);
		String mensaje = "";
		int codigo = (int) httpServletRequest.getAttribute("javax.servlet.error.status_code");

		switch (codigo) {
		case 400:
			if (error != null) {
				mensaje = error;
			} else {
				mensaje = "El recurso solicitado no existe";
			}
			break;
		case 401:
			if (error != null) {
				mensaje = error;
			} else {
				mensaje = "Acceso no autorizado";
			}
			break;
		case 403:
			if (error != null) {
				mensaje = error;
			} else {
				mensaje = "No tiene los permisos suficientes";
			}
			break;
		case 404:
			if (error != null) {
				mensaje = error;
			} else {
				mensaje = "No se encuentra el recurso solicitado";
			}
			break;
		case 500:
			if (error != null) {
				mensaje = error;
			} else {
				mensaje = "El servidor no pudo realizar la peticion solicitada";
			}

			break;
		default:
		}

		model.addAttribute("codigo", codigo);
		model.addAttribute("error", mensaje);

		return "error.html";
	}
}
