package com.javacook.coordinate.sequencer;

import com.javacook.coordinate.Coordinate;
import com.javacook.coordinate.CoordinateInterface;

public class CoordinateSequencerMain {


    public static void main(String[] args) {

        new CoordinateSequencer<>(Coordinate::new).forX(5).forY(9)
                .forEachCoordinate(System.out::println);

        System.out.println("--------------");

        CoordinateSequencer<Coordinate> coordinateSequencer = new CoordinateSequencer<>(Coordinate::new);

        coordinateSequencer
                .fromX(1).lenX(14).forY(4)
                .stopWhenCoordinate(coord -> coord.x() > 10)
                .forEachCoordinate(coord -> System.out.println(coord));

        System.out.println("--------------");

        new CoordinateSequencer(Coordinate::new)
                .fromX(1).lenX(4).forY(4).enter()
                .fromX(2).lenX(4).forY(9)
                .forEachCoordinateArray(coords -> System.out.println(coords[0] + ", " + coords[1]));

        System.out.println("--------------");

        new CoordinateSequencer<>(Coordinate::new)
                .fromX(1).lenX(4).forY(4).enter()
                .fromX(2).lenX(4).forY(9)
                .forEachCoordinatePair( (coord1, coord2) -> System.out.println(coord1 + ", " + coord2));

        System.out.println("--------------");

        new CoordinateSequencer<>(Coordinate::new)
                .fromX(2).lenX(4).forY(9)
                .forEachCoordinate(coordH -> {
                    new CoordinateSequencer(Coordinate::new)
                            .from(coordH).toX(7).lenY(4)
                            .forEachCoordinate(coordV -> System.out.println(coordV) );
                });
    }

}