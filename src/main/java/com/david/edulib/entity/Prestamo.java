package com.david.edulib.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Setter @Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "prestamos")
public class Prestamo {

    private LocalDate fechaPrestamo;
    private LocalDate fechaEntrega;

    // Aunque el objeto Java tiene almacenado un objeto libro como atributo, en la base de datos, como este atributo está anotado con @Id
    // solo se va a almacenar en una columna llamada libro_id el ISBN del libro 
    @Id
    @OneToOne
    // JoinColumn porque es esta entidad la dueña de la relacion, en conreto el atributo libro, del cual seleccionará su id para almacenar en 
    // la columna "libro_id"
    @JoinColumn(name = "libro_id")
    private Libro libro; 
}
