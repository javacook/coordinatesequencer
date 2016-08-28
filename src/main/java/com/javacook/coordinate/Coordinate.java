package com.javacook.coordinate;

/**
 * Stores 2D coordinates
 */
public class Coordinate {

    public final int x, y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate decX() {
        return addX(-1);
    }

    public Coordinate decY() {
        return addY(-1);
    }

    public Coordinate incX() {
        return addX(1);
    }

    public Coordinate incY() {
        return addY(1);
    }

    public Coordinate addX(int offset) {
        return new Coordinate(x + offset, y);
    }

    public Coordinate addY(int offset) {
        return new Coordinate(x, y + offset);
    }

    public Coordinate add(Coordinate coord) {
        return new Coordinate(x + coord.x, y + coord.y);
    }

    public Coordinate setX(int xNew) {
        return new Coordinate(xNew, y);
    }

    public Coordinate setY(int yNew) {
        return new Coordinate(x, yNew);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
