package com.edu.mum.controller;

import com.edu.mum.domain.Post;
import com.edu.mum.service.PostService;
import com.edu.mum.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomePageController {

    @Autowired
    private PostService postService;

    @RequestMapping(value={"/"},
            method= RequestMethod.GET)
    public String homepage()
    {
        return "redirect:/index";
    }


    @GetMapping("/index")
    public String home(@RequestParam(defaultValue = "0") int page,
                       Model model) {
        System.out.println("page number === "+ page);
        Page<Post> posts = postService.findAllOrderedByDatePageable(page);
        Pager pager = new Pager(posts);
        model.addAttribute("pager", pager);

        return "views/home/index";
    }

    }

