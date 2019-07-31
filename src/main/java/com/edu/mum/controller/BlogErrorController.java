package com.edu.mum.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class BlogErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(PATH)
    public ModelAndView error() {
        return new ModelAndView("views/error/default");
    }

    @GetMapping("/error/403")
    public ModelAndView error403() {
        return new ModelAndView("views/error/403");
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    // for 403 access denied page
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView accesssDenied(Principal user) {

        ModelAndView model = new ModelAndView();

        if (user != null) {
            model.addObject("msg", "Hi " + user.getName()
                    + ", you do not have permission to access this page!");
        } else {
            model.addObject("msg",
                    "You do not have permission to access this page!");
        }

        model.setViewName("views/error/403");
        return model;

    }
}
