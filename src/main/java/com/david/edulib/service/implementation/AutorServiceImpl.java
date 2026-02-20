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

// Ojo con la implementación del servicio, como va a pasar información para la vista, y la vista trabaja con DTOs pero el repositorio con
// Entitys, no devolvemos el dato directamente, devolvemos la conversión a DTO que hacemos a través del mapper
    @Override
    public List<AutorDTO> obtenerTodos(){
        return autorRepository.findAllByOrderByNombreAsc().stream()
            .map(autorMapper::toDTO)
            .toList();
    }

    @Override
    public Optional<AutorDTO> obtenerPorId(Integer id) {
        // OJO, arriba el map es el de stream, porque tenemos una lista, aqui es un map de optional, que trabaja sobre nuestra clase pero 
        // devuelve un optional igualmente
        return autorRepository.findById(id).map(autorMapper::toDTO);
    }

    // Este método nos devuelve la entity poruqe lo vamos a usar para registrar el autor en la entidad libro en libroService
    @Override 
    public Optional<Autor> obtenerPorNombre(String nombre){
        return autorRepository.findByNombre(nombre);
    }

    @Override
    @Transactional // Poruqe escribe en la base de datos (no es un select)
    public AutorDTO guardar(AutorDTO autorDTO) throws AutorException{
        Autor autor;
        // Primero, si viene con ID, vamos a proceder con una actualizacion de los datos del repositorio
        if (autorDTO.getId()!=null){
            // Si no existe lanzamos exception, sino continuamos con la logica de edicion
            autor = autorRepository.findByNombre(autorDTO.getNombre()).orElseThrow(() -> new AutorException("No existe el autor con id "+autorDTO.getId()));
            autor.setNombre(autorDTO.getNombre());
            autor.setNacionalidad(autorDTO.getNacionalidad());
        } else {
            // Si no, es que el autor no trae ID, con lo cual es una nueva entrada
            autor = autorRepository.save(autorMapper.toEntity(autorDTO));
        }
        return autorMapper.toDTO(autor);
    }

    @Override
    @Transactional
    public void borrarPorId(Integer id){
        autorRepository.deleteById(id);
    }

    @Override 
    @Transactional
    public void borrar(AutorDTO autorDTO){
        autorRepository.delete(autorMapper.toEntity(autorDTO));
    }

    @Override
    @Transactional
    public void guardar(Autor autor){
        autorRepository.save(autor);
    }
}
