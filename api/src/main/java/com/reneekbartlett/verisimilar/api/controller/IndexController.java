package com.reneekbartlett.verisimilar.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String viewHomePage(Model model) {

        // Inject a dynamic attribute into the UI context
        model.addAttribute("greeting", "Verisimilar API");

        // Tells Spring to render 'src/main/resources/templates/index.html'
        return "index"; 
    }
}
