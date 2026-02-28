package com.david.edulib.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Va a capturar todas las Excepciones del Controller
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AutorException.class)
    public String autorNotFound(AutorException e, Model model){
        model.addAttribute("error",e.getMessage());
        return "error/404";
    }

    @ExceptionHandler(LibroException.class)
    public String libroNotFound(LibroException e, Model model){
        model.addAttribute("error",e.getMessage());
        return "error/404";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String duplicateHandler(DataIntegrityViolationException e, Model model){
        model.addAttribute("error","Se ha intentado registrar un libro con un ISBN que ya existe");
        return "error/404";
    }

    // Por si el acceso a algun dato falla
    @ExceptionHandler
    public String nullPointerHandler(NullPointerException e, Model model){
        model.addAttribute("error","Se ha intentado acceder a un dato que no existe");
        return "error/404";
    }
}
