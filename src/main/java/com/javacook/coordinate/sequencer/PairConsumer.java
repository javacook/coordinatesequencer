package com.javacook.coordinate.sequencer;

/**
 * Created by vollmer on 25.08.16.
 */
@FunctionalInterface
public interface PairConsumer<T> {

    void apply(T value, T value2);
}
