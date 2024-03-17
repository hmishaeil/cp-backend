package com.cp.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@ResponseBody
@RequiredArgsConstructor
public class ExceptionsHandler {

    @ExceptionHandler({ BadRequestException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionMessage duplicateResourceFoundException(Exception ex, ServletWebRequest request) {
        return buildExceptionMessage(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({ ResourceNotFoundException.class })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ExceptionMessage resourceNotFoundException(Exception ex, ServletWebRequest request) {
        return buildExceptionMessage(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({ AuthFailedException.class })
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ExceptionMessage unauthorizedException(Exception ex, ServletWebRequest request) {
        return buildExceptionMessage(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({ RuntimeException.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage otherException(Exception ex, ServletWebRequest request) {

        return buildExceptionMessage(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ExceptionMessage buildExceptionMessage(String message, Integer statusCode) {

        ExceptionMessage exceptionMessage = new ExceptionMessage();
        exceptionMessage.setStatus(statusCode);
        exceptionMessage.setMessage(message);
        return exceptionMessage;

    }
}
