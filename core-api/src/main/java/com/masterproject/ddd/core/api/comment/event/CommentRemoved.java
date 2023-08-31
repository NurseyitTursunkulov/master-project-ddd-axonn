package com.masterproject.ddd.core.api.comment.event;

import com.masterproject.ddd.core.api.core.Event;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class CommentRemoved extends Event<String> {

}
