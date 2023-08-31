package com.masterproject.ddd.article.service.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.util.Assert;

import com.masterproject.ddd.core.api.article.command.CloseArticle;
import com.masterproject.ddd.core.api.article.command.CreateArticle;
import com.masterproject.ddd.core.api.article.command.PublisheArticle;
import com.masterproject.ddd.core.api.article.command.RejectArticle;
import com.masterproject.ddd.core.api.article.command.UpdateArticle;
import com.masterproject.ddd.core.api.article.command.ValidateArticle;
import com.masterproject.ddd.core.api.article.event.ArticleClosed;
import com.masterproject.ddd.core.api.article.event.ArticleCreated;
import com.masterproject.ddd.core.api.article.event.ArticleNotValidated;
import com.masterproject.ddd.core.api.article.event.ArticlePublisched;
import com.masterproject.ddd.core.api.article.event.ArticleRejected;
import com.masterproject.ddd.core.api.article.event.ArticleUpdated;
import com.masterproject.ddd.core.api.article.event.ArticleValidated;
import com.masterproject.ddd.core.api.article.model.ArticleStatus;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aggregate
@NoArgsConstructor
@Slf4j
public class ArticleAggregate {

    @AggregateIdentifier
    private String articleID;
    private ArticleStatus status;
    private String note;
    private String authorID;

    @CommandHandler
    public ArticleAggregate(CreateArticle command) {
        log.debug("[Article][Aggregate][Command] Processing create new article command: {}", command);
        AggregateLifecycle.apply(ArticleCreated.builder().Id(command.Id)
                .articleDetails(command.articleDetails)
                .authorID(command.authorID)
                .build());
    }

    @EventSourcingHandler
    public void on(ArticleCreated event) {
        this.articleID = event.Id;
        this.authorID = event.authorID;
        this.status = ArticleStatus.CREATED;
        this.note = "This article has been created and now been verify for publishing";
    }

    @CommandHandler
    public void handle(PublisheArticle command) {
        log.debug("[Article][Aggregate][Command] Processing publish article command: {}", command);

        Assert.isTrue(this.status.equals(ArticleStatus.CLOSED) || this.status.equals(ArticleStatus.CREATED),
                "Can only publish article that is newly created or closed");
        AggregateLifecycle.apply(ArticlePublisched.builder().Id(command.Id).build());
    }

    @EventSourcingHandler
    public void on(ArticlePublisched event) {
        this.status = ArticleStatus.PUBLISHED;
        this.note = "This article has been published";
    }

    @CommandHandler
    public void handle(ValidateArticle command) {

        if (this.status.equals(ArticleStatus.PUBLISHED)) {
            AggregateLifecycle.apply(
                    ArticleValidated.builder().Id(command.Id).referenceID(command.referenceID).build());
        } else {
            AggregateLifecycle.apply(
                    ArticleNotValidated.builder().Id(command.Id).referenceID(command.referenceID).build());
        }
    }

    @CommandHandler
    public void handle(RejectArticle command) {
        log.debug("[Article][Aggregate][Command] Processing reject article command: {}", command);
        Assert.isTrue(this.status.equals(ArticleStatus.CREATED), "Can only reject newly created article");
        AggregateLifecycle.apply(
                ArticleRejected.builder().Id(command.Id).reason(command.reason).build());

    }

    @EventSourcingHandler
    public void on(ArticleRejected event) {
        this.status = ArticleStatus.REJECTED;
        this.note = event.reason;
    }

    @CommandHandler
    public void handle(UpdateArticle command) {
        Assert.isTrue(this.status.equals(ArticleStatus.PUBLISHED), "Can only update published article");
        AggregateLifecycle
                .apply(ArticleUpdated.builder().Id(command.Id).articleDetails(command.articleDetails).build());
    }

    @EventSourcingHandler
    public void on(ArticleUpdated event) {

    }

    @CommandHandler
    public void handle(CloseArticle command) {
        Assert.isTrue(this.status.equals(ArticleStatus.PUBLISHED), "Can not close article that has been published");
        AggregateLifecycle.apply(ArticleClosed.builder().Id(command.Id).build());
    }

    @EventSourcingHandler
    public void on(ArticleClosed event) {
        this.status = ArticleStatus.CLOSED;
        this.note = "This article has been closed";
    }

}
