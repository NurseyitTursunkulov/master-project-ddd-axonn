package com.masterproject.ddd.user.service.handler;

import java.util.List;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.masterproject.ddd.core.api.user.event.UserProfileActivated;
import com.masterproject.ddd.core.api.user.event.UserProfileCreated;
import com.masterproject.ddd.core.api.user.event.UserProfileRejected;
import com.masterproject.ddd.core.api.user.event.UserProfileVerified;
import com.masterproject.ddd.core.api.user.model.UserStatus;
import com.masterproject.ddd.core.api.user.query.DoesUserProfileWithUsernameOrEmailExist;
import com.masterproject.ddd.core.api.user.query.GetAllUserProfiles;
import com.masterproject.ddd.core.api.user.query.GetUserProfileById;
import com.masterproject.ddd.core.api.user.query.GetUserProfilesByStatus;
import com.masterproject.ddd.user.service.model.UserProfile;
import com.masterproject.ddd.user.service.repository.UserProfileRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class UserProfileProjection {

    private final UserProfileRepository repository;

    @EventHandler
    public void on(UserProfileCreated event) {
        var userDetails = event.getUserProfileDetails();
        var articleEntity = new UserProfile(event.Id, userDetails.getName(), userDetails.getUserName(),
                userDetails.getEmail(), UserStatus.NEW);
        repository.save(articleEntity);
    }

    @EventHandler
    public void on(UserProfileActivated event) {
        this.setStatusIfExits(event.Id, UserStatus.ACTIVATED);
    }

    @EventHandler
    public void on(UserProfileVerified event) {
        this.setStatusIfExits(event.Id, UserStatus.VERIFIED);
    }

    @EventHandler
    public void on(UserProfileRejected event) {
        // this.setStatusIfExits(event.Id, UserStatus.REJECTED);
        var optional = repository.findById(event.Id);
        if (optional.isPresent()) {
            var userProfile = optional.get();
            repository.delete(userProfile);
        }
    }

    private void setStatusIfExits(String id, UserStatus status) {
        var optional = repository.findById(id);
        if (optional.isPresent()) {
            var userProfile = optional.get();
            userProfile.setStatus(status);
            repository.save(userProfile);
        }
    }

    @QueryHandler
    public List<UserProfile> on(GetAllUserProfiles query) {
        return repository.findAll();
    }

    @QueryHandler
    public UserProfile on(GetUserProfileById query) {
        return repository.findById(query.getId())
                .orElseThrow(() -> new RuntimeException("User profile with Id:" + query.getId() +
                        " does not exits"));
    }

    @QueryHandler
    public List<UserProfile> on(GetUserProfilesByStatus query) {
        return repository.findByStatus(query.getStatus());
    }

    @QueryHandler
    public boolean on(DoesUserProfileWithUsernameOrEmailExist query) {
        var userWithSameUsernames = repository.findByUsername(query.getUserName());
        var userWithSameEmail = repository.findByEmail(query.getEmail());

        var optionalUsername = userWithSameUsernames.stream().filter(user -> user.getStatus().equals(UserStatus.ACTIVATED)).findAny();
        var optionalEmail = userWithSameEmail.stream().filter(user -> user.getStatus().equals(UserStatus.ACTIVATED)).findAny();

        if (optionalUsername.isPresent() || optionalEmail.isPresent()) {
            return true;
        }
        return false;
    }

}
