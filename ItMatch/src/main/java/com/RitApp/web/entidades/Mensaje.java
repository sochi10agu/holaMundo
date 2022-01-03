package com.RitApp.web.entidades;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Mensaje implements Comparable<Mensaje> {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	@OneToOne
	private Usuario usuario;
	private String nombre;
	private String tipo;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha_envio;
	private String mensaje_enviado;

	@Override
	public int compareTo(Mensaje o) {
		return getFecha_envio().compareTo(o.getFecha_envio());
	}

}