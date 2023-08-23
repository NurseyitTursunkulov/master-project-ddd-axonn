package com.masterproject.ddd.core.api.core;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public abstract class Command<T> {

    @TargetAggregateIdentifier
    public final T Id;

}
