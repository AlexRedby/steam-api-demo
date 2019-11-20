package ru.alexredby.demo.controllers;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.alexredby.demo.exceptions.OutOfThresholdException;
import ru.alexredby.demo.exceptions.UserNotFoundException;
import ru.alexredby.demo.pojos.ErrorResponse;

import java.net.UnknownHostException;
import java.util.concurrent.CompletionException;

/**
 * Controller to handle exceptions thrown in application.
 */
// TODO: take out the messages somewhere else.
@ControllerAdvice
public class ExceptionController {
    private static final Logger log = LoggerFactory.getLogger(SteamWebApiClient.class);

    /**
     * Processes CompletionException and send a reason of exception to client.
     *
     * @param ex an exception to handle.
     * @return info about error.
     */
    @ExceptionHandler(CompletionException.class)
    public ResponseEntity handleCompletionException(CompletionException ex) {
        HttpStatus responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        Throwable causeEx = ex.getCause();
        if (causeEx instanceof UnknownHostException) {
            return getUnknownHostExceptionHandler((UnknownHostException) causeEx);
        } else if (causeEx instanceof OutOfThresholdException) {
            return getOutOfThresholdExceptionHandler((OutOfThresholdException) causeEx);
        }

        log.error("Unknown exception: ", ex);

        return new ResponseEntity<>(
                new ErrorResponse(responseStatus, "Unknown type of exception."),
                responseStatus
        );
    }

    /**
     * Processes UserNotFoundException and send user-friendly response to client.
     *
     * @param ex an exception to handle.
     * @return info about error.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleCompletionException(UserNotFoundException ex) {
        HttpStatus responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(
                new ErrorResponse(responseStatus, "User wasn't found."),
                responseStatus
        );
    }

    /**
     * Prepares {@code ResponseEntity} for {@code UnknownHostException}
     *
     * @param ex an exception to prepare
     * @return {@code ResponseEntity} with {@code ErrorResponse}
     */
    private ResponseEntity getUnknownHostExceptionHandler(UnknownHostException ex) {
        HttpStatus responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(
                new ErrorResponse(responseStatus, "Access to Steam Api is absent. Try later."),
                responseStatus
        );
    }

    /**
     * Prepares {@code ResponseEntity} for {@code OutOfThresholdException}
     *
     * @param ex an exception to prepare
     * @return {@code ResponseEntity} with {@code ErrorResponse}
     */
    private ResponseEntity getOutOfThresholdExceptionHandler(OutOfThresholdException ex) {
        HttpStatus responseStatus = HttpStatus.CONFLICT;

        return new ResponseEntity<>(
                new ErrorResponse(responseStatus, ex.getMessage()),
                responseStatus
        );
    }
}
