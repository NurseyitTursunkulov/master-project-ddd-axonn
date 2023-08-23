package com.masterproject.ddd.core.api.article.model;

import javax.validation.constraints.NotBlank;

import lombok.Value;

@Value
public class ArticleDetails {
    @NotBlank(message = "An article muss have a titel")
    private final String title;
    private final String description;
    @NotBlank(message = "An article muss have a body")
    private final String body;
}
