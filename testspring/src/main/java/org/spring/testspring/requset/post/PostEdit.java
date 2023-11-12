package org.spring.testspring.requset.post;

import lombok.*;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostEdit {

    @NotBlank(message = "타이틀을 입력해주세요")
    private String title;

    @NotBlank(message = "컨텐츠을 입력해주세요")
    private String content;


    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
