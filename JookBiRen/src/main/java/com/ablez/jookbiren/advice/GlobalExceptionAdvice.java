package com.ablez.jookbiren.advice;

import com.ablez.jookbiren.exception.BusinessLogicException;
import com.ablez.jookbiren.exception.ErrorResponse;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        final ErrorResponse errorResponse = ErrorResponse.of(exception.getBindingResult());
        log.error("error: {}", exception.getMessage());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException exception) {
        final ErrorResponse errorResponse = ErrorResponse.of(exception.getConstraintViolations());
        log.error("error: {}", exception.getMessage());
        return errorResponse;
    }

    @ExceptionHandler
    public ResponseEntity handleBusinessLogicException(BusinessLogicException exception) {
        final ErrorResponse errorResponse = ErrorResponse.of(exception.getExceptionCode());
        log.error("error: {}", exception.getExceptionCode().getMessage());
        return new ResponseEntity(errorResponse, HttpStatus.valueOf(exception.getExceptionCode().getStatus()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        final ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED);
        log.error("error: {}", exception.getMessage());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        final ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST,
                "Required request body is missing");
        log.error("error: {}", exception.getMessage());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(
            MissingServletRequestParameterException exception) {
        final ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, exception.getMessage());
        log.error("error: {}", exception.getMessage());
        return errorResponse;
    }
}
