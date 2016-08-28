package com.javacook.coordinate.sequencer;

public class CoordinateSequencerMain {


    public static void main(String[] args) {

        new CoordinateSequencer().forX(5).forY(9)
                .forEach(System.out::println);

        System.out.println("--------------");

        new CoordinateSequencer()
                .fromX(1).lenX(14).forY(4)
                .stopWhen(coord -> coord.x > 10)
                .forEach(coord -> System.out.println(coord));

        System.out.println("--------------");

        new CoordinateSequencer()
                .fromX(1).lenX(4).forY(4).enter()
                .fromX(2).lenX(4).forY(9)
                .forEachArray(coords -> System.out.println(coords[0] + ", " + coords[1]));

        System.out.println("--------------");

        new CoordinateSequencer()
                .fromX(1).lenX(4).forY(4).enter()
                .fromX(2).lenX(4).forY(9)
                .forEachPair( (coord1, coord2) -> System.out.println(coord1 + ", " + coord2));

        System.out.println("--------------");

        new CoordinateSequencer()
                .fromX(2).lenX(4).forY(9)
                .forEach(coordH -> {
                    new CoordinateSequencer()
                            .from(coordH).toX(7).lenY(4)
                            .forEach(coordV -> System.out.println(coordV) );
                });
    }

}