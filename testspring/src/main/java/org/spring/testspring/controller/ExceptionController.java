package org.spring.testspring.controller;

import lombok.extern.slf4j.Slf4j;
import org.spring.testspring.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse exceptionHandler(MethodArgumentNotValidException e){
        ErrorResponse response = new ErrorResponse("400", "잘못 된 요청");

        for(FieldError fieldError : e.getFieldErrors()){
               response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
           }
            return response;
        }



}
