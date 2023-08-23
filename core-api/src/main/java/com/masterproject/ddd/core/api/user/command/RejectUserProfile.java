package com.masterproject.ddd.core.api.user.command;

import com.masterproject.ddd.core.api.core.Command;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class RejectUserProfile extends Command<String> {
    public final String reason;
}
