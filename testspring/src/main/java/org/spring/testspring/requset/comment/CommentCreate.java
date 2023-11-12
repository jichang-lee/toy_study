package org.spring.testspring.requset.comment;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.spring.testspring.domain.Post;

@Getter
@Setter
public class CommentCreate {


    @Length(min = 1 , max = 8 ,message = "작성자는 1~8 글자까지 가능합니다.")
    @NotBlank(message = "작성자를 입력해주세요.")
    private String author;

    @Length(min = 6 , max = 20 ,message = "비밀번호는 6~20 글자까지 가능합니다.")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @Length(min = 5 , max = 1000 ,message = "내용은 1000 글자까지 가능합니다.")
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Builder
    public CommentCreate(String author, String password, String content) {
        this.author = author;
        this.password = password;
        this.content = content;
    }
}
