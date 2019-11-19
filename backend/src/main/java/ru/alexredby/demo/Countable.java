package ru.alexredby.demo;

import ru.alexredby.demo.exceptions.OutOfThresholdException;

// TODO: rewrite
public interface Countable {
    /**
     *
     * @return
     */
    int getCount();

    /**
     *
     */
    void reset();

    /**
     *
     */
    void increment() throws OutOfThresholdException;
}
