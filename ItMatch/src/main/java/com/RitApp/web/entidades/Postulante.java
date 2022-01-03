package com.RitApp.web.entidades;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Postulante extends Usuario {

	private String dni;
	private String nombre;
	private String apellido;
	@Temporal(TemporalType.DATE)
	private Date fechaNacimiento;
	private int edad;
	private String telefono;
	private String genero;
	private String direccion;
	private String pais;
	@OneToOne
	private Perfil perfil;

}
