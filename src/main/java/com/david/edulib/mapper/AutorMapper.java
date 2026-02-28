package com.david.edulib.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;
import com.david.edulib.dto.AutorDTO;
import com.david.edulib.entity.Autor;
import com.david.edulib.exception.AutorException;

@Component
public class AutorMapper {

    public Autor toEntity(AutorDTO dto){
        if (dto == null) {
            throw new AutorException("Intentando crear un autor con datos vacíos");
        }
        return Autor.builder()
                .id(dto.getId()) // Se lo pasamos, si tra uno JPA hace un update, si no trae (null) hará un insert (clave autogenerada)
                .nombre(dto.getNombre())
                .nacionalidad(dto.getNacionalidad())
                .build();
    }

    public AutorDTO toDTO(Autor autor){
        if (autor == null) {
            throw new AutorException("Intentando recuperar un autor que no existe");
        }
        return AutorDTO.builder()
                .id(autor.getId())
                .nombre(autor.getNombre())
                .nacionalidad(autor.getNacionalidad())
                .build();
    }
}
