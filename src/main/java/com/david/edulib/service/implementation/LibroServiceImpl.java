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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class LibroServiceImpl implements LibroService{
    private final LibroRepository libroRepository;
    private final LibroMapper libroMapper;
    private final AutorService autorService;
    private final AutorRepository autorRepository;

    @Override
    public List<LibroDTO> obtenerTodos(){
        return libroRepository.findAllByOrderByTituloAsc().stream()
                .map(libroMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<LibroDTO> obtenerPorId(String isbn){
        return libroRepository.findById(isbn).map(libroMapper::toDTO);
    }

    @Override
    public LibroDTO guardar(LibroDTO libroDTO) throws LibroException{
        // Primero comprobamos si el dto trae un id de un libro en el repository (actualizacion de una fila existente)
        Libro libro;
        if (libroRepository.findById(libroDTO.getIsbn()).isPresent()){
            libro = libroRepository.findById(libroDTO.getIsbn()).orElseThrow(() -> new LibroException("No existe un libro con ISBN "+libroDTO.getIsbn()));
            log.error("este libro tiene prestamo?"+String.valueOf(libro.getPrestamo()!=null));
            // No puede haber un libro sin autor en la BD asi que no se comprueba si el optional esta vacio
            libro.setAutor(autorService.obtenerPorNombre(libroDTO.getNombreAutor()).get());
            libro.setTitulo(libroDTO.getTitulo());
        } else {
            // Si el isPresent nos devuelve false, es un libro nuevo, antes creamos el objeto prestamo para asociarselo
            Prestamo p = Prestamo.builder()
                            .fechaPrestamo(LocalDate.now())
                            .fechaEntrega(LocalDate.now().plusDays(15))
                            .build();

            libro = Libro.builder()
                    .isbn(libroDTO.getIsbn())
                    .titulo(libroDTO.getTitulo())
                    .autor(autorRepository.findByNombre(libroDTO.getNombreAutor()).get())
                    .prestamo(p)
                    .build();

            // IMPORTANTE, necesitamos indicarle a prestamo cual es su libro, para que al guardar el libro se guarde su prestamo con la clave
            // foránea de libro, aunque en libro estamos indicando que el dueño de la relación es Prestamo con el mappedBy, eso solo hace que 
            // sea prestamo el que almacene referencia a libro, pero igualmente ambos deben conocerse entre si (Libro tiene que tener al prestamo
            // y prestamo tiene que tener al libro)
            p.setLibro(libro);
        }
        // Guardamos la entidad y devolvemos un DTO
        libroRepository.save(libro);
        return libroMapper.toDTO(libro);
    }

    @Override
    @Transactional
    public void borrarPorId(String isbn){
        libroRepository.deleteById(isbn);
    }

    @Override 
    @Transactional
    public void borrar(Libro libro){
        libroRepository.delete(libro);
    }

    @Override
    @Transactional
    public void togglePrestamo(String isbn){
        // Si no existe el libro el Optional lanza una excepcion
        Libro libro = libroRepository.findById(isbn).orElseThrow(() -> new LibroException("No existe el libro"));
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

    @Override 
    @Transactional
    public void guardar(Libro libro){
        // Ojo con este método, recuerda que ambas entidades deben de conocerse entre si al estar relacionadas para que la persistencia no falle
        // si libro no conoce a prestamo o prestamo no conoce a libro, en la base de datos no van a estar relacionadas y el guardado en cascada
        // tampoco funcionaría
        Prestamo p = Prestamo.builder().libro(libro).build();
        libro.setPrestamo(p);
        libroRepository.save(libro);
    }
}
