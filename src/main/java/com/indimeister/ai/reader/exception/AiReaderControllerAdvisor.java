package com.indimeister.ai.reader.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AiReaderControllerAdvisor {

    @ExceptionHandler(AiReaderException.class)
    public ResponseEntity<String> handleGameNotFound(AiReaderException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

}
