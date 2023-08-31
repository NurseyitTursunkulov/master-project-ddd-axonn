package com.masterproject.ddd.core.api.article.utils;

import com.masterproject.ddd.core.api.article.model.ArticleDetails;
import com.masterproject.ddd.core.api.article.model.ArticleStatus;
import com.masterproject.ddd.grpc.api.article.ArtStatus;
import com.masterproject.ddd.grpc.api.article.CreateArticleDetails;

public enum ArticleModelMapper {
    INSTANCE;

    public ArticleDetails convertToCoreAPIModelType(CreateArticleDetails details) {
        return new ArticleDetails(details.getTitle(), details.getDescription(), details.getBody());
    }

    public ArticleStatus convertToCoreAPIModelType(ArtStatus status) {
        switch (status) {
            case Art_CREATED:
                return ArticleStatus.CREATED;
            case Art_CLOSED:
                return ArticleStatus.CLOSED;
            case Art_PUBLISHED:
                return ArticleStatus.PUBLISHED;
            case Art_REJECTED:
                return ArticleStatus.REJECTED;
            default:
                return ArticleStatus.CREATED;
        }
    }

    public ArtStatus convertToGRPCModelType(ArticleStatus status) {
        switch (status) {
            case CREATED:
                return ArtStatus.Art_CREATED;
            case CLOSED:
                return ArtStatus.Art_CLOSED;
            case PUBLISHED:
                return ArtStatus.Art_PUBLISHED;
            case REJECTED:
                return ArtStatus.Art_REJECTED;
            default:
                return ArtStatus.Art_CREATED;
        }
    }

}
