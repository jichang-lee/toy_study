package org.spring.testspring.requset;

import lombok.*;
import org.spring.testspring.domain.Post;
import org.spring.testspring.exception.InvalidRequest;

import javax.validation.constraints.NotBlank;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요")
    private String title;

    @NotBlank(message = "컨텐츠을 입력해주세요")
    private String content;


    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate(){
        if(title.contains("탈모")){
            throw new InvalidRequest("title","제목에 탈모를 포함할 수  없습니다.");
        }

    }


    // @Builder 장점
    //  - 가독성에 좋다
    //  - 필요한 값만 받을 수 있다 (값에 대한 유연성)
    //  - 객체 불변성 * 가장 중요한듯

}
