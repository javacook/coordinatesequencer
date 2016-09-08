package com.javacook.coordinate.sequencer;

import com.javacook.coordinate.CoordinateFactory;
import com.javacook.coordinate.CoordinateInterface;

import java.util.Iterator;

/**
 * Created by vollmer on 20.08.16.
 */
public class CoordinateSequence<T extends CoordinateInterface> implements Iterable<T> {

    protected int fromX;
    protected int fromY;
    protected int toX;
    protected int toY;
    protected int stepX = 1;
    protected int stepY = 1;
    protected CoordinateFactory<T> coordinateFactory;


    public CoordinateSequence(int fromX, int fromY, int toX, int toY, int stepX, int stepY,
                              CoordinateFactory<T> coordinateFactory) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.stepX = stepX;
        this.stepY = stepY;
        this.coordinateFactory = coordinateFactory;
    }

    CoordinateSequence(int fromX, int fromY, int toX, int toY, CoordinateFactory<T> coordinateFactory) {
        this(fromX, fromY, toX, toY, 1, 1, coordinateFactory);
    }


    @Override
    public Iterator<T> iterator() {
        return new CoordinateIterator();
    }

    public class CoordinateIterator implements Iterator<T> {
        int x = fromX, y = fromY;

        @Override
        public boolean hasNext() {
            return (stepY > 0)? (y < toY) : (y > toY);
        }

        @Override
        public T next() {
            if (hasNext()) {
                T result = coordinateFactory.create(x, y);
                x += stepX;
                if ((stepX > 0)? (x >= toX) : (x <= toX)) {
                    x = fromX;
                    y += stepY;
                }
                return result;
            }
            return null;
        }
    }


    @Override
    public String toString() {
        return "CoordinateSequence{" +
                "fromX=" + fromX + ", fromY=" + fromY + ", toX=" + toX + ", toY=" + toY + '}';
    }


}
