package com.david.edulib.service;

import java.util.List;
import java.util.Optional;

import com.david.edulib.dto.AutorDTO;
import com.david.edulib.entity.Autor;
import com.david.edulib.exception.AutorException;

public interface AutorService {

    public List<AutorDTO> obtenerTodos();
    public Optional<AutorDTO> obtenerPorId(Integer id);
    public Optional<Autor> obtenerPorNombre(String nombre);
    public AutorDTO guardar(AutorDTO autorDTO) throws AutorException;
    public void borrarPorId(Integer id);
    public void borrar(AutorDTO autorDTO);
    // Para el dataloader
    public void guardar(Autor autor);
}
