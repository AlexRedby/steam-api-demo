package ru.alexredby.demo.exceptions;

/**
 * Throw when trying increment counter which already reach the threshold.
 */
public class OutOfThresholdException extends Throwable {

    public OutOfThresholdException(String message) {
        super(message);
    }
}
