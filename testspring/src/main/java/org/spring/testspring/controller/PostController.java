package org.spring.testspring.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.spring.testspring.config.UserPrincipal;
import org.spring.testspring.requset.post.PostCreate;
import org.spring.testspring.requset.post.PostEdit;
import org.spring.testspring.requset.post.PostSearch;
import org.spring.testspring.response.PostResponse;
import org.spring.testspring.service.PostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import jakarta.validation.Valid;

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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/posts")
    public void post(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid PostCreate request) {

            postService.write(userPrincipal.getUserId(),request);
    }


    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit) {
        postService.edit(postId, postEdit);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') && hasPermission(#postId, 'POST', 'DELETE')")
    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }

}
