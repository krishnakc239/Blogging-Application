package com.edu.mum.repository;

import com.edu.mum.domain.Post;
import com.edu.mum.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findAllByPost(Post post);
}
