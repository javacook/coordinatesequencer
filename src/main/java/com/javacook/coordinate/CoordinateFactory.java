package com.javacook.coordinate;

/**
 * Created by vollmer on 06.09.16.
 */
@FunctionalInterface
public interface CoordinateFactory<T> {
    T create(int x, int y);
}
