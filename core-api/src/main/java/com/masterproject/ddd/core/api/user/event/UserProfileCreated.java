package com.masterproject.ddd.core.api.user.event;

import com.masterproject.ddd.core.api.core.Event;
import com.masterproject.ddd.core.api.user.model.ProfileDetails;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class UserProfileCreated extends Event<String> {

    private final ProfileDetails userProfileDetails;

}
