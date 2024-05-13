package com.example.devconnect.service;

import com.example.devconnect.model.Comment;
import com.example.devconnect.model.Project;
import com.example.devconnect.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void createComment(Comment comment) {
        commentRepository.save(comment);
    }

    public void createComment(Integer projectId, Integer ownerId, String commentText) {
        commentRepository.addComment(projectId, ownerId, commentText);
    }

    public List<Comment> getAllComments(Project project) {
        return commentRepository.findByProject(project);
    }

    public Comment getCommentById(Integer commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        return comment.orElse(null);
    }

    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }
}
