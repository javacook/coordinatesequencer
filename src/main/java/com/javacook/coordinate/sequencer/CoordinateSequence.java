package com.javacook.coordinate.sequencer;


import com.javacook.coordinate.Coordinate;

import java.util.Iterator;

/**
 * Created by vollmer on 20.08.16.
 */
public class CoordinateSequence implements Iterable<Coordinate>{

    private int fromX;
    private int fromY;
    private int toX;
    private int toY;
    private int stepX = 1;
    private int stepY = 1;

    CoordinateSequence(int fromX, int fromY, int toX, int toY, int stepX, int stepY) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.stepX = stepX;
        this.stepY = stepY;
    }

    CoordinateSequence(int fromX, int fromY, int toX, int toY) {
        this(fromX, fromY, toX, toY, 1, 1);
    }

    @Override
    public Iterator<Coordinate> iterator() {
        return new CoordinateIterator();
    }

    public class CoordinateIterator implements Iterator<Coordinate> {
        int x = fromX, y = fromY;

        @Override
        public boolean hasNext() {
            return (stepY > 0)? (y < toY) : (y > toY);
        }

        @Override
        public Coordinate next() {
            if (hasNext()) {
                Coordinate result = new Coordinate(x, y);
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

        CoordinateSequence sequence = new CoordinateSequence(5, 6, 7, 8);
        for (Coordinate coordinate : sequence) {
            System.out.println(coordinate);
        }
        sequence.forEach(t -> System.out.println(t));
    }
}
