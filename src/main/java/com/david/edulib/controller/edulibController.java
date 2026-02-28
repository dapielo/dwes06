package com.david.edulib.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.david.edulib.dto.LibroDTO;
import com.david.edulib.service.AutorService;
import com.david.edulib.service.LibroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/libro") // para que todos los endpoint vayan sobre esta ruta
public class edulibController {
    private final LibroService libroService;
    private final AutorService autorService;

    // Página principal
    @GetMapping
    public String principal(Model model){
        model.addAttribute("libros", libroService.obtenerTodos());
        model.addAttribute("autores", autorService.obtenerTodos());
        return "index";
    }

    // Página para añadir libro, nos devuelve el formulario de creación, le pasamos un DTO vacío
    @GetMapping("/add")
    public String getFormulario(Model model){
        model.addAttribute("libroDTO",LibroDTO.builder().build());
        // Le pasamos al modelo la lista de autores para que los use en el select
        model.addAttribute("autores",autorService.obtenerTodos());
        return "formulario";
    }

    // Post para verificar si el dto es valido y añadirlo de ser así o de devolver el formulario si no es correcto
    @PostMapping("/add")
    public String addLibro(@Valid @ModelAttribute LibroDTO libroDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            // tenemos que volver a inyectar los autores sinó el modelo no los tiene para mostrar
            model.addAttribute("autores",autorService.obtenerTodos());
            return "formulario";
        }
        redirectAttributes.addFlashAttribute("mensajeOK","Libro agregado correctamente");
        libroService.guardar(libroDTO);
        return "redirect:/";
    }

    // Endpoint para borrar por id (ISBN)
    @GetMapping("/delete/{isbn}")
    public String delete(@PathVariable String isbn, RedirectAttributes redirectAttributes){
        libroService.borrarPorId(isbn);
        redirectAttributes.addFlashAttribute("mensajeOK","El libro con ISBN:"+isbn+" ha sido borrado");
        return "redirect:/";
    }

    // Endpoint para hacer cambio entre prestado y no prestado
    @GetMapping("/prestar/{isbn}")
    public String togglePrestar(@PathVariable String isbn, RedirectAttributes redirectAttributes){
        libroService.togglePrestamo(isbn);
        redirectAttributes.addFlashAttribute("mensajeOK","Se ha actualizado el préstamo del libro "+isbn);
        return "redirect:/";
    }

    // Endpoint para editar libros, recibe el isbn y con el inyecta al modelo el DTO del libro con ese isbn y la vista de autores, luego 
    // devuelve el formulario de creación que rellenará los campos con los datos del DTO que hemos inyectado en la vista
    @GetMapping("/edit/{isbn}")
    public String edit(@PathVariable String isbn, Model model){
        model.addAttribute("libroDTO",libroService.obtenerPorId(isbn));
        model.addAttribute("autores",autorService.obtenerTodos());
        return "formulario";
    }
}
