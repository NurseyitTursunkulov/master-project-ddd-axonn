package com.masterproject.ddd.comment.service.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.masterproject.ddd.core.api.comment.model.CommentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    private String id;
    private String articleID;
    private String authorID;
    private String authorName;
    private String message;
    private CommentStatus status;

    private @NotNull Instant createdOn = Instant.now();
    private @NotNull Instant updatedOn = Instant.now();
}
