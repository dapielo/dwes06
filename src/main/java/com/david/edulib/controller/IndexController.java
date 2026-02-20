package com.david.edulib.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String paginaInicio(){
        return "redirect:/libro";
    }
}
