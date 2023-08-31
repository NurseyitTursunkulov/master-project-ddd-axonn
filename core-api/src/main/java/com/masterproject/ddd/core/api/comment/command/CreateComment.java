package com.masterproject.ddd.core.api.comment.command;

import com.masterproject.ddd.core.api.core.Command;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class CreateComment extends Command<String> {

    public final String message;
    public final String articleId;
    public final String authorId;
}
