package ru.alexredby.demo.controllers;

import com.ibasco.agql.protocols.valve.steam.webapi.SteamWebApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.alexredby.demo.pojos.ErrorResponse;

import java.time.LocalDateTime;
import java.util.concurrent.CompletionException;

/**
 * Controller to handle exceptions thrown in application.
 */
@ControllerAdvice
public class ExceptionController {
    private static final Logger log = LoggerFactory.getLogger(SteamWebApiClient.class);

    /**
     * Processes CompletionException and send a reason of exception to client.
     *
     * @param ex an exception to handle.
     * @return info about error with response status code = 409.
     */
    @ExceptionHandler(CompletionException.class)
    public ResponseEntity handleCompletionException(CompletionException ex) {
        HttpStatus responseStatus = HttpStatus.CONFLICT;

        ErrorResponse errors = new ErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getCause().getMessage());
        errors.setStatus(responseStatus.value());

        log.debug("Sending cause of CompletionException to client. Trace:", ex);

        return new ResponseEntity<>(errors, responseStatus);
    }
}
