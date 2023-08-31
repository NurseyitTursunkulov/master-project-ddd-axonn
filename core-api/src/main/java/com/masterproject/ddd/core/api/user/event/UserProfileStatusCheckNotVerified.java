package com.masterproject.ddd.core.api.user.event;

import com.masterproject.ddd.core.api.core.Event;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class UserProfileStatusCheckNotVerified extends Event<String> {
    public final String referenceID;
}
