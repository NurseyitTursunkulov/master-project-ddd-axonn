package com.masterproject.ddd.core.api.core;

import java.time.Instant;

public enum TimeUtils {
    INSTANCE;

    public com.google.protobuf.Timestamp convertToGRPCModelType(Instant time) {
        return com.google.protobuf.Timestamp.newBuilder().setSeconds(time.getEpochSecond()).setNanos(time.getNano())
                .build();
    }

    public Instant convertToCoreAPIModelType(com.google.protobuf.Timestamp time) {
        return Instant.ofEpochSecond(time.getSeconds(), time.getNanos());
    }

}
