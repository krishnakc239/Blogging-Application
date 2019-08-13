package com.edu.mum.repository;

import com.edu.mum.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value="SELECT c.* FROM comments c ORDER BY c.create_date DESC", nativeQuery = true)
    List<Comment> findLates5Comments();
}
