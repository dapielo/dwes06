package com.david.edulib.entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generatedvalue para que sea hibernate el que gestiona los IDs de forma autonoma 
    private Integer id;

    @Column(nullable = false, unique = true)
    String nombre;

    private String nacionalidad;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "autor")
    private List<Libro> libros;
}
