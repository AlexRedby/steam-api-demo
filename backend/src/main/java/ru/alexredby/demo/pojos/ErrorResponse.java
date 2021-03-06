package ru.alexredby.demo.pojos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Custom error response object to return to client.
 */
@Data
public class ErrorResponse {
    /** Time when error happened. */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    /** Number of http status. */
    private int status;
    /** Human-readable error message. */
    private String error;

    public ErrorResponse(HttpStatus status, String error) {
        this(status.value(), error);
    }

    public ErrorResponse(int status, String error) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
    }
}
