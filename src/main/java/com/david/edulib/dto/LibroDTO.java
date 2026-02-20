package com.david.edulib.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LibroDTO {
    @NotBlank(message = "El ISBN no puede estar vacío")
    @Size(min = 10, max = 13, message = "El ISBN tiene que ser tener entre 10 y 13 caracteres")
    private String isbn;

    @NotBlank(message = "No se puede registrar un libro sin título")
    private String titulo;

    @NotEmpty(message = "Selecciona un autor")
    private String nombreAutor;

    private String nacionalidadAutor;
    private String fechaPrestamo;
    private String fechaEntrega;
}
