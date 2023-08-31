package com.masterproject.ddd.core.api.comment.command;

import com.masterproject.ddd.core.api.core.Command;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class RejectComment extends Command<String> {
    public final String reason;
}
