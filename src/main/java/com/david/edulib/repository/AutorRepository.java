package com.david.edulib.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.david.edulib.entity.Autor;


@Repository
public interface AutorRepository extends JpaRepository<Autor, Integer> {
    // Este método es para buscar autores por nombre, ya que el nombre tiene la propiedad unique = true
    public Optional<Autor> findByNombre(String nombre);
    public List<Autor> findAllByOrderByNombreAsc();
}
