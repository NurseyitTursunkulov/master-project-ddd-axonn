package com.masterproject.ddd.core.api.comment.utils;

import com.masterproject.ddd.core.api.comment.model.CommentStatus;
import com.masterproject.ddd.grpc.api.comment.CMStatus;

public enum CommentModelMapper {
    INSTANCE;

    public CommentStatus convertToCoreAPIModelType(CMStatus status) {
        switch (status) {
            case CM_CREATED:
                return CommentStatus.CREATED;
            case CM_PUBLISHED:
                return CommentStatus.PUBLISHED;
            case CM_REJECTED:
                return CommentStatus.REJECTED;
            case CM_REMOVED:
                return CommentStatus.REMOVED;
            default:
                return CommentStatus.CREATED;
        }
    }

    public CMStatus convertToGRPCModelType(CommentStatus status) {
        switch (status) {
            case CREATED:
                return CMStatus.CM_CREATED;
            case PUBLISHED:
                return CMStatus.CM_PUBLISHED;
            case REJECTED:
                return CMStatus.CM_REJECTED;
            case REMOVED:
                return CMStatus.CM_REMOVED;
            default:
                return CMStatus.CM_CREATED;
        }
    }
}
