package com.masterproject.ddd.comment.service.model;

import javax.persistence.Entity;
import javax.persistence.Id;

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
    private String commentID;
    private String articleID;
    private String authorID;
    private String authorName;
    private String message;
    private CommentStatus status;
}
