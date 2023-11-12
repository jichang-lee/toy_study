package org.spring.testspring.controller;

import lombok.extern.slf4j.Slf4j;
import org.spring.testspring.exception.InvalidRequest;
import org.spring.testspring.exception.MasterException;
import org.spring.testspring.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse exceptionHandler(MethodArgumentNotValidException e){
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.(errorController)")
                .build();

        for(FieldError fieldError : e.getFieldErrors()){
               response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
           }
            return response;
        }

    @ExceptionHandler(MasterException.class)
    public ResponseEntity<ErrorResponse> postNotFound(MasterException e){
       int statusCode = e.getStatusCode();

        ErrorResponse response = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

    return  ResponseEntity.status(statusCode)
                .body(response);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e){
        log.error("예외 발생=!!==>" , e);

        ErrorResponse response = ErrorResponse.builder()
                .code(String.valueOf(500))
                .message(e.getMessage())
                .build();

        return  ResponseEntity.status(500)
                .body(response);
    }

}
