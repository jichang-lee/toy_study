package org.spring.testspring.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/*
* {
*   "code" : "200" ,
*   "message" : "잘못 된 요청",
*   "validation : {
*           "title" : "값을 입력해주세요"
*   }"
* }
* */


@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String message;
    private Map<String,String> validation = new HashMap<>();

    public void addValidation(String filedName, String errorMessage){
        this.validation.put(filedName,errorMessage);
    }


}
