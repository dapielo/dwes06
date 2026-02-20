package com.david.edulib.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AutorDTO {
    private Integer id;
    private String nombre;
    private String nacionalidad;
}
