package com.edu.mum.controller;

import com.edu.mum.domain.User;
import com.edu.mum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

//    @RequestMapping("/users/login")
//    public String login(){
//        // User doesn't need to re-enter credentials
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if ( (auth instanceof AnonymousAuthenticationToken) ) {
//            System.out.println("auth instanceof AnonymousAuthenticationToken ");
//            return "views/users/login";
//        } else {
//            System.out.println("user already logged in !!!!!!!!!!!");
//            return "redirect:/";
//        }
//    }
    // Spring security will see this message.
    @RequestMapping(value = "/users/login", method = RequestMethod.GET)
    public ModelAndView login1(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("username: " + auth.getName());
        ModelAndView m = new ModelAndView();
        if (error != null) {
            m.addObject("error", "Invalid username and password error.");
        }

        if (logout != null) {
            m.addObject("msg", "You have left successfully.");
        }

        m.setViewName("views/users/login");
        return m;
    }


    @RequestMapping(value="/admin/index", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.get().getFirstName() + " " + user.get().getLastName() + " (" + user.get().getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("views/home/home");
        return modelAndView;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout) {

        ModelAndView m = new ModelAndView();
        if (error != null) {
            m.addObject("error", "Invalid username and password error.");
        }

        if (logout != null) {
            m.addObject("msg", "You have left successfully.");
        }

        m.setViewName("views/users/login");
        return m;
    }


}
