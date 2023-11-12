package org.spring.testspring.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class MasterException extends RuntimeException{

    public final Map<String,String> validation = new HashMap<>();

    public MasterException(String message) {
        super(message);
    }

//    public MasterException(String message, Throwable cause) {
//        super(message, cause);
//    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName , String message){
        validation.put(fieldName, message);

    }
}
