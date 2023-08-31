package com.masterproject.ddd.comment.service.handler;

import java.util.List;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.masterproject.ddd.comment.service.model.Comment;
import com.masterproject.ddd.comment.service.repository.CommentRepository;
import com.masterproject.ddd.core.api.comment.event.CommentCreated;
import com.masterproject.ddd.core.api.comment.event.CommentPublished;
import com.masterproject.ddd.core.api.comment.event.CommentRejected;
import com.masterproject.ddd.core.api.comment.event.CommentRemoved;
import com.masterproject.ddd.core.api.comment.model.CommentStatus;
import com.masterproject.ddd.core.api.comment.query.GetCommentById;
import com.masterproject.ddd.core.api.comment.query.GetCommentsByArticleId;
import com.masterproject.ddd.core.api.comment.query.GetCommentsByAuthorId;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CommentProjection {

    private final CommentRepository repository;

    @EventHandler
    public void on(CommentCreated event) {
        var comment = new Comment();
        comment.setId(event.Id);
        comment.setArticleID(event.articleId);
        comment.setMessage(event.message);
        comment.setAuthorID(event.authorId);
        comment.setStatus(CommentStatus.CREATED);
        repository.save(comment);
    }

    @EventHandler
    public void on(CommentPublished event) {
        this.setStatusIfExits(event.Id, CommentStatus.PUBLISHED);
    }

    @EventHandler
    public void on(CommentRejected event) {
        this.deleteIfExits(event.Id);
        // this.setStatusIfExits(event.Id, CommentStatus.REJECTED);
    }

    @EventHandler
    public void on(CommentRemoved event) {
        this.deleteIfExits(event.Id);
        // this.setStatusIfExits(event.Id, CommentStatus.REMOVED);
    }

    private void deleteIfExits(String id) {
        var optional = repository.findById(id);
        if (optional.isPresent()) {
            var userProfile = optional.get();
            repository.delete(userProfile);
        }
    }

    private void setStatusIfExits(String id, CommentStatus status) {
        var optional = repository.findById(id);
        if (optional.isPresent()) {
            var comment = optional.get();
            comment.setStatus(status);
            repository.save(comment);
        }
    }

    @QueryHandler
    public List<Comment> on(GetCommentsByArticleId query) {
        return repository.findByArticleID(query.getArticleId());
    }

    @QueryHandler
    public List<Comment> on(GetCommentsByAuthorId query) {
        return repository.findByArticleID(query.getAuthorId());
    }

    @QueryHandler
    public Comment on(GetCommentById query) {
        return repository.findById(query.getId())
                .orElseThrow(() -> new RuntimeException("Comment with Id:" + query.getId() +
                        " does not exits"));
    }

}
