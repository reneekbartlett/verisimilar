package com.reneekbartlett.verisimilar.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomMvcErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute("jakarta.servlet.error.status_code");

        if (status != null && Integer.parseInt(status.toString()) == 404) {
            model.addAttribute("customMessage", "We tracked this missing link for our team!");
            return new ModelAndView("error/404"); // returns templates/error/404.html
        }

        return new ModelAndView("error/generic"); // fallback for 500, 403, etc.
    }
}
