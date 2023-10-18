package org.spring.testspring.response;

import lombok.Builder;
import lombok.Getter;
import org.spring.testspring.domain.Post;


/*
* 서비스 정책에 맞는 응답 클래스
* */
@Getter
public class PostResponse {

    private Long id;
    private String title;
    private String content;

    public PostResponse(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }

    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0,Math.min(title.length(),10));
        this.content = content;
    }
}
