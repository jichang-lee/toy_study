package org.spring.testspring.controller;

import java.util.Map;

import org.spring.testspring.requset.PostCreate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class PostController {
    

    // @GetMapping("/posts")
    // public String test(){
    //     return "testController Message";
    // }

    @PostMapping("/posts")
    public String post(@RequestBody PostCreate postCreate){
        log.info("postCreate{}" , postCreate.toString());
        return "testController Message";
    }
}
