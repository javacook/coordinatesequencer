package com.javacook.coordinate;

import java.util.Iterator;

/**
 * Created by vollmer on 20.08.16.
 */
public class CoordinateSequence<T extends CoordinateInterface> implements Iterable<T> {

    private int fromX;
    private int fromY;
    private int toX;
    private int toY;
    private int stepX = 1;
    private int stepY = 1;
    private CoordinateFactory<T> coordinateFactory;



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
                "fromX=" + fromX +
                ", fromY=" + fromY +
                ", toX=" + toX +
                ", toY=" + toY +
                '}';
    }

    public static void main(String[] args) {
        CoordinateSequence<Coordinate> sequence = new CoordinateSequence(5, 6, 7, 8, Coordinate::new);
        sequence.forEach(t -> System.out.println(t.x()));

        new CoordinateSequence<>(5, 6, 7, 8, Coordinate::new).forEach(t -> System.out.println(t.x()));


        CoordinateSequence<CoordinateInterface> sequence2 = new CoordinateSequence(5, 6, 7, 8, CoordinateInterface::create);
        sequence2.forEach(t -> System.out.println(t.x() + "," + t.y()));

    }
}
