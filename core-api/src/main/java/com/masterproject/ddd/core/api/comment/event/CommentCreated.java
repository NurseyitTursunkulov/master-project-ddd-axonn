package com.masterproject.ddd.core.api.comment.event;

import com.masterproject.ddd.core.api.core.Event;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class CommentCreated extends Event<String> {

    public final String message;
    public final String articleId;
    public final String authorId;
}
