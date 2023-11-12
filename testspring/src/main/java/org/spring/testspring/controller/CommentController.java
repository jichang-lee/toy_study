package org.spring.testspring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.testspring.requset.comment.CommentCreate;
import org.spring.testspring.requset.comment.CommentDelete;
import org.spring.testspring.service.CommentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public void write(@PathVariable Long postId ,@RequestBody @Valid CommentCreate request){
        commentService.write(postId,request);
    }

    @PostMapping("/comment/{commentId}/delete") //DeleteMapping 은 requestbody를 권장하지않는다
    public void delete(@PathVariable Long commentId , @RequestBody CommentDelete request){
        commentService.delete(commentId,request);
    }

}
