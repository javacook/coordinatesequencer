package com.javacook.coordinate.sequencer;

import com.javacook.coordinate.Coordinate;

public class CoordinateSequencerMain {


    public static void main(String[] args) {

        new CoordinateSequencer<>(Coordinate::new)
                .fromX(3).fromY(5).toX(5).toY(6)
                .sequence().forEach(coord -> System.out.println(coord));

        System.out.println("--------------");

        new CoordinateSequencer<>(Coordinate::new)
                .fromX(3).fromY(5).toX(5).toY(6)
                .sequence()
                .forEach(cord -> System.out.println(cord.x()));

        System.out.println("--------------");

        new CoordinateSequencer<>(Coordinate::new)
                .fromX(3).fromY(5).toX(5).toY(6)
                .forEachCoordinate(coord -> System.out.println(coord.x()));

        System.out.println("--------------");

        new CoordinateSequencer<>(Coordinate::new)
                .fromX(3).fromY(5).toX(8).toY(6)
                .stopWhenCoordinate(coord -> coord.x() == 6)
                .forEachCoordinate((coord, i) -> System.out.println(coord + " - " + i));

        System.out.println("--------------");

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