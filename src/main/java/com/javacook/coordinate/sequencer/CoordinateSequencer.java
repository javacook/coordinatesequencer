package com.javacook.coordinate.sequencer;
import com.javacook.coordinate.CoordinateFactory;
import com.javacook.coordinate.CoordinateInterface;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by vollmer on 20.08.16.
 */
public class CoordinateSequencer<T extends CoordinateInterface>  {

    private List<CoordinateSequence<T>> coordinateSequences = new ArrayList<>();
    private CoordinateFactory<T> coordinateFactory;
    private Integer xFrom;
    private Integer yFrom;
    private Integer xTo;
    private Integer yTo;
    private Integer xLen;
    private Integer yLen;
    private int xStep;
    private int yStep;
    private boolean virgin;


    public CoordinateSequencer(CoordinateFactory<T> coordinateFactory) {
        this.coordinateFactory = coordinateFactory;
        initCache();
    }

    protected void initCache() {
        xFrom = yFrom = null;
        xTo = yTo = null;
        xStep = yStep = 1;
        virgin = true;
    }

    public CoordinateSequencer<T> fromX(int x) {
        xFrom = x;
        virgin = false;
        return this;
    }

    public CoordinateSequencer<T> fromY(int y) {
        yFrom = y;
        virgin = false;
        return this;
    }


    public CoordinateSequencer<T> from(int x, int y) {
        return fromX(x).fromY(y);
    }


    public CoordinateSequencer<T> from(CoordinateInterface c) {
        Objects.requireNonNull(c, "Argument 'c' is null.");
        return from(c.x(), c.y());
    }


    public CoordinateSequencer<T> toX(int xTo) {
        this.xTo = xTo;
        virgin = false;
        return this;
    }

    public CoordinateSequencer<T> toY(int yTo) {
        this.yTo = yTo;
        virgin = false;
        return this;
    }

    public CoordinateSequencer<T> ontoX(int x) {
        return toX(x + 1);
    }


    public CoordinateSequencer<T> ontoY(int y) {
        return toY(y + 1);
    }


    public CoordinateSequencer<T> to(int x, int y) {
        return toX(x).toY(y);
    }

    public CoordinateSequencer<T> to(CoordinateInterface c) {
        return to(c.x(), c.y());
    }


    public CoordinateSequencer<T> forX(int x) {
        return fromX(x).toX(x+1);
    }

    public CoordinateSequencer<T> forY(int y) {
        return fromY(y).toY(y+1);
    }


    public CoordinateSequencer<T> lenX(int len) {
        this.xLen = len;
        virgin = false;
        return this;
    }

    public CoordinateSequencer<T> lenY(int len) {
        this.yLen = len;
        virgin = false;
        return this;
    }

    public CoordinateSequencer<T> stepX(int step) {
        this.xStep = step;
        virgin = false;
        return this;
    }

    public CoordinateSequencer<T> stepY(int step) {
        this.yStep = step;
        virgin = false;
        return this;
    }


    public CoordinateSequencer<T> enter() {
        coordinateSequences.add(sequence());
        initCache();
        return this;
    }



    public CoordinateSequence<T> sequence() {
        try {
            if (xFrom == null) xFrom = xTo - xLen;
            else if (xTo == null) xTo = xFrom + xLen;
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("x-coordinates are not well defined." +
                    "from=("+xFrom+","+yFrom+"), to=("+xTo+","+yTo+"), len=("+xLen+","+yLen+")");
        }
        try {
            if (yFrom == null) yFrom = yTo - yLen;
            else if (yTo == null) yTo = yFrom + yLen;
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("y-coordinates are not well defined." +
                    "from=("+xFrom+","+yFrom+"), to=("+xTo+","+yTo+"), len=("+xLen+","+yLen+")");
        }
        return new CoordinateSequence<T>(xFrom, yFrom, xTo, yTo, xStep, yStep, coordinateFactory);
    }


    /*-------------------------------------------------------------------------*\
     * terminate                                                               *
    \*-------------------------------------------------------------------------*/


    protected Predicate<T> predicate;
    protected PredicateAndCounter<T> predicateAndCounter;
    protected PairPredicate<T> pairPredicate;
    protected PairPredicateAndCounter<T> pairPredicateAndCounter;
    protected ArrayPredicate<T> arrayPredicate;
    protected ArrayPredicateAndCounter<T> arrayPredicateAndCounter;


    public CoordinateSequencer<T> stopWhen(Predicate<T> condition) {
        if (!virgin) enter();
        if (coordinateSequences.size() != 1) {
            throw new IllegalArgumentException("This method allows exactly one coordinate sequence.");
        }
        predicate = condition;
        return this;
    }

    public CoordinateSequencer<T> stopWhen(PredicateAndCounter<T> condition) {
        if (!virgin) enter();
        if (coordinateSequences.size() != 1) {
            throw new IllegalArgumentException("This method allows exactly one coordinate sequence.");
        }
        predicateAndCounter = condition;
        return this;
    }

    public CoordinateSequencer<T> stopWhenPair(PairPredicate<T> condition) {
        if (!virgin) enter();
        if (coordinateSequences.size() != 2) {
            throw new IllegalArgumentException("This method allows exactly two coordinate sequences.");
        }
        pairPredicate = condition;
        return this;
    }

