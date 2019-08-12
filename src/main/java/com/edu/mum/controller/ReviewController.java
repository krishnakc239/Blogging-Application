package com.edu.mum.controller;

import com.edu.mum.domain.Post;
import com.edu.mum.domain.Review;
import com.edu.mum.domain.User;
import com.edu.mum.service.PostService;
import com.edu.mum.service.ReviewService;
import com.edu.mum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;


    @GetMapping("/posts/review/{id}")
    public String upateRating(@PathVariable("id") Long postId,
                              @RequestParam(value = "rating") String rating){
        System.out.println("inside review mehtod");
        System.out.println("post id :"+postId);
        System.out.println("post rating :"+ rating);
        Review review = new Review();
            review.setRating(Integer.parseInt(rating));

        if (postId != null){

            Optional<Post> reviewedPost = postService.findById(postId);
            review.setPost(reviewedPost.get());
//            User reviewer = SessionUtils.getCurrentUser();

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("auth.getName =="+ auth.getName());
            if (auth != null || auth.getName().equalsIgnoreCase("anonymousUser")){
                Optional<User> user = userService.findByUsername(auth.getName());
                if (!user.isPresent()){
                    return "redirect:/users/login";
                }
                review.setUser(user.get());
                reviewService.save(review);
                System.out.println("review saved!!!!!!");
            }

        }

        return "redirect:/index";
    }
}
