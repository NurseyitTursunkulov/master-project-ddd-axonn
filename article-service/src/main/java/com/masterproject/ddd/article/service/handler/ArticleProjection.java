package com.masterproject.ddd.article.service.handler;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.masterproject.ddd.article.service.model.Article;
import com.masterproject.ddd.article.service.repository.ArticleRepository;
import com.masterproject.ddd.core.api.article.event.ArticleClosed;
import com.masterproject.ddd.core.api.article.event.ArticleCreated;
import com.masterproject.ddd.core.api.article.event.ArticlePublisched;
import com.masterproject.ddd.core.api.article.event.ArticleRejected;
import com.masterproject.ddd.core.api.article.event.ArticleUpdated;
import com.masterproject.ddd.core.api.article.model.ArticleStatus;
import com.masterproject.ddd.core.api.article.query.GetArticleByID;
import com.masterproject.ddd.core.api.article.query.GetArticlesByStatus;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ArticleProjection {

    private final ArticleRepository repository;

    @EventHandler
    public void on(ArticleCreated event) {

        var articleDetails = event.articleDetails;
        var articleEntity = new Article();
        articleEntity.setId(event.Id);
        articleEntity.setTitle(articleDetails.getTitle());
        articleEntity.setDescription(articleDetails.getDescription());
        articleEntity.setBody(articleDetails.getBody());
        articleEntity.setAuthorID(event.authorID);
        articleEntity.setAuthorName(event.authorName);
        articleEntity.setStatus(ArticleStatus.CREATED);
        repository.save(articleEntity);
    }

    @EventHandler
    public void on(ArticleUpdated event) {
        var optional = repository.findById(event.Id);
        if (optional.isPresent()) {
            var article = optional.get();
            article.setTitle(event.articleDetails.getTitle());
            article.setDescription(event.articleDetails.getDescription());
            article.setBody(event.articleDetails.getBody());

            if (StringUtils.isNotBlank(event.authorName)) {
                article.setAuthorName(event.authorName);
            }
            repository.save(article);
        }
    }

    @EventHandler
    public void on(ArticlePublisched event) {
        this.setStatusIfExits(event.Id, ArticleStatus.PUBLISHED);
    }

    @EventHandler
    public void on(ArticleRejected event) {
        this.setStatusIfExits(event.Id, ArticleStatus.REJECTED);
    }

    @EventHandler
    public void on(ArticleClosed event) {
        this.setStatusIfExits(event.Id, ArticleStatus.CLOSED);
    }

    private void setStatusIfExits(String id, ArticleStatus status) {
        var optional = repository.findById(id);
        if (optional.isPresent()) {
            var articleEntity = optional.get();
            articleEntity.setStatus(status);
            repository.save(articleEntity);
        }
    }

    @QueryHandler
    public Article on(GetArticleByID query) {
        return repository.findById(query.getId())
                .orElseThrow(() -> new RuntimeException("Article with Id:" + query.getId() + " does not exits"));
    }

    @QueryHandler
    public List<Article> on(GetArticlesByStatus query) {
        return repository.findByStatus(query.getStatus());
    }

}
