package com.masterproject.ddd.core.api.user.query;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class GetUserProfileById {

    private final String id;

}
