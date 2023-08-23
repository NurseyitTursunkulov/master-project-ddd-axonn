package com.masterproject.ddd.user.service.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.util.Assert;

import com.masterproject.ddd.core.api.user.command.ActivateUserProfile;
import com.masterproject.ddd.core.api.user.command.CheckUserProfileStatus;
import com.masterproject.ddd.core.api.user.command.CreateUserProfile;
import com.masterproject.ddd.core.api.user.command.RejectUserProfile;
import com.masterproject.ddd.core.api.user.command.VerifyUserProfile;
import com.masterproject.ddd.core.api.user.event.UserProfileActivated;
import com.masterproject.ddd.core.api.user.event.UserProfileCreated;
import com.masterproject.ddd.core.api.user.event.UserProfileRejected;
import com.masterproject.ddd.core.api.user.event.UserProfileStatusCheckNotVerified;
import com.masterproject.ddd.core.api.user.event.UserProfileStatusCheckVerified;
import com.masterproject.ddd.core.api.user.event.UserProfileVerified;
import com.masterproject.ddd.core.api.user.model.UserStatus;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aggregate
@NoArgsConstructor
@Slf4j
public class UserProfileAggregate {

    @AggregateIdentifier
    private String userId;

    private String name;
    private String email;
    private UserStatus status;
    private String note;

    @CommandHandler
    public UserProfileAggregate(CreateUserProfile command) {

        log.debug("[User][Aggregate][Command] Processing create new user profile command: {}", command);
        AggregateLifecycle.apply(UserProfileCreated.builder().Id(command.Id)
                .userProfileDetails(command.getUserProfileDetails()).build());

    }

    @EventSourcingHandler
    public void on(UserProfileCreated event) {
        this.userId = event.Id;
        this.name = event.getUserProfileDetails().getName();
        this.email = event.getUserProfileDetails().getEmail();
        this.status = UserStatus.NEW;
    }

    @CommandHandler
    public void handle(ActivateUserProfile command) {
        Assert.isTrue(this.status.equals(UserStatus.NEW) || this.status.equals(UserStatus.DEACTIVATED),
                "Can only activate a newly or deactivated user profiles ");
        AggregateLifecycle.apply(UserProfileActivated.builder().Id(command.Id).build());
    }

    @EventSourcingHandler
    public void on(UserProfileActivated event) {
        this.status = UserStatus.ACTIVATED;
    }

    @CommandHandler
    public void handle(VerifyUserProfile command) {
        Assert.isTrue(this.status.equals(UserStatus.ACTIVATED), "Can only verify an active user profile.");
        AggregateLifecycle.apply(UserProfileVerified.builder().Id(command.Id).build());
    }

    @EventSourcingHandler
    public void on(UserProfileVerified event) {
        this.status = UserStatus.VERIFIED;
    }

    @CommandHandler
    public void handle(RejectUserProfile command) {

        Assert.isTrue(this.status.equals(UserStatus.NEW), "Can only reject a newly created user profile");
        AggregateLifecycle.apply(UserProfileRejected.builder().Id(command.Id).reason(command.reason).build());
    }

    @EventSourcingHandler
    public void on(UserProfileRejected event) {
        this.status = UserStatus.REJECTED;
        this.note = event.reason;
    }

    @CommandHandler
    public void handle(CheckUserProfileStatus command) {
        log.info("[User][Aggregate][Command] Processing verify ");
        if (this.status.equals(UserStatus.VERIFIED)) {
            AggregateLifecycle.apply(
                    UserProfileStatusCheckVerified.builder().Id(command.Id).articleID(command.getArticleID()).build());
        } else {
            AggregateLifecycle.apply(
                    UserProfileStatusCheckNotVerified.builder().Id(command.Id).articleID(command.getArticleID())
                            .build());
        }
    }

}
