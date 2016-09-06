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
                return x;
            }

            @Override
            public int y() {
                return y;
            }

            @Override
            public String toString() {
                return "(" + x + ", " + y + ")";
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || !(o instanceof CoordinateInterface)) return false;

                CoordinateInterface that = (CoordinateInterface) o;
                return x == that.x() && y == that.y();
            }

            @Override
            public int hashCode() {
                return 31 * x + y;
            }

        };
    }

}
