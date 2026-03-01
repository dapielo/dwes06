package com.david.edulib.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AutorDTO {
    private Integer id;
    @NotBlank
    private String nombre;
    private String nacionalidad;
}
