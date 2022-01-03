package com.RitApp.web.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Emiliano
 *
 */
@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Trabajo {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	private String modalidad;
	private String puesto;
	private String zona;
	private String nombre_empresa;
	private String lenguajes;
	@OneToOne
	private Perfil perfil;
	@ManyToOne
	private Empresa empresa;
}
