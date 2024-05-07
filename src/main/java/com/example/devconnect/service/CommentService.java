package com.example.devconnect.service;

import com.example.devconnect.model.Comment;
import com.example.devconnect.model.Project;
import com.example.devconnect.model.Skill;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments(Project project) {
        return commentRepository.findByProject(project);
    }

    public Comment getCommentById(Integer commentId) {
        return commentRepository.findById(commentId).get();
    }

    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }
}
