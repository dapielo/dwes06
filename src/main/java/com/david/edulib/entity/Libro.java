package com.david.edulib.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "libros")
public class Libro {
    @Id // No usamos GeneratedValue porque nosotros vamos a indicar el ISBN, pero como es único para cada libro, nos sirve de clave primaria
    @Size(min = 10, max = 13)
    private String isbn;

    private String titulo;

// Con esta anotación bastaría para que se generase la columna de clave foránea, el persist es para que al añadir un libro se añade su autor si no existe
    @ManyToOne(cascade = {CascadeType.PERSIST}) 
    @JoinColumn(name = "autor_id") // Usamos esto para ponerle el nombre que queramos
    private Autor autor;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "libro")
    Prestamo prestamo;
}
