package com.masterproject.ddd.core.api.user.event;

import com.masterproject.ddd.core.api.core.Event;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class UserProfileRejected extends Event<String> {
    public final String reason;
}
