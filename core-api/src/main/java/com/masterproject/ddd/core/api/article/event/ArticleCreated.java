package com.masterproject.ddd.core.api.article.event;

import com.masterproject.ddd.core.api.article.model.ArticleDetails;
import com.masterproject.ddd.core.api.core.Event;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class ArticleCreated extends Event<String> {

    public final String authorID;
    public final ArticleDetails articleDetails;

}
