package com.javacook.coordinate.sequencer;

import com.javacook.coordinate.Coordinate;
import com.javacook.coordinate.CoordinateInterface;

public class CoordinateSequenceMain {


    public static void main(String[] args) {
        CoordinateSequence<Coordinate> sequence = new CoordinateSequence(5, 6, 7, 8, Coordinate::new);
        sequence.forEach(t -> System.out.println(t.x()));

        new CoordinateSequence<>(5, 6, 7, 8, Coordinate::new).forEach(t -> System.out.println(t.x()));


        CoordinateSequence<CoordinateInterface> sequence2 = new CoordinateSequence(5, 6, 7, 8, CoordinateInterface::create);
        sequence2.forEach(t -> System.out.println(t.x() + "," + t.y()));

    }

}