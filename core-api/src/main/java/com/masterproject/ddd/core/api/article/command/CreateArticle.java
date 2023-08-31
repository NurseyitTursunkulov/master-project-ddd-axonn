package com.masterproject.ddd.core.api.article.command;

import com.masterproject.ddd.core.api.article.model.ArticleDetails;
import com.masterproject.ddd.core.api.core.Command;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class CreateArticle extends Command<String> {

    public final String authorID;
    public final ArticleDetails articleDetails;
}
