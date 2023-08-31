package com.masterproject.ddd.article.management.sagas;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.masterproject.ddd.core.api.article.command.ValidateArticle;
import com.masterproject.ddd.core.api.article.event.ArticleNotValidated;
import com.masterproject.ddd.core.api.article.event.ArticleValidated;
import com.masterproject.ddd.core.api.comment.command.PublishComment;
import com.masterproject.ddd.core.api.comment.command.RejectComment;
import com.masterproject.ddd.core.api.comment.event.CommentCreated;
import com.masterproject.ddd.core.api.comment.event.CommentPublished;
import com.masterproject.ddd.core.api.comment.event.CommentRejected;
import com.masterproject.ddd.core.api.user.command.CheckUserProfileStatus;
import com.masterproject.ddd.core.api.user.event.UserProfileStatusCheckNotVerified;
import com.masterproject.ddd.core.api.user.event.UserProfileStatusCheckVerified;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Saga
@NoArgsConstructor
@Slf4j
public class CommentCreationSaga {

    @Autowired
    private transient CommandGateway commandGateway;
    private String articleID;

    @StartSaga
    @SagaEventHandler(associationProperty = "Id")
    public void on(CommentCreated event) {
        this.articleID = event.articleId;
        SagaLifecycle.associateWith("referenceID", event.Id);
        log.info("[CommentSaga] Verify if the author is a verified user.");
        commandGateway
                .send(CheckUserProfileStatus.builder().Id(event.authorId)
                        .referenceID(event.Id).build());

    }

    @SagaEventHandler(associationProperty = "referenceID")
    public void on(UserProfileStatusCheckVerified event) {
        log.info("[CommentSaga] Author is a verified user check if article is published");
        commandGateway.send(ValidateArticle.builder().Id(this.articleID).referenceID(event.referenceID).build());
    }

    @SagaEventHandler(associationProperty = "referenceID")
    public void on(ArticleValidated event) {
        log.info("[CommentSaga] Article is Valid");
        commandGateway.send(PublishComment.builder().Id(event.referenceID).build());
    }

    @SagaEventHandler(associationProperty = "referenceID")
    public void on(ArticleNotValidated event) {
        var reason = String.format("[CommentSaga] Article with id: %s is not Valid.",
                event.Id);
        log.info(reason);
        log.info("[CommentSaga] Comment will be rejected.");
        commandGateway.send(RejectComment.builder().Id(event.referenceID).reason(reason).build());
    }

    @SagaEventHandler(associationProperty = "referenceID")
    public void on(UserProfileStatusCheckNotVerified event) {
        var reason = String.format("[CommentSaga] Author of comment with id: %s is not verified.",
                event.Id);
        log.info(reason);
        log.info("[CommentSaga] Comment will be rejected.");
        commandGateway.send(RejectComment.builder().Id(event.referenceID).reason(reason).build());
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "Id", keyName = "referenceID")
    public void on(CommentRejected event) {
        log.info("Comment with Id: " + event.Id + " has been rejected with the following reasons: " + event.reason);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "Id", keyName = "referenceID")
    public void on(CommentPublished event) {
        log.info("Comment with Id: " + event.Id + " is now published.");
    }

}
