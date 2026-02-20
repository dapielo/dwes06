package com.david.edulib.dataloader;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.david.edulib.entity.Autor;
import com.david.edulib.entity.Libro;
import com.david.edulib.service.AutorService;
import com.david.edulib.service.LibroService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner{
    private final LibroService libroService;
    private final AutorService autorService;

    @Override
    public void run(String... args){
        Autor a1 = Autor.builder().nombre("Victor Hugo").nacionalidad("Francés").build();
        Autor a2 = Autor.builder().nombre("José Saramago").nacionalidad("Portugués").build();
        Autor a3 = Autor.builder().nombre("Agatha Christie").nacionalidad("Inglés").build();
        Autor a4 = Autor.builder().nombre("Arturo Pérez Reverte").nacionalidad("Español").build();
        Autor a5 = Autor.builder().nombre("Dolores Redondo").nacionalidad("Español").build();
        Autor a6 = Autor.builder().nombre("Orwell").nacionalidad("Estadounidense").build();

        List.of(a1,a2,a3,a4,a5,a6).forEach(a -> autorService.guardar(a));

        Libro l1 = Libro.builder().isbn("GG11111111").titulo("Los miserables").autor(a1).build();
        Libro l2 = Libro.builder().isbn("GG11111112").titulo("Ensayo sobre la ceguera").autor(a2).build();
        Libro l3 = Libro.builder().isbn("GG11111113").titulo("7 negritos").autor(a3).build();
        Libro l4 = Libro.builder().isbn("GG11111114").titulo("Los perros duros no bailan").autor(a4).build();

        List.of(l1,l2,l3,l4).forEach(l -> libroService.guardar(l));
    }
}
