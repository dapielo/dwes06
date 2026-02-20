package com.david.edulib.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;
import com.david.edulib.dto.AutorDTO;
import com.david.edulib.entity.Autor;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AutorMapper {

    public Autor toEntity(AutorDTO dto){
        if (dto == null) return null;
        return Autor.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .nacionalidad(dto.getNacionalidad())
                // Importante inicializarlo para no arrastrar un null y que luego spring no falle al intentar recuperar los libros del autor
                .libros(new ArrayList<>()) 
                .build();
    }

    public AutorDTO toDTO(Autor autor){
        if (autor == null) return null;
        return AutorDTO.builder()
                .id(autor.getId())
                .nombre(autor.getNombre())
                .nacionalidad(autor.getNacionalidad())
                .build();
    }
}
