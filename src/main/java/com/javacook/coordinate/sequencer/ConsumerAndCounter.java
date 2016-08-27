package com.javacook.coordinate.sequencer;

/**
 * Created by vollmer on 25.08.16.
 */
@FunctionalInterface
public interface ConsumerAndCounter<T> {

    void apply(T values, int counter);
}
