package com.david.edulib.service.implementation;

import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.david.edulib.dto.AutorDTO;
import com.david.edulib.entity.Autor;
import com.david.edulib.exception.AutorException;
import com.david.edulib.mapper.AutorMapper;
import com.david.edulib.repository.AutorRepository;
import com.david.edulib.service.AutorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class AutorServiceImpl implements AutorService{
    private final AutorMapper autorMapper;
    private final AutorRepository autorRepository;

    // READ
    @Override
    public List<AutorDTO> obtenerTodos(){
        // Devolvemos DTO no entidad
        return autorRepository.findAllByOrderByNombreAsc().stream()
            .map(autorMapper::toDTO)
            .toList();
    }

    @Override
    public Optional<AutorDTO> obtenerPorId(Integer id) {
        // map de Optional, permite hacer una transformacion al contenido si no esta vacio
        return autorRepository.findById(id).map(autorMapper::toDTO);
    }

    // Este método nos devuelve la entity poruqe lo vamos a usar para registrar el autor en la entidad libro en libroService
    @Override 
    public Optional<Autor> obtenerPorNombre(String nombre){
        return autorRepository.findByNombre(nombre);
    }

    // CREATE Y UPDATE
    @Override
    @Transactional // Poruqe escribe en la base de datos (no es un select)
    public AutorDTO guardar(AutorDTO autorDTO){
        if (autorDTO == null){
            throw new AutorException("Intentado registrar un autor sin datos");
        }
        Autor autor;
        // Edicion, viene con Id
        if (autorDTO.getId()!=null){
            // Si no existe lanzamos exception, sino continuamos con la logica de edicion, la lista de libros me la trae la relacion EAGER
            autor = autorRepository.findByNombre(autorDTO.getNombre()).orElseThrow(() -> new AutorException("No existe el autor con id "+autorDTO.getId()));
            autor.setNombre(autorDTO.getNombre());
            autor.setNacionalidad(autorDTO.getNacionalidad());
        // Creación, viene sin Id    
        } else {
            // El arraylist de libros se inicializa solo con @Builder.Default de autor
            autor = autorRepository.save(autorMapper.toEntity(autorDTO));
        }
        return autorMapper.toDTO(autor);
    }
    
    // Para el DataLoader
    @Override
    @Transactional
    public void guardar(Autor autor){
        if (autor != null){
            autorRepository.save(autor);
        }
    }

    // DELETE
    @Override
    @Transactional
    public void borrarPorId(Integer id){
        if (id != null){    
            autorRepository.deleteById(id);
        }
    }

    @Override 
    @Transactional
    public void borrar(AutorDTO autorDTO){
        if (autorDTO != null){
            autorRepository.delete(autorMapper.toEntity(autorDTO));
        }
    }
}
