package com.masterproject.ddd.article.management.sagas;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.masterproject.ddd.core.api.article.command.PublisheArticle;
import com.masterproject.ddd.core.api.article.command.RejectArticle;
import com.masterproject.ddd.core.api.article.event.ArticleCreated;
import com.masterproject.ddd.core.api.article.event.ArticlePublisched;
import com.masterproject.ddd.core.api.article.event.ArticleRejected;
import com.masterproject.ddd.core.api.user.command.CheckUserProfileStatus;
import com.masterproject.ddd.core.api.user.event.UserProfileStatusCheckNotVerified;
import com.masterproject.ddd.core.api.user.event.UserProfileStatusCheckVerified;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Saga
@NoArgsConstructor
@Slf4j
public class ArticleCreationSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "Id")
    public void on(ArticleCreated event) {

        SagaLifecycle.associateWith("referenceID", event.Id);

        try {
            // Verify if user is allow to post an article.
            // TODO remove this (Just for simulation purpose)
            Thread.sleep(20000);
            log.info("[ArticleSaga] Verify if the author is a verified user.");
            commandGateway
                    .send(CheckUserProfileStatus.builder().Id(event.authorID)
                            .referenceID(event.Id).build());
        } catch (InterruptedException e) {
        }
        // commandGateway
        // .send(VerifyArticleAuthorCommand.builder().Id(event.authorID)
        // .articleID(event.Id).build());
    }

    @SagaEventHandler(associationProperty = "referenceID")
    public void on(UserProfileStatusCheckVerified event) {
        // publish article if the user is a verified user
        log.info("[ArticleSaga] Author is a verified user article can now be published.");
        commandGateway.send(PublisheArticle.builder().Id(event.referenceID).build());
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "Id", keyName = "referenceID")
    public void on(ArticlePublisched event) {
        log.info("[ArticleSaga] Article with id {} has been published", event.Id);
    }

    @SagaEventHandler(associationProperty = "referenceID")
    public void on(UserProfileStatusCheckNotVerified event) {
        var reason = String.format("[ArticleSaga] Author of article with id: %s is not verified.",
                event.Id);
        log.info(reason);
        log.info("[ArticleSaga] Article will be rejected.");
        // reject the article if the user is not a verified user
        commandGateway.send(RejectArticle.builder().Id(event.referenceID).reason(reason).build());
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "Id", keyName = "referenceID")
    public void on(ArticleRejected event) {
        log.info("[ArticleSaga] Article with id {} has been rejected with following reasons {}", event.Id,
                event.reason);
    }

}
