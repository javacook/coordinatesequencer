package com.javacook.coordinate.sequencer;

/**
 * Created by vollmer on 27.08.16.
 */
public interface ArrayPredicateAndCounter<T> {

    boolean test(T[] values, int counter);

}
