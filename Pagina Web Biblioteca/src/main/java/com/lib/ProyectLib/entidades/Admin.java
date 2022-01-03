
package com.lib.ProyectLib.entidades;

import com.lib.ProyectLib.enums.Turno;
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
public class Admin extends Usuario {
    private String nombre;
    private String apellido;
    private Turno turno;
}
