package ru.alexredby.demo.interfaces;

import ru.alexredby.demo.exceptions.OutOfThresholdException;

/**
 * A class implements the <code>Thresholdable</code> interface to
 * provide it with counter and threshold to limit, for example,
 * the calls of some method.
 */
public interface Thresholdable {
    /** Default increment value of counter. */
    int INCREMENT_VALUE = 1;
    /** Default initial value of counter. */
    int COUNTER_INITIAL_VALUE = 0;

    /**
     * @return value of counter.
     */
    int getCount();

    /**
     * @return value of threshold.
     */
    int getThreshold();

    /**
     * Resets counter to initial value.
     */
    void resetCounter();

    /**
     * Increment counter on some value(depends on realisation).
     *
     * @throws OutOfThresholdException when counter can't be increment because of reaching threshold.
     */
    void incrementCounter() throws OutOfThresholdException;
}
