package com.masterproject.ddd.article.service.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.masterproject.ddd.core.api.article.model.ArticleStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    private String id;
    private String title;
    private String description;
    private String body;
    private String authorID;
    private String authorName;
    // @Convert(converter = ArticleAuthorDetailsConverter.class)
    // private ProfileDetails articleAuthor;
    private ArticleStatus status;

}
