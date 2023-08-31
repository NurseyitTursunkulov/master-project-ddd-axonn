package com.masterproject.ddd.comment.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masterproject.ddd.comment.service.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, String> {

    List<Comment> findByArticleID(String articleID);

    List<Comment> findByAuthorID(String authorID);
}
