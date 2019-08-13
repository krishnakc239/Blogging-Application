package com.edu.mum.service;

import com.edu.mum.domain.Comment;

import java.util.List;

public interface CommentService {

    Comment save(Comment comment);
   List<Comment> findLates5Comments();
}
