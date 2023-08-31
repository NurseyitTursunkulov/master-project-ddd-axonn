package com.masterproject.ddd.article.management.sagas;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.masterproject.ddd.core.api.user.command.ActivateUserProfile;
import com.masterproject.ddd.core.api.user.command.RejectUserProfile;
import com.masterproject.ddd.core.api.user.event.UserProfileActivated;
import com.masterproject.ddd.core.api.user.event.UserProfileCreated;
import com.masterproject.ddd.core.api.user.event.UserProfileRejected;
import com.masterproject.ddd.core.api.user.query.DoesUserProfileWithUsernameOrEmailExist;

import lombok.extern.slf4j.Slf4j;

@Saga
@Slf4j
public class UserProfileCreationSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "Id")
    public void on(UserProfileCreated event) {
        // check if there is a user with the provided username or email
        System.out.println("In Saga");
        queryGateway.query(
                DoesUserProfileWithUsernameOrEmailExist.builder().userName(event.getUserProfileDetails().getUserName())
                        .email(event.getUserProfileDetails().getEmail()).build(),
                ResponseTypes.instanceOf(Boolean.class)).thenAccept(result -> {
                    if (result) {
                        // Reject account creation with that username
                        log.info("User Exits");
                        try {
                            // TODO remove this (Just for simulation purpose)
                            Thread.sleep(5000);
                            commandGateway.send(RejectUserProfile.builder().Id(event.Id)
                                    .reason("A user with the provided username already exists.").build());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // commandGateway.send(RejectUserProfile.builder().Id(event.Id)
                        //             .reason("A user with the provided username already exists.").build());
                    } else {

                        try {
                            // TODO remove this (Just for simulation purpose)
                            Thread.sleep(5000);
                            commandGateway.send(ActivateUserProfile.builder().Id(event.Id).build());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "Id")
    public void on(UserProfileRejected event) {
        log.info("User could not be activated due to username conflit. try using a different username.");
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "Id")
    public void on(UserProfileActivated event) {
        log.info("User have been activated");
    }

}
