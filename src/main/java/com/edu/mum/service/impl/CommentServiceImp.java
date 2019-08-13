package com.edu.mum.service.impl;

import com.edu.mum.domain.Comment;
import com.edu.mum.repository.CommentRepository;
import com.edu.mum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImp implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImp(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.saveAndFlush(comment);
    }

    @Override
    public List<Comment> findLates5Comments() {
        return commentRepository.findLates5Comments();
    }
}
