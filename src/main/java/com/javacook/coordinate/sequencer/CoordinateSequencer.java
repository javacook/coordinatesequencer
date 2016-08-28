package com.javacook.coordinate.sequencer;

import com.javacook.coordinate.Coordinate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by vollmer on 20.08.16.
 */
public class CoordinateSequencer {

    private List<CoordinateSequence> coordinateSequences = new ArrayList<>();
    private Integer xFrom;
    private Integer yFrom;
    private Integer xTo;
    private Integer yTo;
    private Integer xLen;
    private Integer yLen;
    private int xStep;
    private int yStep;
    private boolean virgin;


    public CoordinateSequencer() {
        initCache();
    }

    protected void initCache() {
        xFrom = yFrom = null;
        xTo = yTo = null;
        xStep = yStep = 1;
        virgin = true;
    }

    public CoordinateSequencer fromX(int x) {
        xFrom = x;
        virgin = false;
        return this;
    }

    public CoordinateSequencer fromY(int y) {
        yFrom = y;
        virgin = false;
        return this;
    }

    public CoordinateSequencer from(Coordinate c) {
        return fromX(c.x).fromY(c.y);
    }


    public CoordinateSequencer toX(int xTo) {
        this.xTo = xTo;
        virgin = false;
        return this;
    }

    public CoordinateSequencer toY(int yTo) {
        this.yTo = yTo;
        virgin = false;
        return this;
    }

    public CoordinateSequencer to(Coordinate c) {
        return toX(c.x).toY(c.y);
    }

    public CoordinateSequencer forX(int x) {
        return fromX(x).lenX(1);
    }

    public CoordinateSequencer forY(int y) {
        return fromY(y).lenY(1);
    }


    public CoordinateSequencer lenX(int xLength) {
        this.xLen = xLength;
        virgin = false;
        return this;
    }

    public CoordinateSequencer lenY(int yLength) {
        this.yLen = yLength;
        virgin = false;
        return this;
    }


    public CoordinateSequencer stepX(int stepX) {
        this.xStep = Objects.requireNonNull(stepX, "Argument stepX must not be null.");
        virgin = false;
        return this;
    }

    public CoordinateSequencer stepY(int stepY) {
        this.yStep = Objects.requireNonNull(stepY, "Argument stepY must not be null.");
        virgin = false;
        return this;
    }

    public CoordinateSequencer ontoX(int x) {
        return toX(x + 1);
    }

    public CoordinateSequencer ontoY(int y) {
        return toY(y + 1);
    }

    public CoordinateSequencer enter() {
        coordinateSequences.add(build());
        initCache();
        return this;
    }


    public CoordinateSequence build() {
        try {
            if (xFrom == null) xFrom = xTo - xLen;
            else if (xTo == null) xTo = xFrom + xLen;
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("x-coordinates are not well defined.");
        }
        try {
            if (yFrom == null) yFrom = yTo - yLen;
            else if (yTo == null) yTo = yFrom + yLen;
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("y-coordinates are not well defined.");
        }
        return new CoordinateSequence(xFrom, yFrom, xTo, yTo, xStep, yStep);
    }

    @Override
    public String toString() {
        return "CoordinateSequencer{" +
                "coordinateSequences=" + coordinateSequences +
                '}';
    }


    /*-------------------------------------------------------------------------*\
     * terminate                                                               *
    \*-------------------------------------------------------------------------*/

    private Predicate<Coordinate> predicate;
    private PredicateAndCounter<Coordinate> predicateAndCounter;
    private PairPredicate<Coordinate> pairPredicate;
    private PairPredicateAndCounter<Coordinate> pairPredicateAndCounter;
    private ArrayPredicate<Coordinate> arrayPredicate;
    private ArrayPredicateAndCounter<Coordinate> arrayPredicateAndCounter;


    public CoordinateSequencer stopWhen(Predicate<Coordinate> condition) {
        predicate = condition;
        return this;
    }

    public CoordinateSequencer stopWhen(PredicateAndCounter<Coordinate> condition) {
        predicateAndCounter = condition;
        return this;
    }

    public CoordinateSequencer stopWhenPair(PairPredicate<Coordinate> condition) {
        pairPredicate = condition;
        return this;
    }

    public CoordinateSequencer stopWhenPair(PairPredicateAndCounter<Coordinate> condition) {
        pairPredicateAndCounter = condition;
        return this;
    }

    public CoordinateSequencer stopWhenArray(ArrayPredicate<Coordinate> condition) {
        arrayPredicate = condition;
        return this;
    }

    public CoordinateSequencer stopWhenArray(ArrayPredicateAndCounter<Coordinate> condition) {
        arrayPredicateAndCounter = condition;
        return this;
    }


