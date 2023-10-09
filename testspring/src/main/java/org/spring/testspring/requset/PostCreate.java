package org.spring.testspring.requset;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class PostCreate {
    
    private String title;
    private String content;
}
