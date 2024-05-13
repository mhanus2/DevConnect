package com.example.devconnect.service;

import com.example.devconnect.model.Comment;
import com.example.devconnect.model.Project;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    void createComment(Comment comment);

    void createComment(Integer projectId, Integer ownerId, String commentText);

    List<Comment> getAllComments(Project project);

    Comment getCommentById(Integer commentId);

    void deleteComment(Integer id);
}
