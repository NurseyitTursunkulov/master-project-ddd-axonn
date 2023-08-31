package com.masterproject.ddd.core.api.article.query;

import com.masterproject.ddd.core.api.article.model.ArticleStatus;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class GetArticlesByStatus {

    private final ArticleStatus status;
}
