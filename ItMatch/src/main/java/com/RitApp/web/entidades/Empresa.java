
package com.RitApp.web.entidades;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

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
public class Empresa extends Usuario {
	private String nombre;
	private String actividad;
	private String sitioWeb;
	private String beneficios;
	private String sobreNosotros;
	private String pais;
	@OneToMany
	private List<Trabajo> listaTrabajos;
}
