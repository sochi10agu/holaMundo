
package com.lib.ProyectLib.entidades;

import javax.persistence.Entity;
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
public class Estudiante extends Usuario {
    private String dni;
    private String nombre;
    private String apellido;
    private String domicilio;
    private Long telefono;
}
