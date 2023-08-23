package com.masterproject.ddd.core.api.core;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public abstract class Event<T> {
    public final T Id;
}
