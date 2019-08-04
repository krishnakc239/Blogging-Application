package com.edu.mum.controller;

import com.edu.mum.domain.Post;
import com.edu.mum.domain.User;
import com.edu.mum.service.PostService;
import com.edu.mum.service.UserService;
import com.edu.mum.util.Pager;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.misc.BASE64Encoder;

import javax.sql.rowset.serial.SerialBlob;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @RequestMapping(value = "/posts/create", method = RequestMethod.GET)
    public ModelAndView create(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("post",new Post());
        modelAndView.setViewName("views/posts/create");
        return modelAndView;
    }

    @RequestMapping(value = "/posts/create", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String create(@Valid Post post, @RequestParam("file") MultipartFile imgFile, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        if( post.getTitle().isEmpty() ){
            bindingResult.rejectValue("title", "error.post", "Title cannot be empty");
        }
        if( post.getBody().isEmpty() ){System.out.println("body empty !!!!!!!!");

            bindingResult.rejectValue("body", "error.post", "Content cannot be empty");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("successMessage", "There is problem creating this post");
            modelAndView.setViewName("views/posts/create");
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Optional<User> user = this.userService.findByUsername(auth.getName());
            if( !user.isPresent() ){
            bindingResult.rejectValue("user", "error.post", "User cannot be null");
        }
            MultipartFile img = imgFile;
            post.setCoverImage(img.getBytes());
            post.setUser(user.get());
            this.postService.create(post);
            System.out.println("post created !!!!!!!!");
            redirectAttributes.addFlashAttribute("successMessage","Post has been created, we will let you know when aproved");

        }
        return "redirect:/posts/create";

    }
    @RequestMapping("/posts/view/{id}")
    public String view(@PathVariable("id") Long id, Model model){
        Optional<Post> post = this.postService.findById(id);
//        if( post == null ){
//            notifyService.addErrorMessage("Cannot find post #" + id);
//            return "redirect:/";
//        }
        model.addAttribute("post", post);
        // To have something like src/main/resources/templates/<CONTROLLER-NAME>/<Mapping-Name-view>
        return "views/posts/view";
    }
//    @RequestMapping(value = "/newPost", method = RequestMethod.POST)
//    public String createNewPost(@Valid Post post,
//                                BindingResult bindingResult) {
//
//        if (bindingResult.hasErrors()) {
//            return "/postForm";
//        } else {
//            postService.create(post);
//            return "redirect:/blog/" + post.getUser().getUsername();
//        }
//    }

//    @RequestMapping(value = "/editPost/{id}", method = RequestMethod.GET)
//    public String editPostWithId(@PathVariable Long id,
//                                 Principal principal,
//                                 Model model) {
//
//        Optional<Post> optionalPost = postService.findById(id);
//
//        if (optionalPost.isPresent()) {
//            Post post = optionalPost.get();
//
//            if (isPrincipalOwnerOfPost(principal, post)) {
//                model.addAttribute("post", post);
//                return "/postForm";
//            } else {
//                return "/403";
//            }
//
//        } else {
//            return "/error";
//        }
//    }

//    @RequestMapping(value = "/post/{id}", method = RequestMethod.GET)
//    public String getPostWithId(@PathVariable Long id,
//                                Principal principal,
//                                Model model) {
//
//        Optional<Post> optionalPost = postService.findById(id);
//
//        if (optionalPost.isPresent()) {
//            Post post = optionalPost.get();
//
//            model.addAttribute("post", post);
//            if (isPrincipalOwnerOfPost(principal, post)) {
//                model.addAttribute("username", principal.getName());
//            }
//
//            return "/post";
//
//        } else {
//            return "/error";
//        }
//    }

    @RequestMapping(value = "/post/{id}", method = RequestMethod.DELETE)
    public String deletePostWithId(@PathVariable Long id,
                                   Principal principal) {

        Optional<Post> optionalPost = postService.findById(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            if (isPrincipalOwnerOfPost(principal, post)) {
                postService.delete(post);
                return "redirect:/";
            } else {
                return "views/error/403";
            }

        } else {
            return "views/error/default";
        }
    }
    @RequestMapping("/posts")
    public String index(@RequestParam(defaultValue = "0") int page, Model model){
        Page<Post> posts = this.postService.findAllOrderedByDatePageable(page);
        Pager pager = new Pager(posts);

//        model.addAttribute("image", Base64.getEncoder().encodeToString())
        model.addAttribute("pager", pager);
        model.addAttribute("posts", posts);
        return "views/posts/postList";
    }

    @PostMapping("/post/review")
    public String ratePost(@RequestParam("rating") Integer rating, @ModelAttribute Post post){
        post.updateRatedCount();
        post.updateAvgRating(rating);
        return "redirect:/posts";
    }

    private boolean isPrincipalOwnerOfPost(Principal principal, Post post) {
        return principal != null && principal.getName().equals(post.getUser().getUsername());
    }
}