    public CoordinateSequencer<T> stopWhenPair(PairPredicateAndCounter<T> condition) {
        if (!virgin) enter();
        if (coordinateSequences.size() != 2) {
            throw new IllegalArgumentException("This method allows exactly two coordinate sequences.");
        }
        pairPredicateAndCounter = condition;
        return this;
    }

    public CoordinateSequencer<T> stopWhenArray(ArrayPredicate<T> condition) {
        if (!virgin) enter();
        arrayPredicate = condition;
        return this;
    }

    public CoordinateSequencer<T> stopWhenArray(ArrayPredicateAndCounter<T> condition) {
        if (!virgin) enter();
        arrayPredicateAndCounter = condition;
        return this;
    }


    protected boolean terminate(T coord1, int counter) {
        return (predicate != null && predicate.test(coord1)) ||
                (predicateAndCounter != null && predicateAndCounter.test(coord1, counter));
    }

    protected boolean terminate(T coord1, T coord2, int counter) {
        return (pairPredicate != null && pairPredicate.test(coord1, coord2)) ||
                (pairPredicateAndCounter != null && pairPredicateAndCounter.test(coord1, coord2, counter));
    }

    protected boolean terminate(T[] coords, int counter) {
        return (arrayPredicate != null && arrayPredicate.test(coords)) ||
                (arrayPredicateAndCounter != null && arrayPredicateAndCounter.test(coords, counter));
    }



    /*-------------------------------------------------------------------------*\
     * forEach                                                       *
    \*-------------------------------------------------------------------------*/


    public void forEach(Consumer<? super T> action) {
        if (!virgin) enter();
        if (coordinateSequences.size() != 1) {
            throw new IllegalArgumentException("This method allows exactly one coordinate sequence.");
        }
        int counter = 0;
        for (T coord : coordinateSequences.get(0)) {
            if (terminate(coord, counter++)) break;
            action.apply(coord);
        }
    }

    public void forEach(ConsumerAndCounter<? super T> action) {
        if (!virgin) enter();
        if (coordinateSequences.size() != 1) {
            throw new IllegalArgumentException("This method allows exactly one coordinate sequence.");
        }
        int counter = 0;
        for (T coord : coordinateSequences.get(0)) {
            if (terminate(coord, counter)) break;
            action.apply(coord, counter++);
        }
    }


    public void forEachPair(PairConsumer<T> action) {
        if (!virgin) enter();
        if (coordinateSequences.size() != 2) {
            throw new IllegalArgumentException("This method allows exactly two CoordinateSequences.");
        }
        Iterator<T> iter = coordinateSequences.get(1).iterator();
        int counter = 0;
        for (T coord1 : coordinateSequences.get(0)) {
            if (!iter.hasNext()) break;
            T coord2 = iter.next();
            if (terminate(coord1, coord2, counter++)) break;
            action.apply(coord1, coord2);
        }
    }


    public void forEachPair(PairConsumerAndCounter<T> action) {
        if (!virgin) enter();
        if (coordinateSequences.size() != 2) {
            throw new IllegalArgumentException("This method allows exactly two CoordinateSequences.");
        }
        Iterator<T> iter = coordinateSequences.get(1).iterator();
        int counter = 0;
        for (T coord1 : coordinateSequences.get(0)) {
            if (!iter.hasNext()) break;
            T coord2 = iter.next();
            if (terminate(coord1, coord2, counter)) break;
            action.apply(coord1, coord2, counter++);
        }
    }


    public void forEachArray(ArrayConsumer<T> action) {
        if (!virgin) enter();
        List<Iterator<T>> coordinateSequencesIterators = new ArrayList<>();
        for (CoordinateSequence<T> coordinateSequence : coordinateSequences) {
            coordinateSequencesIterators.add(coordinateSequence.iterator());
        }

        T[] coordinates = null;
        loop:
        for (int loopCnt = 0;; loopCnt++) {
            int coordArrIndex = 0;
            for (Iterator<T> iter : coordinateSequencesIterators) {
                if (!iter.hasNext()) break loop;
                T next = iter.next();
                if (coordinates == null) {
                    coordinates = (T[]) Array.newInstance(next.getClass(), coordinateSequences.size());
                }
                coordinates[coordArrIndex++] = next;
            }
            if (terminate(coordinates, loopCnt)) break loop;
            action.apply(coordinates);
        }
    }


    public void forEachArray(ArrayConsumerAndCounter<T> action) {
        if (!virgin) enter();
        List<Iterator<T>> coordinateSequencesIterators = new ArrayList<>();
        for (CoordinateSequence<T> coordinateSequence : coordinateSequences) {
            coordinateSequencesIterators.add(coordinateSequence.iterator());
        }

        T[] coordinates = null;
        loop:
        for (int loopCnt = 0;; loopCnt++) {
            int coordArrIndex = 0;
            for (Iterator<T> iter : coordinateSequencesIterators) {
                if (!iter.hasNext()) break loop;
                T next = iter.next();
                if (coordinates == null) {
                    coordinates = (T[]) Array.newInstance(next.getClass(), coordinateSequences.size());
                }
                coordinates[coordArrIndex++] = next;
            }
            if (terminate(coordinates, loopCnt)) break loop;
            action.apply(coordinates, loopCnt);
        }
    }


}