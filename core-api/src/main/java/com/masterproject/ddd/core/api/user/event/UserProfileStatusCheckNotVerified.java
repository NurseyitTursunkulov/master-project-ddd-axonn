package com.masterproject.ddd.core.api.user.event;

import com.masterproject.ddd.core.api.core.Event;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class UserProfileStatusCheckNotVerified extends Event<String> {
    private final String articleID;
}
