package com.edu.mum.service;

import com.edu.mum.domain.Post;
import com.edu.mum.domain.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    void save(Review review);
    Optional<Review> findById(Long id);
    List<Review> findAllReviewsByPost(Post post);

}