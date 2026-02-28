package com.david.edulib.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generatedvalue para que sea hibernate el que gestiona los IDs de forma autonoma 
    private Integer id;

    @Column(nullable = false, unique = true)
    String nombre;

    private String nacionalidad;

// EAGER para que si recuperamos un autor se recuperen tambien sus libros en la misma consulta
//  mappedBy para indicar que el dueño de la relacion es el atributo "autor de la clase Libro
    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER)
    @Builder.Default // Para que lombok inicialice la lista de libros y luego el repositorio pueda rellenarlo con los libros (EAGER)
    private List<Libro> libros = new ArrayList<>();
}
