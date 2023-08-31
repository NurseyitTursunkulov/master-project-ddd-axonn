package com.masterproject.ddd.core.api.article.event;

import com.masterproject.ddd.core.api.core.Event;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class ArticleValidated extends Event<String> {
    public final String referenceID;
}
