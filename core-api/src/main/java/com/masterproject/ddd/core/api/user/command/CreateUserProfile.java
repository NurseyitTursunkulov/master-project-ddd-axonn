package com.masterproject.ddd.core.api.user.command;

import javax.validation.Valid;

import com.masterproject.ddd.core.api.core.Command;
import com.masterproject.ddd.core.api.user.model.ProfileDetails;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class CreateUserProfile extends Command<String> {

    @Valid
    private final ProfileDetails userProfileDetails;

}
