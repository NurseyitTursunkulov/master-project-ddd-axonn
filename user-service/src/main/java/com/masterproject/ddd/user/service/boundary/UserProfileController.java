package com.masterproject.ddd.user.service.boundary;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masterproject.ddd.core.api.user.command.CreateUserProfile;
import com.masterproject.ddd.core.api.user.command.VerifyUserProfile;
import com.masterproject.ddd.core.api.user.model.ProfileDetails;
import com.masterproject.ddd.core.api.user.model.UserStatus;
import com.masterproject.ddd.core.api.user.query.GetAllUserProfiles;
import com.masterproject.ddd.core.api.user.query.GetUserProfileById;
import com.masterproject.ddd.core.api.user.query.GetUserProfilesByStatus;
import com.masterproject.ddd.user.service.model.UserProfile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("api/profiles")
@Tag(name = "UserProfile Controller", description = "Provides user profile details")
public class UserProfileController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @PostMapping("/")
    @Operation(summary = "Create new user profile Command.", description = "create a new user profile with the provided details")
    public CompletableFuture<String> createUserProfile(@Valid @RequestBody ProfileDetails details) {
        UUID newId = UUID.randomUUID();
        return commandGateway
                .send(CreateUserProfile.builder().Id(newId.toString()).userProfileDetails(details).build())
                .thenCompose((s) -> CompletableFuture.supplyAsync(() -> newId.toString()));
    }

    @PostMapping("/verify/{profileId:.+}")
    @Operation(summary = "User profile verification Command.", description = "User send this command to verify them self.")
    public CompletableFuture<String> verifyProfile(@PathVariable @NotEmpty final String profileId) {
        return commandGateway
                .send(VerifyUserProfile.builder().Id(profileId).build())
                .thenCompose((s) -> CompletableFuture.supplyAsync(() -> "User Verification Process stated."));
    }

    @GetMapping("/")
    @Operation(summary = "Query to get all user profiles", description = "Get all system registered users")
    public CompletableFuture<List<UserProfile>> getUserProfiles() {
        return queryGateway.query(GetAllUserProfiles.builder().build(),
                ResponseTypes.multipleInstancesOf(UserProfile.class));
    }

    @GetMapping("/{profileId:.+}")
    @Operation(summary = "Query to get a particular user given an ID", description = "Get a system regisred user by ID")
    public CompletableFuture<UserProfile> getUserProfileById(@PathVariable @NotEmpty final String profileId) {
        return queryGateway.query(GetUserProfileById.builder().id(profileId).build(),
                ResponseTypes.instanceOf(UserProfile.class));
    }

    @GetMapping("/status/{status:.+}")
    @Operation(summary = "Query to all user by a specific status", description = "Get all system registered users having the provided status")
    public CompletableFuture<List<UserProfile>> getUserProfilesByStatus(
            @PathVariable @NotEmpty final UserStatus status) {
        return queryGateway.query(GetUserProfilesByStatus.builder().status(status).build(),
                ResponseTypes.multipleInstancesOf(UserProfile.class));
    }

}
