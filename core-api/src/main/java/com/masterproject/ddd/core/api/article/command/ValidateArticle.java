package com.masterproject.ddd.core.api.article.command;

import com.masterproject.ddd.core.api.core.Command;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class ValidateArticle extends Command<String> {
    public final String referenceID;
}