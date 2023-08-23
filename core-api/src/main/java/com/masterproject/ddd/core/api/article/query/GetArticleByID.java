package com.masterproject.ddd.core.api.article.query;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class GetArticleByID {

    @NonNull
    private final String Id;

}
