package com.david.edulib.service.implementation;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.david.edulib.dto.LibroDTO;
import com.david.edulib.entity.Libro;
import com.david.edulib.entity.Prestamo;
import com.david.edulib.exception.LibroException;
import com.david.edulib.mapper.LibroMapper;
import com.david.edulib.repository.AutorRepository;
import com.david.edulib.repository.LibroRepository;
import com.david.edulib.service.AutorService;
import com.david.edulib.service.LibroService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class LibroServiceImpl implements LibroService{
    private final LibroRepository libroRepository;
    private final LibroMapper libroMapper;
    private final AutorService autorService;
    private final AutorRepository autorRepository;

    // READ
    @Override
    public List<LibroDTO> obtenerTodos(){
        return libroRepository.findAllByOrderByTituloAsc().stream()
                .map(libroMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<LibroDTO> obtenerPorId(String id){
        return libroRepository.findById(id).map(libroMapper::toDTO);
    }

    // CREATE Y UPDATE
    @Override
    @Transactional
    public LibroDTO guardar(LibroDTO libroDTO) throws LibroException{
        if (libroDTO == null){
            throw new LibroException("Intentando guardar un libro sin datos");
        }
        Libro libro;
        // Lógica de edición (viene con un isbn existente en la base de datos)
        if (libroRepository.findById(libroDTO.getIsbn()).isPresent()){
            libro = libroRepository.findById(libroDTO.getIsbn()).orElseThrow(() -> new LibroException("No existe un libro con ISBN "+libroDTO.getIsbn()));
            libro.setTitulo(libroDTO.getTitulo());
            libro.setAutor(autorService.obtenerPorNombre(libroDTO.getNombreAutor()).orElse(libro.getAutor()));
        // Lógica de creación (el isbn no esta en la BD), no inyectamos el prestamo porque nunca es null es la base de datos, ya trae uno
        } else {
            libro = Libro.builder()
                    .isbn(libroDTO.getIsbn())
                    .titulo(libroDTO.getTitulo())
                    .autor(autorRepository.findByNombre(libroDTO.getNombreAutor()).orElseThrow(() -> new LibroException("No se puede registrar un libro sin autor")))
                    .build();
            // Creamos su objeto prestamo y le pasamos el objeto libro que tiene asociado ya que Prestamo es el dueño de la relacion
            Prestamo p = Prestamo.builder()
                            .fechaPrestamo(LocalDate.now())
                            .fechaEntrega(LocalDate.now().plusDays(15))
                            .libro(libro)
                            .build();
            // Siendo bireccional es buena practica pasarle a libro tambien su prestamo
            libro.setPrestamo(p);
        }
        // Guardamos la entidad y devolvemos un DTO
        libroRepository.save(libro);
        return libroMapper.toDTO(libro);
    }

    // Método para el DataLoader, creo el prestamo porque no lo creo en el DataLoader
    @Override 
    @Transactional
    public void guardar(Libro libro){
        Prestamo p = Prestamo.builder().libro(libro).build();
        libro.setPrestamo(p);
        libroRepository.save(libro);
    }

    // DELETE
    @Override
    @Transactional
    public void borrarPorId(String id){
        if (id != null){
            libroRepository.deleteById(id);
        }
    }

    @Override 
    @Transactional
    public void borrar(Libro libro){
        if (libro != null) {
            libroRepository.delete(libro);
        }
    }

    @Override
    @Transactional
    public void togglePrestamo(String id){
        // Si no existe el libro el Optional lanza una excepcion
        if (id != null){
            Libro libro = libroRepository.findById(id).orElseThrow(() -> new LibroException("No existe el libro"));
            Prestamo p = libro.getPrestamo();
            if (p.getFechaPrestamo()!=null){
                p.setFechaEntrega(null);
                p.setFechaPrestamo(null);
            } else {
                p.setFechaPrestamo(LocalDate.now());
                p.setFechaEntrega(LocalDate.now().plus(Period.ofDays(15)));
            }
            libroRepository.save(libro);
        }
    }
}
