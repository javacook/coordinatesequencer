package com.javacook.coordinate.sequencer;

/**
 * Created by vollmer on 26.08.16.
 */
@FunctionalInterface
public interface Predicate<T> {

        boolean test(T t);
}
