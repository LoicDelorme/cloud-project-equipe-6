package fr.polytech.cloud.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

@RestControllerAdvice
public class ExceptionsHandlerController extends AbstractController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception exception) {
        final Writer writer = new StringWriter();
        exception.printStackTrace(new PrintWriter(writer));

        LOGGER.error(writer.toString());
        return ResponseEntity.notFound().build();
    }
}