package com.masterproject.ddd.article.service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masterproject.ddd.article.service.model.Article;
import com.masterproject.ddd.core.api.article.model.ArticleStatus;

public interface ArticleRepository extends JpaRepository<Article, String> {

    List<Article> findByStatus(ArticleStatus status);

    List<Article> findByAuthorID(UUID authorID);

}
