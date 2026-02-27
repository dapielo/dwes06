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

// CascadeType.PERSIST es para que al añadir un libro se añade su autor si no existe, pero si se borra un libro no se borrre su autor (CascadeType.ALL)
    @ManyToOne(cascade = {CascadeType.PERSIST}) 
// Join column porque el dueño de esta relacion es libro al ser el lado de los many
    @JoinColumn(name = "autor_id")
    private Autor autor;

// mappedBy siempre en el lado NO propietario de la relacion, para indicar a spring que la relacion esta en el atributo que le pasamos de la otra entidad
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "libro")
    Prestamo prestamo;
}
