package org.spring.testspring.requset;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.spring.testspring.domain.Post;

import javax.validation.constraints.NotBlank;

@ToString
@Getter
@Setter
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
