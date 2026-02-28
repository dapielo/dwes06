package com.david.edulib.mapper;

import org.springframework.stereotype.Component;
import com.david.edulib.dto.LibroDTO;
import com.david.edulib.entity.Libro;
import com.david.edulib.exception.LibroException;

@Component
public class LibroMapper {

    // El AUTOR va vacío, el mapper no puede acceder al repositorio, tendrá que ser el servicio el que rellene esta información con lo que nos aporta el DTO
    public Libro toEntity(LibroDTO dto) {
        if (dto == null) {
            throw new LibroException("Intentando reguistrar un libro sin datos");
        }
        return Libro.builder()
                .isbn(dto.getIsbn()) // Se lo pasamos, si trae isbn existente JPA hará un update, si es nuevo hara un insert
                .build();
    }   

    public LibroDTO toDTO(Libro libro){
        if (libro == null) {
            throw new LibroException("Intentando recuperar un libro que no existe");
        }
        return LibroDTO.builder()
                .isbn(libro.getIsbn())
                .titulo(libro.getTitulo())
                .nombreAutor(libro.getAutor().getNombre())
                .nacionalidadAutor(libro.getAutor().getNacionalidad())
                // OJO, auqnue Prestamo no sea null FechaEntrega puede serlo
                .fechaEntrega(libro.getPrestamo().getFechaEntrega()!=null ? libro.getPrestamo().getFechaEntrega().toString() : "Sin prestar")
                .fechaPrestamo(libro.getPrestamo().getFechaPrestamo()!=null ? libro.getPrestamo().getFechaPrestamo().toString() : "Sin prestar")
                .build();
    }
}
