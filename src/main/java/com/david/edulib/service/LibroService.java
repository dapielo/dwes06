package com.david.edulib.service;

import java.util.List;
import java.util.Optional;
import com.david.edulib.dto.LibroDTO;
import com.david.edulib.entity.Libro;
import com.david.edulib.exception.LibroException;

public interface LibroService {

    public List<LibroDTO> obtenerTodos();
    public Optional<LibroDTO> obtenerPorId(String isbn);
    public LibroDTO guardar(LibroDTO libroDTO) throws LibroException;
    public void borrarPorId(String isbn);
    public void borrar(Libro libro);
    public void togglePrestamo(String isbn);
    // Para el dataloader
    public void guardar(Libro libro);
}
