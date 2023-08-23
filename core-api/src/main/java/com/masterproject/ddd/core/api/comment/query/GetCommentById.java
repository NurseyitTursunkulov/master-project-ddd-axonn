package com.masterproject.ddd.core.api.comment.query;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class GetCommentById {

    @NonNull
    private String id;
}
