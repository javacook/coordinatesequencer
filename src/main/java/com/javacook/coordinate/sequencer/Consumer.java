package com.javacook.coordinate.sequencer;

/**
 * Created by vollmer on 25.08.16.
 */
@FunctionalInterface
public interface Consumer<T> {

    void apply(T values);
}
