package fr.polytech.cloud.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

@ControllerAdvice
public class ExceptionHandlerController extends AbstractController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(HttpServletRequest request, Exception exception) {
        final Writer writer = new StringWriter();
        exception.printStackTrace(new PrintWriter(writer));

        final String error = writer.toString();
        LOGGER.error(error);

        return new ResponseEntity<String>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
}