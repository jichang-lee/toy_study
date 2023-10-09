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
    public Map<String,String> post(@RequestBody @Valid PostCreate params ) {

        return Map.of();
    }
}
