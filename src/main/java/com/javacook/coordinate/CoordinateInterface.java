package com.javacook.coordinate;

/**
 * Created by vollmer on 30.08.16.
 */
public interface CoordinateInterface {

    int x();
    int y();

    static CoordinateInterface create(final int argX, final int argY) {
        return new CoordinateInterface() {

            final int x = argX;
            final int y = argY;

            @Override
            public int x() {
                return 0;
            }

            @Override
            public int y() {
                return 0;
            }
        };
    }

    public static void main(String[] args) {
        CoordinateInterface ci = CoordinateInterface.create(4, 5);
        System.out.println(ci.x() + ", " + ci.y());
    }

}
