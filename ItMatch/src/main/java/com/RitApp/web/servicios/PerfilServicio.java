package com.RitApp.web.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.RitApp.web.entidades.Perfil;
import com.RitApp.web.error.MyException;
import com.RitApp.web.repositorios.PerfilRepositorio;

@Service
public class PerfilServicio {

	@Autowired
	private PerfilRepositorio perfilRepositorio;

	@Transactional
	public Perfil crearPerfil(String lenguaje, String seniority, String idioma, String estudios, String algoSobreMi)
			throws MyException {
		try {
			Perfil perfil = new Perfil();
			perfil.setLenguaje(lenguaje);
			perfil.setIdioma(idioma);
			perfil.setSeniority(seniority);
			perfil.setEstudios(estudios);
			perfil.setAlgoSobreMi(algoSobreMi);
			perfilRepositorio.save(perfil);

			return perfil;
		} catch (Exception e) {
			throw new MyException("Error al crear Perfil");
		}

	}

	@Transactional
	public Perfil crearPerfilVacio() throws MyException {
		try {
			Perfil perfil = new Perfil();

			perfilRepositorio.save(perfil);

			return perfil;
		} catch (Exception e) {
			throw new MyException("Error al crear Perfil vacio");

		}

	}

	@Transactional(readOnly = true)
	public Perfil buscarPerfilxId(String id) throws MyException {
		try {
			Perfil perfil = perfilRepositorio.getById(id);

			return perfil;
		} catch (Exception e) {
			throw new MyException("Error al buscar perfil x id");
		}

	}

	@Transactional
	public Perfil modificarPerfil(String id, String lenguaje, String seniority, String idioma, String estudios,
			String algoSobreMi) throws MyException {
		try {
			Perfil perfil = perfilRepositorio.getById(id);
			perfil.setLenguaje(lenguaje);
			perfil.setIdioma(idioma);
			perfil.setSeniority(seniority);
			perfil.setEstudios(estudios);
			perfil.setAlgoSobreMi(algoSobreMi);
			perfilRepositorio.saveAndFlush(perfil);

			return perfil;
		} catch (Exception e) {
			throw new MyException("error al modificar perfil");
		}

	}
}
