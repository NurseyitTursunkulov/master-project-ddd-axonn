package com.masterproject.ddd.core.api.user.query;

import com.masterproject.ddd.core.api.user.model.UserStatus;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class GetUserProfilesByStatus {

    @Builder.Default
    private final UserStatus status = UserStatus.VERIFIED;

}
