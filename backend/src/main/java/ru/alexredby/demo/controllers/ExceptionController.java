package ru.alexredby.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.CompletionException;

@ControllerAdvice
public class ExceptionController {

    // TODO: write custom error model without trace
    @ExceptionHandler(CompletionException.class)
    public void handleCompletionException(Exception ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.I_AM_A_TEAPOT.value(), ex.getCause().getMessage());
    }
}
