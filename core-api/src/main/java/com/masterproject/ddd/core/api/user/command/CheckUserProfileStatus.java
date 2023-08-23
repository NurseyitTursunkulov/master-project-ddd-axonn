package com.masterproject.ddd.core.api.user.command;

import com.masterproject.ddd.core.api.core.Command;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class CheckUserProfileStatus extends Command<String> {

    private final String articleID;

}
