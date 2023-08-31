package com.masterproject.ddd.comment.service.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.util.Assert;

import com.masterproject.ddd.core.api.comment.command.CreateComment;
import com.masterproject.ddd.core.api.comment.command.PublishComment;
import com.masterproject.ddd.core.api.comment.command.RejectComment;
import com.masterproject.ddd.core.api.comment.command.RemoveComment;
import com.masterproject.ddd.core.api.comment.event.CommentCreated;
import com.masterproject.ddd.core.api.comment.event.CommentPublished;
import com.masterproject.ddd.core.api.comment.event.CommentRejected;
import com.masterproject.ddd.core.api.comment.event.CommentRemoved;
import com.masterproject.ddd.core.api.comment.model.CommentStatus;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aggregate
@NoArgsConstructor
@Slf4j
public class CommentAggregate {

    @AggregateIdentifier
    private String commentID;
    private String articleID;
    private String authorID;
    private String note;

    private CommentStatus status;

    @CommandHandler
    public CommentAggregate(CreateComment command) {
        log.debug("[Comment][Aggregate][Command] Processing create new article command: {}", command);
        AggregateLifecycle.apply(CommentCreated.builder().Id(command.Id)
                .message(command.message)
                .articleId(command.articleId)
                .authorId(command.authorId)
                .build());
    }

    @EventSourcingHandler
    public void on(CommentCreated event) {
        this.commentID = event.Id;
        this.articleID = event.articleId;
        this.authorID = event.authorId;
        this.status = CommentStatus.CREATED;
        this.note = "Comment has been created and will now be verify";
    }

    @CommandHandler
    public void handle(PublishComment command) {
        Assert.isTrue(this.status.equals(CommentStatus.CREATED), "Can only published newly created comments");
        AggregateLifecycle.apply(CommentPublished.builder().Id(command.Id).build());
    }

    @EventSourcingHandler
    public void on(CommentPublished event) {
        this.status = CommentStatus.PUBLISHED;
        this.note = "Comment has now been published";
    }

    @CommandHandler
    public void handle(RejectComment command) {
        Assert.isTrue(this.status.equals(CommentStatus.CREATED), "Can only reject newly created comments");
        AggregateLifecycle.apply(CommentRejected.builder().Id(command.Id).reason(command.reason).build());
    }

    @EventSourcingHandler
    public void on(CommentRejected event) {
        this.status = CommentStatus.REJECTED;
        this.note = event.reason;
    }

    @CommandHandler
    public void handle(RemoveComment command) {
        Assert.isTrue(this.status.equals(CommentStatus.PUBLISHED), "Can only remove published comments");
        AggregateLifecycle.apply(CommentRemoved.builder().Id(command.Id).build());
    }

    @EventSourcingHandler
    public void on(CommentRemoved event) {
        this.status = CommentStatus.REMOVED;
        this.note = "Comment has now been removed";
    }

}
