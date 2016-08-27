package com.javacook.coordinate.sequencer;

/**
 * Created by vollmer on 26.08.16.
 */
@FunctionalInterface
public interface PairPredicateAndCounter<T> {

        boolean test(T t1, T t2, int counter);
}
