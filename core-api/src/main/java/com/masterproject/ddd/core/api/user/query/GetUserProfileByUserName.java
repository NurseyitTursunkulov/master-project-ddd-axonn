package com.masterproject.ddd.core.api.user.query;

import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class GetUserProfileByUserName {

    @NotEmpty(message = "Username can not be empty")
    private final String userName;

}
