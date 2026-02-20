package com.david.edulib.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.david.edulib.entity.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, String>{
    List<Libro> findAllByOrderByTituloAsc();
}
