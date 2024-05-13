package com.example.devconnect.repository;

import com.example.devconnect.model.Comment;
import com.example.devconnect.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByProject(Project project);

    @Procedure(procedureName = "add_comment")
    void addComment(@Param("p_project_id") Integer projectId,
                    @Param("p_owner_id") Integer ownerId,
                    @Param("p_value") String commentText);
}
