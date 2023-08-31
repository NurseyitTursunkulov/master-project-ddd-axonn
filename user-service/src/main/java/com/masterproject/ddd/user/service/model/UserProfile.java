package com.masterproject.ddd.user.service.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.masterproject.ddd.core.api.user.model.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    private String id;
    private String name;
    private String username;
    private String email;
    private UserStatus status;
    private @NotNull Instant registeredOn = Instant.now();

}
