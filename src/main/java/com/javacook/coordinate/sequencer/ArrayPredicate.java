package com.javacook.coordinate.sequencer;

/**
 * Created by vollmer on 27.08.16.
 */
@FunctionalInterface
public interface ArrayPredicate<T> {

    boolean test(T[] values);

}
