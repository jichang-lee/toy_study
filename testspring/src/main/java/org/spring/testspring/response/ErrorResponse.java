package org.spring.testspring.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
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
//@JsonInclude(value = JsonInclude.Include.NON_EMPTY) 비어있는 값은 X -> 개인적으로 선호하지 않아서 주석
public class ErrorResponse {

    private final String code;
    private final String message;
    private Map<String,String> validation = new HashMap<>();

    @Builder
    public ErrorResponse(String code, String message ,Map<String,String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation;
    }

    public void addValidation(String filedName, String errorMessage){
        this.validation.put(filedName,errorMessage);
    }


}
