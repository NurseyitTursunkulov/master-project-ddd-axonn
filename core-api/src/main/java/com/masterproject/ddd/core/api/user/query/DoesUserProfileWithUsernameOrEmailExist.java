package com.masterproject.ddd.core.api.user.query;

import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DoesUserProfileWithUsernameOrEmailExist {
    @NotEmpty(message = "Username can not be empty")
    private final String userName;
    @NotEmpty(message = "Username can not be empty")
    private final String email;
}
