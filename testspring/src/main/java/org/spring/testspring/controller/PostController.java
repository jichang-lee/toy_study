package org.spring.testspring.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.spring.testspring.domain.Post;
import org.spring.testspring.requset.PostCreate;
import org.spring.testspring.service.PostService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    
        //Case 1 : 저장한 데이터 Entity -> response 로 응답하기
            // - return postService.write(request);
        //Case 2 : 저장한 데이터 Entity -> PK로 응답하기
            // - Client 에서는 수신한 id를 글 조회 API를 통해서 데이터를 수신받음
        //Case 3 : 응답 필요없음 -> 클라이언트에서 모든 post 데이터 관리
        //BadCase : 서버에서 -> 반드시 이렇게 할 것이다라는 fix
            // - 서버에서 유연하게 할 필요성이 있다 (코딩 잘한다는 전제..)
            // - 일괄적으로 처리보다는 잘 관리하는 형태가 중요


    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request ) {
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public Post get(@PathVariable(name = "postId") Long id){
       return postService.get(id);
    }

}
