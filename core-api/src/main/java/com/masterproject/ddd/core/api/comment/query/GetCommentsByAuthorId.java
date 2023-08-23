package com.masterproject.ddd.core.api.comment.query;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class GetCommentsByAuthorId {

    @NonNull
    private String authorId;

}
