package com.lib.ProyectLib.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Libro {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String isbn;
    private String titulo;
    @OneToOne
    private Autor autor;
    private String editorial;
    private Integer anio;
    private Integer ejemplares;
    private Integer prestados;
    private Integer disponibles;
    private byte[] caratula;
}
