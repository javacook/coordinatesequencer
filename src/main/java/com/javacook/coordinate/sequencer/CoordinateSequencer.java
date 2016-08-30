package com.javacook.coordinate.sequencer;

import com.javacook.coordinate.Coordinate;
import com.javacook.coordinate.CoordinateSequence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by vollmer on 20.08.16.
 */
public class CoordinateSequencer {

    protected List<CoordinateSequence> coordinateSequences = new ArrayList<>();
    protected Integer xFrom;
    protected Integer yFrom;
    protected Integer xTo;
    protected Integer yTo;
    protected Integer xLen;
    protected Integer yLen;
    protected int xStep;
    protected int yStep;
    protected boolean virgin;


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
        coordinateSequences.add(sequence());
        initCache();
        return this;
    }


    public CoordinateSequence sequence() {
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

    protected Predicate<Coordinate> predicate;
    protected PredicateAndCounter<Coordinate> predicateAndCounter;
    protected PairPredicate<Coordinate> pairPredicate;
    protected PairPredicateAndCounter<Coordinate> pairPredicateAndCounter;
    protected ArrayPredicate<Coordinate> arrayPredicate;
    protected ArrayPredicateAndCounter<Coordinate> arrayPredicateAndCounter;


    public CoordinateSequencer stopWhenCoordinate(Predicate<Coordinate> condition) {
        if (!virgin) enter();
        if (coordinateSequences.size() != 1) {
            throw new IllegalArgumentException("This method allows exactly one CoordinateSequence.");
        }
        predicate = condition;
        return this;
    }

    public CoordinateSequencer stopWhenCoordinate(PredicateAndCounter<Coordinate> condition) {
        if (!virgin) enter();
        if (coordinateSequences.size() != 1) {
            throw new IllegalArgumentException("This method allows exactly one CoordinateSequence.");
        }
        predicateAndCounter = condition;
        return this;
    }

    public CoordinateSequencer stopWhenCoordinatePair(PairPredicate<Coordinate> condition) {
        if (!virgin) enter();
        if (coordinateSequences.size() != 2) {
            throw new IllegalArgumentException("This method allows exactly two CoordinateSequences.");
        }
        pairPredicate = condition;
        return this;
    }

    public CoordinateSequencer stopWhenCoordinatePair(PairPredicateAndCounter<Coordinate> condition) {
        if (!virgin) enter();
        if (coordinateSequences.size() != 2) {
            throw new IllegalArgumentException("This method allows exactly two CoordinateSequences.");
        }
        pairPredicateAndCounter = condition;
        return this;
    }

    public CoordinateSequencer stopWhenCoordinateArray(ArrayPredicate<Coordinate> condition) {
        if (!virgin) enter();
        arrayPredicate = condition;
        return this;
    }

    public CoordinateSequencer stopWhenCoordinateArray(ArrayPredicateAndCounter<Coordinate> condition) {
        if (!virgin) enter();
        arrayPredicateAndCounter = condition;
        return this;
    }


    protected boolean terminate(Coordinate coord1, int counter) {
        return (predicate != null && predicate.test(coord1)) ||
                (predicateAndCounter != null && predicateAndCounter.test(coord1, counter));
    }

    protected boolean terminate(Coordinate coord1, Coordinate coord2, int counter) {
        return (pairPredicate != null && pairPredicate.test(coord1, coord2)) ||
                (pairPredicateAndCounter != null && pairPredicateAndCounter.test(coord1, coord2, counter));
    }

    protected boolean terminate(Coordinate[] coords, int counter) {
        return (arrayPredicate != null && arrayPredicate.test(coords)) ||
                (arrayPredicateAndCounter != null && arrayPredicateAndCounter.test(coords, counter));
    }

    /*-------------------------------------------------------------------------*\
     * forEachCoordinate                                                                 *
    \*-------------------------------------------------------------------------*/


    public void forEachCoordinate(Consumer<Coordinate> action) {
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


    public void forEachCoordinate(ConsumerAndCounter<Coordinate> action) {
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

    public void forEachCoordinatePair(PairConsumer<Coordinate> action) {
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


    public void forEachCoordinatePair(PairConsumerAndCounter<Coordinate> action) {
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


    public void forEachCoordinateArray(ArrayConsumer<Coordinate> action) {
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


    public void forEachCoordinateArray(ArrayConsumerAndCounter<Coordinate> action) {
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
