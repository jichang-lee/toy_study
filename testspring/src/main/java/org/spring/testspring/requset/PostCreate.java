package org.spring.testspring.requset;

import lombok.*;
import org.spring.testspring.domain.Post;

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
    public PostCreate(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
