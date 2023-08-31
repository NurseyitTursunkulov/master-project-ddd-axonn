package com.masterproject.ddd.core.api.article.command;

import com.masterproject.ddd.core.api.article.model.ArticleDetails;
import com.masterproject.ddd.core.api.core.Command;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class UpdateArticle extends Command<String> {
    public final ArticleDetails articleDetails;
}
