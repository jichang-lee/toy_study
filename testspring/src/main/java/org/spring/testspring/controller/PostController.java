package org.spring.testspring.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spring.testspring.requset.PostCreate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

@Slf4j
@RestController
public class PostController {
    

    // @GetMapping("/posts")
    // public String test(){
    //     return "testController Message";
    // }

    @PostMapping("/posts")
    public Map<String,String> post(@RequestBody @Valid PostCreate params , BindingResult result) {

        if(result.hasErrors()){
           List<FieldError> fieldErrorList = result.getFieldErrors();

            FieldError fieldError= fieldErrorList.get(0); //0번지
            String fieldName=fieldError.getField();// title
            String fieldMessage = fieldError.getDefaultMessage(); //에러메세지

            Map<String,String> error = new HashMap<>();
            error.put(fieldName,fieldMessage);

            return error;
        }
        return Map.of();
    }
}
