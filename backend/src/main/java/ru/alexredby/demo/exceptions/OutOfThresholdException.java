package ru.alexredby.demo.exceptions;

public class OutOfThresholdException extends Throwable {

    public OutOfThresholdException(int threshold) {
        super("Threshold of " + threshold + " was crossed!");
    }

    public OutOfThresholdException(String message) {
        super(message);
    }
}