    private boolean terminate(Coordinate coord1, int counter) {
        return (predicate != null && predicate.test(coord1)) ||
                (predicateAndCounter != null && predicateAndCounter.test(coord1, counter));
    }

    private boolean terminate(Coordinate coord1, Coordinate coord2, int counter) {
        return (pairPredicate != null && pairPredicate.test(coord1, coord2)) ||
                (pairPredicateAndCounter != null && pairPredicateAndCounter.test(coord1, coord2, counter));
    }

    private boolean terminate(Coordinate[] coords, int counter) {
        return (arrayPredicate != null && arrayPredicate.test(coords)) ||
                (arrayPredicateAndCounter != null && arrayPredicateAndCounter.test(coords, counter));
    }

    /*-------------------------------------------------------------------------*\
     * forEach                                                                 *
    \*-------------------------------------------------------------------------*/


    public void forEach(Consumer<Coordinate> action) {
        if (!virgin) enter();
        if (coordinateSequences.size() != 1) {
            throw new IllegalArgumentException("This method allows exactly one CoordinateSequence.");
        }
        int counter = 0;
        for (Coordinate coord : coordinateSequences.get(0)) {
            if (terminate(coord, counter++)) break;
            action.apply(coord);
        }
    }


    public void forEach(ConsumerAndCounter<Coordinate> action) {
        if (!virgin) enter();
        if (coordinateSequences.size() != 1) {
            throw new IllegalArgumentException("This method allows exactly one CoordinateSequence.");
        }
        int counter = 0;
        for (Coordinate coord : coordinateSequences.get(0)) {
            if (terminate(coord, counter)) break;
            action.apply(coord, counter++);
        }
    }

    public void forEachPair(PairConsumer<Coordinate> action) {
        if (!virgin) enter();
        if (coordinateSequences.size() != 2) {
            throw new IllegalArgumentException("This method allows exactly two CoordinateSequences.");
        }
        Iterator<Coordinate> iter = coordinateSequences.get(1).iterator();
        int counter = 0;
        for (Coordinate coord1 : coordinateSequences.get(0)) {
            if (!iter.hasNext()) break;
            Coordinate coord2 = iter.next();
            if (terminate(coord1, coord2, counter)) break;
            action.apply(coord1, coord2);
        }
    }


    public void forEachPair(PairConsumerAndCounter<Coordinate> action) {
        if (!virgin) enter();
        if (coordinateSequences.size() != 2) {
            throw new IllegalArgumentException("This method allows exactly two CoordinateSequences.");
        }
        Iterator<Coordinate> iter = coordinateSequences.get(1).iterator();
        int counter = 0;
        for (Coordinate coord1 : coordinateSequences.get(0)) {
            if (!iter.hasNext()) break;
            Coordinate coord2 = iter.next();
            if (terminate(coord1, coord2, counter)) break;
            action.apply(coord1, coord2, counter);
        }
    }


    public void forEachArray(ArrayConsumer<Coordinate> action) {
        if (!virgin) enter();
        List<Iterator<Coordinate>> coordinateSequencesIterators = new ArrayList<>();
        for (CoordinateSequence coordinateSequence : coordinateSequences) {
            coordinateSequencesIterators.add(coordinateSequence.iterator());
        }
        Coordinate[] coordinates = new Coordinate[coordinateSequences.size()];
        loop:
        for (int loopCnt = 0;; loopCnt++) {
            int coordArrIndex = 0;
            for (Iterator<Coordinate> iter : coordinateSequencesIterators) {
                if (!iter.hasNext()) break loop;
                coordinates[coordArrIndex++] = iter.next();
            }
            if (terminate(coordinates, loopCnt)) break loop;
            action.apply(coordinates);
        }
    }


    public void forEachArray(ArrayConsumerAndCounter<Coordinate> action) {
        if (!virgin) enter();
        List<Iterator<Coordinate>> coordinateSequencesIterators = new ArrayList<>();
        for (CoordinateSequence coordinateSequence : coordinateSequences) {
            coordinateSequencesIterators.add(coordinateSequence.iterator());
        }
        Coordinate[] coordinates = new Coordinate[coordinateSequences.size()];
        loop:
        for (int loopCnt = 0;; loopCnt++) {
            int coordArrIndex = 0;
            for (Iterator<Coordinate> iter : coordinateSequencesIterators) {
                if (!iter.hasNext()) break loop;
                coordinates[coordArrIndex++] = iter.next();
            }
            if (terminate(coordinates, loopCnt)) break loop;
            action.apply(coordinates, loopCnt);
        }
    }

}
